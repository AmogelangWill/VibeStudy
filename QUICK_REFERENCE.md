# Quick Reference — Past Papers System

## Commands

### Organize downloaded papers
```bash
python3 scripts/organize_papers.py rename
```
Renames all PDFs in `DATABASE/` to the standard format.

### Process papers to images
```bash
source venv/bin/activate
python scripts/process_all_pdfs.py
```
Converts PDFs to images, splits questions, creates manifests.

### Build and install
```bash
./gradlew assembleDebug
./gradlew installDebug
```

---

## Download Sources

1. https://www.education.gov.za/ (Official DBE — best)
2. https://www.testpapers.co.za/ (Largest collection)
3. https://www.saexampapers.co.za/
4. https://stanmorephysics.com/ (Sciences)
5. https://education.gauteng.gov.za/ (Grade 10–11)

---

## File Naming

Save downloaded PDFs under any name; the organize script handles renaming.

Target format:
```
Grade_12_Mathematics_2024_November_P1.pdf
Grade_12_Mathematics_2024_November_P1_MEMO.pdf
```

---

## Phase 1 Targets (2024 November only)

- [ ] Mathematics P1 + P2 + Memos (4 files)
- [ ] Physical Sciences P1 + P2 + Memos (4 files)
- [ ] Life Sciences P1 + P2 + Memos (4 files)
- [ ] English P1 + P2 + P3 + Memos (6 files)
- [ ] Accounting P1 + Memo (2 files)

Total: 20 files

---

## Workflow

1. Download 4–6 PDFs from the sources above
2. Save to `DATABASE/`
3. Run `organize_papers.py rename`
4. Run `process_all_pdfs.py`
5. Build app
6. Test
7. Repeat

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Script cannot rename a file | Check filename has subject, year, paper number |
| Processing fails | PDF may be corrupted; re-download |
| Questions not splitting | Script will flag it for review |
| Build fails | Run `./gradlew clean assembleDebug` |

---

## Progress Check

```bash
python3 scripts/organize_papers.py list   # Show all files
python3 scripts/organize_papers.py check  # Show missing files
```

---

## Storage Estimates

| Dataset | Size |
|---------|------|
| 4 papers | ~2 MB |
| Phase 1 (20 papers) | ~12 MB |
| Phase 2 (60 papers) | ~40 MB |
| Complete (1,760 papers) | ~1.2 GB |

---

## Reference

- Full guide: `HOW_TO_USE.md`
- Download info: `DOWNLOAD_GUIDE.md`
- System overview: `AUTOMATION_SYSTEM_READY.md`

