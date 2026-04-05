# PDF Processing and Question Cropping System

## Overview

This system processes exam PDFs, detects pages containing multiple questions, crops them, and generates manifests for the app.

---

## How It Works

### 1. Page Splitting

Two methods are used:

**Automatic whitespace detection**
- Renders each page at 2x zoom
- Analyzes horizontal whitespace gaps
- If a gap of at least 10% of page height is found, the page is cropped into two parts

**Manual splitting (fallback)**
- Used when auto-detection misses a split
- Based on manual page-to-question mapping
- Splits the page at 50% when two questions are detected on the same page

### 2. Question Cropping Results

**Mathematics P1 November 2024:**
- Q1 & Q2 on page 3 — cropped into separate images
- Q3 & Q4 on page 4 — cropped
- Q5 on page 5 — single image (multi-part question)
- Q6 on page 6 — single image
- Q7 & Q8 on page 7 — cropped
- Q9 & Q10 on page 8 — cropped
- Q11 & Q12 on page 9 — cropped

### 3. Memo Organization

Multiple memo pages are grouped per question:
- Q1: 2 pages
- Q2: 1 page
- Q3: 4 pages
- Q7: 5 pages
- etc.

---

## Scripts

### `scripts/split_pdf.py`

Renders PDF pages to images, detects horizontal whitespace gaps, and crops pages automatically.

```bash
python scripts/split_pdf.py <pdf_file> <output_dir> [--zoom=2.0] [--min-gap-ratio=0.10]
```

### `scripts/process_all_pdfs.py`

Master script that processes all PDFs in the DATABASE folder, applies automatic and manual splits, and generates JSON manifests.

```bash
source venv/bin/activate
python scripts/process_all_pdfs.py
```

---

## Output Structure

```
app/src/main/assets/
├── Grade_12_Mathematics_2024_November_P1.json
├── Grade_12_Mathematics_2024_November_P1_MEMO.json
├── Grade_12_Physical_Sciences_2024_November_P1.json
├── Grade_12_Physical_Sciences_2024_November_P1_MEMO.json
├── Mathematics P1 Nov 2024 Eng/
│   ├── Mathematics P1 Nov 2024 Eng_page_1.png
│   ├── Mathematics P1 Nov 2024 Eng_page_3_part1.png  (Q1)
│   ├── Mathematics P1 Nov 2024 Eng_page_3_part2.png  (Q2)
│   └── ...
├── Mathematics P1 Nov 2024 MEMO/
│   └── ...
└── ...
```

---

## Manifest Format

```json
{
  "grade": "12",
  "subject": "Mathematics",
  "year": "2024",
  "month": "November",
  "paper": "P1",
  "type": "question",
  "questions": {
    "Q1": {
      "images": ["Mathematics P1 Nov 2024 Eng_page_3_part1.png"]
    },
    "Q2": {
      "images": ["Mathematics P1 Nov 2024 Eng_page_3_part2.png"]
    }
  }
}
```

---

## Statistics

**PDFs processed:** 4
- Mathematics P1 Nov 2024 (question paper)
- Mathematics P1 Nov 2024 (memo)
- Physical Sciences P1 Nov 2024 (question paper)
- Physical Sciences P1 Nov 2024 (memo)

**Images generated:** 106 total
- Mathematics questions: 17 images (14 auto + 3 manual splits)
- Mathematics memos: 28 images
- Physical Sciences questions: 31 images
- Physical Sciences memos: 35 images

**Questions cropped:** 44 total (22 questions × 2 papers)

---

## Adding Future Papers

1. Add the PDF to the `DATABASE/` folder.
2. Update `scripts/process_all_pdfs.py` with the page mapping:

```python
"Your Paper.pdf": {
    "grade": "12",
    "subject": "Subject_Name",
    "year": "2024",
    "month": "November",
    "paper": "P1",
    "type": "question",
    "page_questions": {
        1: None,          # cover
        2: None,          # rules
        3: ["Q1", "Q2"],  # two questions on one page
        4: ["Q3"],        # single question
    }
}
```

3. Run the processing script:

```bash
source venv/bin/activate
python scripts/process_all_pdfs.py
```

Images and manifests are generated automatically.

---

## Dependencies

```bash
pip install pymupdf pillow numpy
```

## Notes

- The virtual environment (`venv/`) contains all Python dependencies.
- Images are generated at 2x zoom for high quality.
- The whitespace detection threshold can be adjusted with `--min-gap-ratio`.
- Manual splits use an exact 50/50 division by default.
