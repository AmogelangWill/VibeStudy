#  HOW TO USE - PAST PAPERS DOWNLOAD & PROCESSING SYSTEM

##  **YOU HAVE 2 TOOLS:**

### **1. DOWNLOAD_GUIDE.md** - Manual download instructions
### **2. Scripts to organize and process papers**

---

##  **STEP-BY-STEP INSTRUCTIONS:**

### **STEP 1: Download Your First Papers (5 minutes)**

1. Open your browser
2. Go to: https://www.education.gov.za/Curriculum/NationalSeniorCertificate(NSC)Examinations/NSCPastExaminationpapers.aspx
3. Download these 4 files to test:
   - **Mathematics 2024 November P1** (Question Paper)
   - **Mathematics 2024 November P1 MEMO** (Memorandum)
   - **Physical Sciences 2024 November P1** (Question Paper)
   - **Physical Sciences 2024 November P1 MEMO** (Memorandum)

4. Save them all to: `/home/kali/Desktop/VibeStudy_Project/DATABASE/`

**Don't worry about the filenames - the script will fix them!**

---

### **STEP 2: Organize the Downloaded Files (1 minute)**

Open terminal and run:

```bash
cd /home/kali/Desktop/VibeStudy_Project
python3 scripts/organize_papers.py rename
```

**This will:**
-  Automatically rename files to the correct format
-  Example: `gr12-math-nov2024-p1.pdf` → `Grade_12_Mathematics_2024_November_P1.pdf`

---

### **STEP 3: Check What You Have**

```bash
python3 scripts/organize_papers.py list
```

**This shows:**
- All files organized by subject
- How many papers you have

---

### **STEP 4: Process the Papers (2-3 minutes per paper)**

```bash
cd /home/kali/Desktop/VibeStudy_Project
source venv/bin/activate
python scripts/process_all_pdfs.py
```

**This will:**
-  Convert PDFs to images (one image per page)
-  Detect questions automatically
-  Split pages where multiple questions exist
-  Create JSON manifests
-  Put everything in `app/src/main/assets/`

---

### **STEP 5: Build and Test the App**

```bash
./gradlew assembleDebug
```

**Then install on your phone and test!**

---

##  **REPEAT THE CYCLE:**

1. Download more papers
2. Run `organize_papers.py rename`
3. Run `process_all_pdfs.py`
4. Build app
5. Test

---

##  **PROGRESS TRACKING:**

### **Check what's missing from Phase 1:**

```bash
python3 scripts/organize_papers.py check
```

This shows which Phase 1 papers (2024 November) you still need to download.

---

##  **RECOMMENDED WORKFLOW:**

### **Week 1: Core Subjects (Phase 1)**
**Target: 20 papers** (2024 November only)

- Mathematics (P1, P2, + Memos) = 4 files
- Physical Sciences (P1, P2, + Memos) = 4 files  
- Life Sciences (P1, P2, + Memos) = 4 files
- English (P1, P2, P3, + Memos) = 6 files
- Accounting (P1, + Memo) = 2 files

**Daily schedule:**
- **Day 1:** Download + process Mathematics (4 files, ~15 mins)
- **Day 2:** Download + process Physical Sciences (4 files, ~15 mins)
- **Day 3:** Download + process Life Sciences (4 files, ~15 mins)
- **Day 4:** Download + process English (6 files, ~20 mins)
- **Day 5:** Download + process Accounting (2 files, ~10 mins)
- **Day 6-7:** Test app, fix any issues

---

### **Week 2-3: Expand Years (Phase 2)**
**Target: Add 2022-2023** for same subjects

- Same 5 subjects, but for 2022 and 2023
- ~60 papers total
- 30 minutes per day for 2 weeks

---

### **Week 4+: Complete Dataset (Phase 3)**
**Target: All 1,760 papers**

- Add remaining subjects
- Add earlier years (2015-2021)
- 1 hour per day at your own pace

---

##  **PRO TIPS:**

### **1. Batch Downloads:**
Download all papers for ONE subject and ONE year at a time.
Example: Get all Mathematics 2024 papers (Nov, June, Feb, Sept) together.

### **2. Process Immediately:**
After downloading 4-8 papers, process them right away so you can catch errors early.

### **3. Backup:**
After each successful processing batch, copy the DATABASE folder as backup.

### **4. Verify:**
Check the processed images in `app/src/main/assets/` to make sure they look good.

---

##  **TROUBLESHOOTING:**

### **Problem: Script doesn't rename a file**
**Solution:** The filename might be too different. Manually rename it to match the format:
```
Grade_12_{Subject}_{Year}_{Period}_{Paper}.pdf
Grade_12_{Subject}_{Year}_{Period}_{Paper}_MEMO.pdf
```

### **Problem: Processing fails for a paper**
**Solution:** The PDF might be corrupted. Re-download it.

### **Problem: Questions not splitting correctly**
**Solution:** This will happen sometimes. You can:
- Add the paper to a "manual review" list
- Process it later with adjustments
- Or just use full pages for now

---

##  **TRACKING YOUR PROGRESS:**

Create a simple text file `progress.txt`:

```
PHASE 1 (Core 2024 November):
[] Mathematics 2024 Nov P1 + MEMO
[] Mathematics 2024 Nov P2 + MEMO
[] Physical Sciences 2024 Nov P1 + MEMO
[] Physical Sciences 2024 Nov P2 + MEMO
[ ] Life Sciences 2024 Nov P1 + MEMO
[ ] Life Sciences 2024 Nov P2 + MEMO
...
```

---

##  **QUESTIONS?**

### **Q: Do I really need to download 1,760 papers?**
**A:** NO! Start with Phase 1 (20 papers). That's enough to launch the app and get user feedback.

### **Q: What if a website doesn't have a paper?**
**A:** Try the other sources. Some years/subjects might genuinely not be available. Document it and move on.

### **Q: Can I hire someone to help download?**
**A:** YES! Once you have the workflow tested (Phase 1), you can give someone the checklist and have them download while you process.

### **Q: How long does processing take?**
**A:** ~2-3 minutes per paper on your machine. You can run it overnight for large batches.

---

##  **YOUR GOAL FOR TODAY:**

1.  Download 4 papers (Maths + Physics 2024 Nov P1 + Memos)
2.  Run organize script
3.  Run process script
4.  See the processed images
5.  Build app
6.  Test on phone

**Time needed: 30-45 minutes**

---

##  **START NOW:**

Open your browser and go to:
https://www.education.gov.za/Curriculum/NationalSeniorCertificate(NSC)Examinations/NSCPastExaminationpapers.aspx

**Download your first 4 papers and let's GO!** 

