# How to Use — Past Papers Download and Processing System

## Tools Available

1. `DOWNLOAD_GUIDE.md` — Manual download instructions
2. `scripts/organize_papers.py` — Auto-rename and organize files
3. `scripts/process_all_pdfs.py` — Convert PDFs to images, split questions, create manifests

---

## Step-by-Step Instructions

### Step 1: Download your first papers

1. Open a browser and go to: https://www.education.gov.za/Curriculum/NationalSeniorCertificate(NSC)Examinations/NSCPastExaminationpapers.aspx
2. Download these 4 files:
   - Mathematics 2024 November P1 (Question Paper)
   - Mathematics 2024 November P1 Memo
   - Physical Sciences 2024 November P1 (Question Paper)
   - Physical Sciences 2024 November P1 Memo
3. Save all files to the `DATABASE/` folder. Exact filenames do not matter — the organize script handles renaming.

### Step 2: Organize the downloaded files

```bash
cd /path/to/VibeStudy
python3 scripts/organize_papers.py rename
```

This renames files to the standard format, e.g. `gr12-math-nov2024-p1.pdf` → `Grade_12_Mathematics_2024_November_P1.pdf`.

### Step 3: Check what you have

```bash
python3 scripts/organize_papers.py list
```

### Step 4: Process the papers

```bash
source venv/bin/activate
python scripts/process_all_pdfs.py
```

This converts PDFs to images, detects questions, splits pages, and creates JSON manifests in `app/src/main/assets/`.

### Step 5: Build and test

```bash
./gradlew assembleDebug
```

---

## Repeat the Cycle

1. Download more papers
2. Run `organize_papers.py rename`
3. Run `process_all_pdfs.py`
4. Build the app
5. Test

---

## Phase 1 Recommended Workflow (Week 1)

Target: 20 papers (2024 November only)

| Day | Task |
|-----|------|
| 1 | Mathematics (4 files, ~15 min) |
| 2 | Physical Sciences (4 files, ~15 min) |
| 3 | Life Sciences (4 files, ~15 min) |
| 4 | English (6 files, ~20 min) |
| 5 | Accounting (2 files, ~10 min) |
| 6–7 | Test app, fix any issues |

---

## Phase 2 (Weeks 2–3)

Add 2022–2023 for the same 5 subjects (~60 papers). Spend about 30 minutes per day.

---

## Phase 3 (Ongoing)

Add remaining subjects and years 2015–2021 (~1,760 papers total). 1 hour per day at your own pace.

---

## Tips

- Download all papers for one subject and one year at a time.
- Process immediately after downloading a batch of 4–8 papers to catch errors early.
- Verify processed images in `app/src/main/assets/` to confirm they look correct.

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Script does not rename a file | Filename may be too different — rename manually to match the format |
| Processing fails for a paper | PDF may be corrupted — re-download it |
| Questions not splitting correctly | Flag the paper for manual review and process it later |

---

## Tracking Progress

Create `progress.txt` and update it after each download session:

```
PHASE 1 (Core 2024 November):
[x] Mathematics 2024 Nov P1 + Memo
[x] Mathematics 2024 Nov P2 + Memo
[x] Physical Sciences 2024 Nov P1 + Memo
[x] Physical Sciences 2024 Nov P2 + Memo
[ ] Life Sciences 2024 Nov P1 + Memo
...
```

Use this command to check what is missing from Phase 1:

```bash
python3 scripts/organize_papers.py check
```

---

## Common Questions

**Do I need to download all 1,760 papers?**
No. Phase 1 (20 papers) is enough to launch the app and get feedback.

**What if a website does not have a paper?**
Try the next source in `DOWNLOAD_GUIDE.md`. Document any papers that cannot be found.

**How long does processing take?**
About 2–3 minutes per paper. Large batches can be run overnight.

