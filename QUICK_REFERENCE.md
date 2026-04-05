#  QUICK REFERENCE - PAST PAPERS SYSTEM

##  **3 COMMANDS YOU NEED:**

### **1. Organize Downloaded Papers**
```bash
cd /home/kali/Desktop/VibeStudy_Project
python3 scripts/organize_papers.py rename
```
**What it does:** Renames all PDFs in DATABASE/ to standard format

---

### **2. Process Papers to Images**
```bash
cd /home/kali/Desktop/VibeStudy_Project
source venv/bin/activate
python scripts/process_all_pdfs.py
```
**What it does:** Converts PDFs to images, splits questions, creates manifests

---

### **3. Build & Install App**
```bash
cd /home/kali/Desktop/VibeStudy_Project
./gradlew assembleDebug
./gradlew installDebug
```
**What it does:** Builds APK and installs on phone

---

##  **DOWNLOAD SOURCES:**

1. **https://www.education.gov.za/** (Official DBE - BEST!)
2. **https://www.testpapers.co.za/** (Largest collection)
3. **https://www.saexampapers.co.za/**
4. **https://stanmorephysics.com/** (Sciences)
5. **https://education.gauteng.gov.za/** (Grade 10-11)

---

##  **FILE NAMING:**

Save downloaded PDFs as anything, script will fix them!

**Target format:**
```
Grade_12_Mathematics_2024_November_P1.pdf
Grade_12_Mathematics_2024_November_P1_MEMO.pdf
```

---

##  **PHASE 1 TARGET (Start Here):**

Download these for **2024 November ONLY**:

- [ ] Mathematics P1 + P2 + Memos (4 files)
- [ ] Physical Sciences P1 + P2 + Memos (4 files)
- [ ] Life Sciences P1 + P2 + Memos (4 files)
- [ ] English P1 + P2 + P3 + Memos (6 files)
- [ ] Accounting P1 + Memo (2 files)

**Total: 20 files | Time: 2-3 hours**

---

## ️ **WORKFLOW:**

1. Download 4-6 PDFs from websites
2. Save to `DATABASE/` folder
3. Run `organize_papers.py rename`
4. Run `process_all_pdfs.py`
5. Build app
6. Test
7. Repeat!

---

##  **TROUBLESHOOTING:**

| Problem | Solution |
|---------|----------|
| Script can't rename file | Check filename has: subject, year, paper number |
| Processing fails | PDF might be corrupted, re-download |
| Questions not splitting | Script will flag it for review |
| Build fails | Run `./gradlew clean assembleDebug` |

---

##  **CHECK PROGRESS:**

```bash
python3 scripts/organize_papers.py list   # Show all files
python3 scripts/organize_papers.py check  # Check missing
```

---

##  **STORAGE:**

- **4 papers:** ~2MB
- **Phase 1 (20 papers):** ~12MB
- **Phase 2 (60 papers):** ~40MB
- **Complete (1,760 papers):** ~1.2GB

**Totally manageable!**

---

## ⏱️ **TIME PER PAPER:**

- Download: 30 sec
- Process: 2-3 min
- **Total: ~3-4 min per paper**

---

##  **GOAL FOR TODAY:**

 Download 4-6 more papers  
 Process them  
 Test in app  
 See the results!

**Time needed: 30-45 minutes**

---

** Full Guide:** Read `HOW_TO_USE.md`  
** Download Info:** Read `DOWNLOAD_GUIDE.md`  
** System Overview:** Read `AUTOMATION_SYSTEM_READY.md`

