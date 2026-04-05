# Past Papers Download Guide

## Target: 1,760 Grade 12 Papers

**Calculation:** 1 Grade × 11 subjects × 10 years (2015–2024) × 4 exam periods × 2 papers × 2 (QP + Memo) = **1,760 papers**

---

## Subjects

1. Mathematics
2. Physical Sciences
3. Life Sciences
4. English Home Language
5. Afrikaans First Additional Language
6. Accounting
7. Economics
8. Geography
9. History
10. Business Studies
11. Mathematical Literacy

---

## Download Sources (free)

### 1. education.gov.za (official DBE — start here)
URL: https://www.education.gov.za/Curriculum/NationalSeniorCertificate(NSC)Examinations/NSCPastExaminationpapers.aspx
Best for Grade 12 November papers. Usually covers 2020–2024, all subjects. Official source.

### 2. testpapers.co.za (largest collection)
URL: https://www.testpapers.co.za/
Navigate: Grade 12 → Subject → Year. Comprehensive coverage of all years and exam periods.

### 3. saexampapers.co.za
URL: https://www.saexampapers.co.za/
Good for mid-year exams (May/June, September).

### 4. stanmorephysics.com
URL: https://stanmorephysics.com/
Excellent for sciences (Physical Sciences, Life Sciences, Mathematics).

### 5. gauteng.gov.za
URL: https://education.gauteng.gov.za/pages/Exam-Papers.aspx
Gauteng provincial papers. Use as a backup source.

---

## Folder Structure

Save all files to the `DATABASE/` folder using this naming convention:

```
DATABASE/
├── Grade_12_Mathematics_2024_November_P1.pdf
├── Grade_12_Mathematics_2024_November_P1_MEMO.pdf
├── Grade_12_Mathematics_2024_November_P2.pdf
├── Grade_12_Mathematics_2024_November_P2_MEMO.pdf
├── Grade_12_Physical_Sciences_2024_November_P1.pdf
├── Grade_12_Physical_Sciences_2024_November_P1_MEMO.pdf
└── ...
```

**Naming convention:**
```
Grade_{GRADE}_{SUBJECT}_{YEAR}_{PERIOD}_{PAPER}.pdf
Grade_{GRADE}_{SUBJECT}_{YEAR}_{PERIOD}_{PAPER}_MEMO.pdf
```

Examples:
- `Grade_12_Mathematics_2024_November_P1.pdf`
- `Grade_12_Physical_Sciences_2023_June_P2_MEMO.pdf`
- `Grade_12_Life_Sciences_2022_February_March_P1.pdf`

---

## 3-Phase Download Strategy

### Phase 1: Core papers (start here)
**Target: ~20 files**

Download for 2024 November only:
- Mathematics (P1, P2, Memos) = 4 files
- Physical Sciences (P1, P2, Memos) = 4 files
- Life Sciences (P1, P2, Memos) = 4 files
- English (P1, P2, P3, Memos) = 6 files
- Accounting (P1, Memo) = 2 files

### Phase 2: Expand years
**Target: ~400 files**

Add 2022–2023 for the same 5 subjects.

### Phase 3: Complete dataset
**Target: 1,760 files**

Add remaining subjects and years (2015–2021).

---

## Time Estimates

- Phase 1 (core 2024): 2–3 hours
- Phase 2 (2022–2023): 1–2 days spread over a week
- Phase 3 (complete): 1–2 weeks at 30–60 minutes per day

---

## Tips

- Use the browser's download manager to download multiple files at once.
- Download by subject — finish one subject before moving to the next.
- Verify file sizes: most papers are 200 KB–2 MB. A 10 KB file is likely broken.
- The organize script auto-renames files, so exact naming on download is not required.

---

## Getting Started

1. Go to https://www.education.gov.za/Curriculum/NationalSeniorCertificate(NSC)Examinations/NSCPastExaminationpapers.aspx
2. Download **Mathematics 2024 November P1 + Memo**.
3. Save to the `DATABASE/` folder.
4. Run `python3 scripts/organize_papers.py rename` to standardize the filename.
5. Continue with the remaining Phase 1 papers.

If a source is unavailable, try the next one in the list. Document any papers that cannot be found for future reference.

