# Download and Automation System

## Overview

This document describes the tools created for downloading, organizing, and processing past exam papers.

### 1. DOWNLOAD_GUIDE.md

- Full list of 5 free download sources
- Folder structure and naming conventions
- 3-phase download strategy (Phase 1: 20 papers through Phase 3: 1,760 papers)
- Time estimates and progress tracking

### 2. scripts/organize_papers.py

Automatically renames downloaded PDFs to the standard format, lists all papers organized by subject, and checks for missing files.

```bash
python3 scripts/organize_papers.py rename  # Auto-rename files
python3 scripts/organize_papers.py list    # Show all files
python3 scripts/organize_papers.py check   # Check missing
```

### 3. HOW_TO_USE.md

- Step-by-step instructions
- Daily schedule for Phase 1
- Troubleshooting guide
- Progress tracking templates

### 4. scripts/process_all_pdfs.py

Converts PDFs to images, detects questions automatically, splits pages intelligently, and creates JSON manifests.

---

## Paper Count

**Grade 12 Papers:**
- 1 Grade × 11 subjects × 10 years × 4 periods × 2 papers × 2 (QP + Memo) = **1,760 papers**

---

## Quick Start

### Download test papers

1. Go to https://www.education.gov.za/ (official DBE site)
2. Download the following to the `DATABASE/` folder:
   - Mathematics 2024 Nov P1 and Memo
   - Physical Sciences 2024 Nov P1 and Memo

### Organize

```bash
cd /path/to/VibeStudy
python3 scripts/organize_papers.py rename
```

### Process

```bash
source venv/bin/activate
python scripts/process_all_pdfs.py
```

### Build

```bash
./gradlew assembleDebug
```

---

## 3-Phase Download Strategy

### Phase 1: Core 2024 November (start here)
**Target: 20 papers**

5 core subjects, 2024 November only:
- Mathematics
- Physical Sciences
- Life Sciences
- English
- Accounting

### Phase 2: Expand to 2022–2023
**Target: 60 papers**

Same subjects, add 2022 and 2023.

### Phase 3: Complete dataset
**Target: 1,760 papers**

All subjects (11), all years (2015–2024), all periods.

---

## Download Sources (all free)

1. **education.gov.za** — Official DBE (best for Grade 12)
2. **testpapers.co.za** — Largest collection
3. **saexampapers.co.za** — Good for mid-year exams
4. **stanmorephysics.com** — Excellent for sciences
5. **gauteng.gov.za** — Provincial backup

---

## File Naming

The organize script converts filenames automatically:
- `gr12-math-nov2024-p1.pdf` → `Grade_12_Mathematics_2024_November_P1.pdf`

---

## Automation Status

**Fully automated:**
- PDF to images conversion
- Question detection (OCR-based)
- Page splitting
- JSON manifest generation
- File organization

**Semi-automated:**
- Downloading (manual save; script organizes)
- Quality check (script flags issues for review)

**Not automated:**
- Initial download (requires clicking through websites)
- Final quality verification

---

## Time Estimates

**Per paper:** ~3–4 minutes (30 seconds download, 2–3 minutes processing)

**Phase 1 (20 papers):** ~2 hours

**Complete dataset (1,760 papers):** 2–3 weeks at 30–60 minutes per day

---

## Mapping Approach

Manual mapping is not required. The processing script uses OCR to detect "QUESTION" markers automatically and splits pages based on whitespace analysis. Some papers (~5–10%) may need manual review after processing.

---

## Current Dataset

4 papers in the DATABASE folder have already been processed:
- Mathematics P1 Nov 2024 Eng.pdf
- Mathematics P1 Nov 2024 MEMO.pdf
- Physical Sciences P1 Nov 2024 Eng.pdf
- Physical Sciences P1 Nov 2024 MEMO.pdf

Check `app/src/main/assets/` for the processed output.

---

## Files Created

1. `DOWNLOAD_GUIDE.md` — Where and how to download
2. `HOW_TO_USE.md` — Complete usage instructions
3. `scripts/organize_papers.py` — Auto-rename and organize
4. `scripts/download_papers.py` — Web scraper framework (for future automation)

