#!/usr/bin/env python3
"""
Web scraper to download past papers from multiple sources.
Targets Grade 12 papers for all subjects, years, and exam periods.

Now includes a generic crawler that, given one or more seed URLs, will:
- crawl pages within the same domain up to a small depth/page budget
- discover PDF links
- infer metadata (subject/year/period/paper/memo) from link text/URL
- generate standard filenames and download with retry + skip logic

Usage examples:
    python download_papers.py --seeds https://www.saexampapers.co.za/grade-12 --max-depth 2 --max-pages 60
    python download_papers.py --seeds https://www.testpapers.co.za --workers 6 --dry-run
    python download_papers.py --default-seeds --subjects "Mathematics" "Physical Sciences" --years 2022 2023

Note: Site structures change. Prefer passing a focused seed URL per subject/year page when possible.
"""

import requests
from bs4 import BeautifulSoup
import os
import time
import re
from pathlib import Path
from urllib.parse import urljoin, urlparse
import json
from dataclasses import dataclass, asdict
from datetime import datetime, timezone
from collections import deque
from concurrent.futures import ThreadPoolExecutor, as_completed
import argparse

# Configuration
GRADE = "12"
SUBJECTS = [
    "Mathematics",
    "Physical Sciences",
    "Life Sciences",
    "English Home Language",
    "Afrikaans First Additional Language",
    "Accounting",
    "Economics",
    "Geography",
    "History",
    "Business Studies",
    "Mathematical Literacy"
]

YEARS = list(range(2015, 2025))  # 2015 to 2024 (10 years)
EXAM_PERIODS = ["February-March", "May-June", "September", "November"]
PAPER_TYPES = ["P1", "P2"]  # Paper 1 and Paper 2

# Output directory
OUTPUT_DIR = Path(__file__).parent.parent / "DATABASE"
OUTPUT_DIR.mkdir(exist_ok=True)

# Download log
DOWNLOAD_LOG = OUTPUT_DIR / "download_log.json"


@dataclass
class PaperRecord:
    subject: str
    year: int
    period: str
    paper: str
    is_memo: bool
    source: str
    url: str
    filename: str
    size: int | None = None
    sha256: str | None = None


