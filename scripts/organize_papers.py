#!/usr/bin/env python3
"""
File Renamer and Organizer for Past Papers
Automatically renames downloaded PDFs to the correct format
"""

import os
import re
from pathlib import Path
import shutil

DATABASE_DIR = Path(__file__).parent.parent / "DATABASE"

# Naming patterns to detect
SUBJECTS_MAP = {
    "math": "Mathematics",
    "maths": "Mathematics",
    "mathematics": "Mathematics",
    "physical science": "Physical_Sciences",
    "physicalscience": "Physical_Sciences",
    "physics": "Physical_Sciences",
    "life science": "Life_Sciences",
    "lifescience": "Life_Sciences",
    "biology": "Life_Sciences",
    "english": "English_Home_Language",
    "afrikaans": "Afrikaans_First_Additional_Language",
    "accounting": "Accounting",
    "economics": "Economics",
    "geography": "Geography",
    "history": "History",
    "business": "Business_Studies",
    "mathematical literacy": "Mathematical_Literacy",
    "maths lit": "Mathematical_Literacy",
}

PERIODS_MAP = {
    "feb": "February_March",
    "february": "February_March",
    "march": "February_March",
    "may": "May_June",
    "june": "May_June",
    "sept": "September",
    "september": "September",
    "nov": "November",
    "november": "November",
}


def extract_info_from_filename(filename):
    """Extract grade, subject, year, period, paper from filename"""
    filename_lower = filename.lower()

    # Extract year (4 digits)
    year_match = re.search(r'(20\d{2})', filename)
    year = year_match.group(1) if year_match else None

    # Extract subject
    subject = None
    for key, value in SUBJECTS_MAP.items():
        if key in filename_lower:
            subject = value
            break

    # Extract period
    period = None
    for key, value in PERIODS_MAP.items():
        if key in filename_lower:
            period = value
            break

    # Extract paper number
    paper = None
    if "p1" in filename_lower or "paper 1" in filename_lower or "paper1" in filename_lower:
        paper = "P1"
    elif "p2" in filename_lower or "paper 2" in filename_lower or "paper2" in filename_lower:
        paper = "P2"
    elif "p3" in filename_lower or "paper 3" in filename_lower or "paper3" in filename_lower:
        paper = "P3"

    # Check if memo
    is_memo = "memo" in filename_lower or "memorandum" in filename_lower

    # Grade (assume 12 for now)
    grade = "12"

    return {
        "grade": grade,
        "subject": subject,
        "year": year,
        "period": period,
        "paper": paper,
        "is_memo": is_memo
    }


def generate_standard_filename(info):
    """Generate standardized filename"""
    if not all([info["subject"], info["year"], info["period"], info["paper"]]):
        return None

    memo_suffix = "_MEMO" if info["is_memo"] else ""
    return f"Grade_{info['grade']}_{info['subject']}_{info['year']}_{info['period']}_{info['paper']}{memo_suffix}.pdf"


def rename_files():
    """Rename all PDFs in DATABASE folder"""
    if not DATABASE_DIR.exists():
        print(f"❌ DATABASE folder not found: {DATABASE_DIR}")
        return

    pdf_files = list(DATABASE_DIR.glob("*.pdf"))

    if not pdf_files:
        print("❌ No PDF files found in DATABASE folder")
        return

    print(f"\n{'='*80}")
    print(f"📁 Found {len(pdf_files)} PDF files in DATABASE folder")
    print(f"{'='*80}\n")

    renamed_count = 0
    skipped_count = 0

    for pdf_file in pdf_files:
        # Skip if already in correct format
        if pdf_file.name.startswith("Grade_"):
            print(f"✅ Already correct: {pdf_file.name}")
            continue

        # Extract info
        info = extract_info_from_filename(pdf_file.name)
        new_name = generate_standard_filename(info)

        if new_name:
            new_path = DATABASE_DIR / new_name

            # Check if target already exists
            if new_path.exists():
                print(f"⚠️  Target exists: {new_name}")
                print(f"   Original: {pdf_file.name}")
                skipped_count += 1
            else:
                # Rename
                shutil.move(str(pdf_file), str(new_path))
                print(f"✨ Renamed:")
                print(f"   From: {pdf_file.name}")
                print(f"   To:   {new_name}")
                renamed_count += 1
        else:
            print(f"❌ Could not parse: {pdf_file.name}")
            print(f"   Info: {info}")
            skipped_count += 1

    print(f"\n{'='*80}")
    print(f"✅ Renamed: {renamed_count} files")
    print(f"⚠️  Skipped: {skipped_count} files")
    print(f"{'='*80}\n")


def list_files():
    """List all files in DATABASE folder"""
    if not DATABASE_DIR.exists():
        print(f"❌ DATABASE folder not found: {DATABASE_DIR}")
        return

    pdf_files = sorted(DATABASE_DIR.glob("*.pdf"))

    print(f"\n{'='*80}")
    print(f"📋 FILES IN DATABASE FOLDER ({len(pdf_files)} total)")
    print(f"{'='*80}\n")

    if not pdf_files:
        print("❌ No PDF files found")
        return

    # Group by subject
    by_subject = {}
    for pdf_file in pdf_files:
        # Extract subject from filename
        if pdf_file.name.startswith("Grade_"):
            parts = pdf_file.name.split("_")
            if len(parts) >= 3:
                subject = parts[2]
                if subject not in by_subject:
                    by_subject[subject] = []
                by_subject[subject].append(pdf_file.name)

    for subject, files in sorted(by_subject.items()):
        print(f"\n📚 {subject.replace('_', ' ')} ({len(files)} files):")
        for f in sorted(files):
            print(f"   - {f}")


def check_missing():
    """Check what papers are missing"""
    print("\n🔍 Checking for missing papers...")
    print("(This will compare against Phase 1 target: 2024 November papers)")

    phase1_subjects = ["Mathematics", "Physical_Sciences", "Life_Sciences", "English_Home_Language", "Accounting"]
    phase1_papers = ["P1", "P2"]

    missing = []

    for subject in phase1_subjects:
        for paper in phase1_papers:
            # Question paper
            qp_name = f"Grade_12_{subject}_2024_November_{paper}.pdf"
            qp_path = DATABASE_DIR / qp_name
            if not qp_path.exists():
                missing.append(qp_name)

            # Memo
            memo_name = f"Grade_12_{subject}_2024_November_{paper}_MEMO.pdf"
            memo_path = DATABASE_DIR / memo_name
            if not memo_path.exists():
                missing.append(memo_name)

    if missing:
        print(f"\n❌ Missing {len(missing)} files from Phase 1:")
        for m in missing:
            print(f"   - {m}")
    else:
        print("\n✅ All Phase 1 files present!")


if __name__ == "__main__":
    import sys

    print("\n" + "="*80)
    print("📁 PAST PAPERS FILE ORGANIZER")
    print("="*80)

    if len(sys.argv) > 1:
        command = sys.argv[1]

        if command == "rename":
            rename_files()
        elif command == "list":
            list_files()
        elif command == "check":
            check_missing()
        else:
            print(f"❌ Unknown command: {command}")
            print("\nUsage:")
            print("  python organize_papers.py rename  - Rename all PDFs to standard format")
            print("  python organize_papers.py list    - List all files")
            print("  python organize_papers.py check   - Check for missing Phase 1 files")
    else:
        print("\nUsage:")
        print("  python organize_papers.py rename  - Rename all PDFs to standard format")
        print("  python organize_papers.py list    - List all files")
        print("  python organize_papers.py check   - Check for missing Phase 1 files")

