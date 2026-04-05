# Smart PDF Splitting and Menu Icons

## Changes

### 1. Smart PDF splitting

#### Problem
PDF splitting used only whitespace detection, causing inaccurate splits at arbitrary positions.

#### Solution
Created an intelligent splitter that detects "QUESTION" text markers in the PDF.

**How it works:**

1. **Primary method — text detection**
   - Scans the PDF for text matching "QUESTION"
   - Identifies text with larger font size, bold formatting, and black color (typical question headers)
   - Finds the position of each "QUESTION N" marker
   - Splits pages between question markers, using whitespace near the marker for a clean cut

2. **Fallback — whitespace detection**
   - If no "QUESTION" text is found, falls back to whitespace analysis
   - Searches for the largest gap in the middle third of the page
   - Only splits if the gap is significant (>10% of page height)

3. **Manual override**
   - Manual page mappings in `process_all_pdfs.py` still take priority
   - Useful when automatic detection does not match the actual structure

**Example output:**
```
Detected 2 QUESTION markers on page 2
Split page 2 into 2 parts
Split page 3 at 40.6% (Q1 / Q2)
Split page 7 at 57.4% (Q7 / Q8)
```

---

### 2. Hamburger menu icons restored

#### Problem
Menu showed only text, with no icons.

#### Solution
Restored Material Icons inside white rounded boxes for each drawer item.

**Menu structure:**
```
┌─────────────────────────────┐
│     [Vibe Study Logo]       │
│        Vibe Study           │
├─────────────────────────────┤
│ [Home icon]   Home        › │  (Mint Green)
├─────────────────────────────┤
│            GRADES           │
├─────────────────────────────┤
│ [School]      Grade 10    › │  (Soft Peach)
│ [School]      Grade 11    › │  (Soft Blue)
│ [School]      Grade 12    › │  (Soft Purple)
├─────────────────────────────┤
│ [Info]        About       › │  (Soft Pink)
├─────────────────────────────┤
│         Version 1.0.0       │
└─────────────────────────────┘
```

Icons: Material Icons `Home`, `School`, `Info` — each displayed in a 44dp white rounded box (12dp corners).

---

## Technical Details

### Smart PDF Splitter (`split_pdf.py`)

**Key functions:**

- `find_question_markers(page)` — extracts text with position and font info, filters for "QUESTION" text, returns markers with y-position and font size.
- `find_smart_split(page, img, markers)` — takes the two largest markers, calculates midpoint, finds whitespace near the split point.
- `find_whitespace_near(img, target_y)` — searches for blank rows near a target position, returns the blankest row.
- `find_horizontal_split_fallback(img)` — fallback whitespace detection using the middle third of the page.

### Menu Icons (`AppBar.kt`)

`DrawerCard` now accepts an `icon: ImageVector` parameter:

```kotlin
@Composable
private fun DrawerCard(
    title: String,
    backgroundColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
)
```

---

## Test Results

### PDF Splitting (Mathematics P1 Nov 2024)

| Page | Split | Result |
|------|-------|--------|
| Page 3 | 40.6% (Q1 / Q2) | Accurate |
| Page 4 | 44.2% (Q3 / Q4) | Accurate |
| Page 7 | 57.4% (Q7 / Q8) | Accurate |
| Page 8 | 39.4% (Q9 / Q10) | Accurate |
| Page 9 | 48.2% (Q11 / Q12) | Accurate |

### Build

Build is successful with 0 compilation errors.

---

## Notes

When adding a new PDF, the script first attempts to detect "QUESTION" text automatically. If found, it splits at clean boundaries near each marker. If not found, it falls back to whitespace detection. Manual mappings in `process_all_pdfs.py` always override automatic detection.

**Build date:** November 11, 2025