class PaperDownloader:
    def __init__(self):
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
        })
        self.downloaded = self.load_log()
        # Precompile regexes
        self.year_re = re.compile(r"\b(201[5-9]|202[0-4])\b")
        self.paper_re = re.compile(r"\b(?:paper|p)\s*([1-3])\b", re.I)
        self.paper_short_re = re.compile(r"\bP([1-3])\b", re.I)
        self.memo_re = re.compile(r"\b(memo|memorandum|solutions?|ans(?:wers)?)\b", re.I)
        self.period_keywords = {
            "feb": "February-March",
            "march": "February-March",
            "may": "May-June",
            "june": "May-June",
            "sept": "September",
            "sep": "September",
            "nov": "November",
            "final": "November",
            "nsc": "November",
        }
        # Subject alias mapping for detection
        self.subject_aliases = self._build_subject_aliases()

    def load_log(self):
        """Load download log to avoid re-downloading. Returns dict[filename] -> record."""
        if DOWNLOAD_LOG.exists():
            try:
                with open(DOWNLOAD_LOG, 'r') as f:
                    data = json.load(f)
                # Backward compatibility if old format (flat dict)
                if isinstance(data, dict) and 'papers' in data:
                    return data['papers'] or {}
                elif isinstance(data, dict):
                    return data
            except Exception:
                pass
        return {}

    def save_log(self):
        """Save download log in structured schema."""
        payload = {
            "version": 1,
            "generated_at": datetime.now(timezone.utc).isoformat(),
            "papers": self.downloaded,
        }
        with open(DOWNLOAD_LOG, 'w') as f:
            json.dump(payload, f, indent=2)

    def generate_filename(self, subject, year, period, paper, is_memo=False):
        """Generate standardized filename"""
        subject_clean = subject.replace(" ", "_")
        period_clean = period.replace("-", "_")
        memo_suffix = "_MEMO" if is_memo else ""
        return f"Grade_{GRADE}_{subject_clean}_{year}_{period_clean}_{paper}{memo_suffix}.pdf"

    def build_output_path(self, subject: str, year: int, period: str, filename: str) -> Path:
        """Organize files into subfolders Subject/Year/Period for easier browsing."""
        safe_subject = subject.replace(" ", "_")
        safe_period = period.replace("-", "_")
        folder = OUTPUT_DIR / safe_subject / str(year) / safe_period
        folder.mkdir(parents=True, exist_ok=True)
        return folder / filename

    def _build_subject_aliases(self):
        aliases = {}
        for subj in SUBJECTS:
            key = subj.lower()
            a = set([key, key.replace(" ", ""), key.replace(" ", "_")])
            # Common synonyms
            if subj == "Mathematics":
                a.update(["maths", "mathematics", "math", "pure maths"])
            if subj == "Mathematical Literacy":
                a.update(["maths lit", "mathematical literacy", "math lit"]) 
            if subj == "Physical Sciences":
                a.update(["physics", "physical science", "physical sciences"]) 
            if subj == "Life Sciences":
                a.update(["life science", "biology"]) 
            if subj == "English Home Language":
                a.update(["english hl", "english home language", "english home"]) 
            if subj == "Afrikaans First Additional Language":
                a.update(["afrikaans fal", "afrikaans first additional language", "afrikaans first add"]) 
            aliases[subj] = a
        return aliases

    def infer_subject(self, text: str, url: str) -> str | None:
        blob = f"{text} {url}".lower()
        for subj, alias_set in self.subject_aliases.items():
            for alias in alias_set:
                if alias in blob:
                    return subj
        return None

    def infer_year(self, text: str, url: str) -> int | None:
        blob = f"{text} {url}"
        m = self.year_re.search(blob)
        if m:
            return int(m.group(1))
        return None

    def infer_paper(self, text: str, url: str) -> str | None:
        blob = f"{text} {url}"
        m = self.paper_re.search(blob)
        if m:
            return f"P{m.group(1)}"
        m = self.paper_short_re.search(blob)
        if m:
            return f"P{m.group(1)}"
        return None

    def infer_memo(self, text: str, url: str) -> bool:
        blob = f"{text} {url}"
        return bool(self.memo_re.search(blob))

    def infer_period(self, text: str, url: str) -> str:
        blob = f"{text} {url}".lower()
        for key, norm in self.period_keywords.items():
            if key in blob:
                return norm
        # Default unknown period to November to keep filenames consistent if missing
        return "November"

    def download_file(self, url, filepath):
        """Download a file with retry logic"""
        max_retries = 3
        for attempt in range(max_retries):
            try:
                print(f"  📥 Downloading: {filepath.name}")
                response = self.session.get(url, timeout=30, stream=True, allow_redirects=True)
                response.raise_for_status()
                tmp_path = filepath.with_suffix(filepath.suffix + ".part")
                with open(tmp_path, 'wb') as f:
                    for chunk in response.iter_content(chunk_size=8192):
                        if chunk:
                            f.write(chunk)

                # Atomic move into place
                os.replace(tmp_path, filepath)
                print(f"  ✅ Downloaded: {filepath.name} ({filepath.stat().st_size // 1024} KB)")
                return True

            except Exception as e:
                print(f"  ⚠️  Attempt {attempt + 1}/{max_retries} failed: {e}")
                # exponential backoff with cap
                sleep_s = min(8, 1.5 ** attempt)
                time.sleep(sleep_s)

        print(f"  ❌ Failed to download: {filepath.name}")
        return False

    def discover_pdfs_from_seed(self, seed_url: str, max_pages: int = 40, max_depth: int = 2, same_domain: bool = True):
        """Crawl starting from seed_url to find PDF links. Returns list of tuples (pdf_url, anchor_text, source)."""
        print(f"\n🔎 Crawling seed: {seed_url}")
        parsed_seed = urlparse(seed_url)
        domain = parsed_seed.netloc
        q = deque([(seed_url, 0)])
        visited = set()
        found = []
        pages_fetched = 0
        source = domain.split(':')[0]

        while q and pages_fetched < max_pages:
            url, depth = q.popleft()
            if url in visited or depth > max_depth:
                continue
            visited.add(url)
            try:
                resp = self.session.get(url, timeout=30)
                if 'text/html' not in resp.headers.get('Content-Type', ''):
                    continue
                pages_fetched += 1
                soup = BeautifulSoup(resp.text, 'html.parser')
                for a in soup.find_all('a', href=True):
                    href = a['href']
                    full = urljoin(url, href)
                    # Filter fragments/mailto
                    if full.startswith('mailto:') or full.startswith('javascript:'):
                        continue
                    # PDF link
                    if full.lower().endswith('.pdf'):
                        found.append((full, a.get_text(strip=True) or '', source))
                        continue
                    # Enqueue next HTML page
                    if same_domain:
                        if urlparse(full).netloc != domain:
                            continue
                    # Heuristic: follow pages likely relevant
                    if any(str(y) in full for y in YEARS) or any(k in full.lower() for k in ["grade", "paper", "memo", "exam", "nsc", "nov", "sept", "may", "june", "feb"]):
                        if full not in visited:
                            q.append((full, depth + 1))
            except Exception as e:
                print(f"   ⚠️  Error fetching {url}: {e}")
                continue

        print(f"   ➜ Found {len(found)} PDF links from {seed_url}")
        return found

    def scrape_testpapers_co_za(self):
        """Scrape testpapers.co.za (biggest source)"""
        print("\n🔍 Scraping testpapers.co.za...")
        base_url = "https://www.testpapers.co.za"

        # Try to find Grade 12 page
        try:
            # Generic crawl seeded at homepage (can be narrowed via CLI seeds)
            return self.discover_pdfs_from_seed(base_url, max_pages=40, max_depth=2, same_domain=True)
        except Exception as e:
            print(f"❌ Error scraping testpapers.co.za: {e}")
            return []

    def scrape_saexampapers_co_za(self):
        """Scrape saexampapers.co.za"""
        print("\n🔍 Scraping saexampapers.co.za...")
        base_url = "https://www.saexampapers.co.za/grade-12"

        try:
            return self.discover_pdfs_from_seed(base_url, max_pages=60, max_depth=2, same_domain=True)
        except Exception as e:
            print(f"❌ Error scraping saexampapers.co.za: {e}")
            return []

    def scrape_dbe_gov_za(self):
        """Scrape official DBE papers"""
        print("\n🔍 Scraping education.gov.za (Official DBE)...")
        base_url = "https://www.education.gov.za"

        try:
            return self.discover_pdfs_from_seed(base_url, max_pages=30, max_depth=1, same_domain=True)
        except Exception as e:
            print(f"❌ Error scraping DBE: {e}")
            return []

    def scrape_stanmorephysics_com(self):
        """Scrape stanmorephysics.com (good for sciences)"""
        print("\n🔍 Scraping stanmorephysics.com...")
        base_url = "https://stanmorephysics.com"

        try:
            return self.discover_pdfs_from_seed(base_url, max_pages=40, max_depth=2, same_domain=True)
        except Exception as e:
            print(f"❌ Error scraping stanmorephysics: {e}")
            return []

    def process_discovered_links(self, discovered, subjects_filter=None, years_filter=None, periods_filter=None, papers_filter=None, dry_run=False, force=False, workers=4):
        """Turn discovered links into downloads via metadata inference and filename logic."""
        queue: list[tuple[PaperRecord, str, Path]] = []

        for url, anchor_text, source in discovered:
            subj = self.infer_subject(anchor_text, url) or "Unknown_Subject"
            year = self.infer_year(anchor_text, url) or None
            paper = self.infer_paper(anchor_text, url) or "P1"
            is_memo = self.infer_memo(anchor_text, url)
            period = self.infer_period(anchor_text, url)

            # Apply filters
            if subjects_filter and subj not in subjects_filter:
                continue
            if years_filter and (year is None or year not in years_filter):
                continue
            if periods_filter and period not in periods_filter:
                continue
            if papers_filter and paper not in papers_filter:
                continue
            # Require year inference for meaningful organization
            if year is None:
                continue

            filename = self.generate_filename(subj, year, period, paper, is_memo)
            out_path = self.build_output_path(subj, year, period, filename)
            rec = PaperRecord(
                subject=subj,
                year=year,
                period=period,
                paper=paper,
                is_memo=is_memo,
                source=source,
                url=url,
                filename=filename,
            )
            queue.append((rec, url, out_path))

        # Deduplicate by filename, prefer first occurrence
        unique = {}
        for rec, url, path in queue:
            if rec.filename not in unique:
                unique[rec.filename] = (rec, url, path)

        tasks = list(unique.values())
        print(f"\n📦 Ready to download: {len(tasks)} files after filtering and dedup")

        if dry_run:
            for rec, _, path in tasks[:25]:  # show a sample
                print(f"  • {rec.filename}  <- {rec.url}")
            if len(tasks) > 25:
                print(f"  • ... and {len(tasks) - 25} more")
            return 0

        def do_download(item):
            rec, url, path = item
            if not force and (rec.filename in self.downloaded or path.exists()):
                return (rec.filename, 'skipped')
            ok = self.download_file(url, path)
            if ok:
                self.downloaded[rec.filename] = asdict(rec)
                try:
                    self.downloaded[rec.filename]['size'] = path.stat().st_size
                except Exception:
                    pass
                return (rec.filename, 'downloaded')
            return (rec.filename, 'failed')

        completed = 0
        failed = 0
        skipped = 0
        if workers and workers > 1:
            with ThreadPoolExecutor(max_workers=workers) as pool:
                futures = [pool.submit(do_download, t) for t in tasks]
                for fut in as_completed(futures):
                    fname, status = fut.result()
                    if status == 'downloaded':
                        completed += 1
                    elif status == 'skipped':
                        skipped += 1
                    else:
                        failed += 1
        else:
            for t in tasks:
                fname, status = do_download(t)
                if status == 'downloaded':
                    completed += 1
                elif status == 'skipped':
                    skipped += 1
                else:
                    failed += 1

        print(f"\n✅ Downloads finished. Downloaded={completed}, Skipped={skipped}, Failed={failed}")
        return completed

    def run_all_scrapers(self, seeds=None, subjects=None, years=None, periods=None, papers=None, dry_run=False, force=False, workers=4, max_pages=40, max_depth=2, same_domain=True):
        """Run all scrapers"""
        print("=" * 80)
        print("🚀 STARTING GRADE 12 PAST PAPERS DOWNLOAD")
        print("=" * 80)
        print(f"Target: {len(SUBJECTS)} subjects × {len(YEARS)} years × {len(EXAM_PERIODS)} periods × {len(PAPER_TYPES)} papers × 2 (QP+Memo)")
        print(f"Total target: ~{len(SUBJECTS) * len(YEARS) * len(EXAM_PERIODS) * len(PAPER_TYPES) * 2} papers")
        print(f"Output directory: {OUTPUT_DIR}")
        print("=" * 80)

        # Discover links
        discovered = []
        if seeds:
            for seed in seeds:
                discovered.extend(self.discover_pdfs_from_seed(seed, max_pages=max_pages, max_depth=max_depth, same_domain=same_domain))
        else:
            # Use built-in scrapers as generic crawls
            discovered.extend(self.scrape_testpapers_co_za())
            discovered.extend(self.scrape_saexampapers_co_za())
            discovered.extend(self.scrape_dbe_gov_za())
            discovered.extend(self.scrape_stanmorephysics_com())

        # Filter sets
        subj_filter = set(subjects) if subjects else None
        year_filter = set(int(y) for y in years) if years else None
        period_filter = set(periods) if periods else None
        paper_filter = set(papers) if papers else None

        # Process and download
        self.process_discovered_links(
            discovered,
            subjects_filter=subj_filter,
            years_filter=year_filter,
            periods_filter=period_filter,
            papers_filter=paper_filter,
            dry_run=dry_run,
            force=force,
            workers=workers,
        )

        # Save log
        self.save_log()

        print("\n" + "=" * 80)
        print(f"✅ SCRAPING COMPLETE")
        print(f"📊 Logged papers: {len(self.downloaded)}")
        print(f"💾 Output directory: {OUTPUT_DIR}")
        print("=" * 80)


def main():
    """Main entry point with CLI."""
    parser = argparse.ArgumentParser(description="Download Grade 12 past papers by crawling seed URLs for PDFs.")
    src = parser.add_mutually_exclusive_group()
    src.add_argument("--seeds", nargs="*", help="Seed URLs to crawl for PDF links.")
    src.add_argument("--default-seeds", action="store_true", help="Use built-in generic seeds/scrapers.")

    parser.add_argument("--subjects", nargs="*", choices=SUBJECTS, help="Subjects to include (default: all).")
    parser.add_argument("--years", nargs="*", type=int, help="Years to include (e.g., 2022 2023).")
    parser.add_argument("--periods", nargs="*", choices=EXAM_PERIODS, help="Exam periods to include.")
    parser.add_argument("--papers", nargs="*", choices=PAPER_TYPES, help="Paper types to include (P1/P2/P3).")

    parser.add_argument("--max-pages", type=int, default=40, help="Max pages to fetch per seed.")
    parser.add_argument("--max-depth", type=int, default=2, help="Max link depth per seed.")
    parser.add_argument("--any-domain", action="store_true", help="Allow crawler to leave the seed's domain (not recommended).")

    parser.add_argument("--workers", type=int, default=4, help="Concurrent download workers.")
    parser.add_argument("--dry-run", action="store_true", help="List what would be downloaded without fetching.")
    parser.add_argument("--force", action="store_true", help="Re-download even if exists in log.")

    args = parser.parse_args()

    downloader = PaperDownloader()
    seeds = args.seeds if args.seeds else (None if args.default_seeds else None)
    downloader.run_all_scrapers(
        seeds=seeds,
        subjects=args.subjects,
        years=args.years,
        periods=args.periods,
        papers=args.papers,
        dry_run=args.dry_run,
        force=args.force,
        workers=max(1, args.workers),
        max_pages=args.max_pages,
        max_depth=args.max_depth,
        same_domain=not args.any_domain,
    )


if __name__ == "__main__":
    main()

