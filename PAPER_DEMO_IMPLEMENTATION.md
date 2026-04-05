# Paper Demo Implementation

## Files Created

### 1. Data Model — `Question.kt`
Simple data class with `id`, `imageRes`, and `memoRes` fields.
Location: `app/src/main/java/com/example/myapplication/model/`

### 2. QuestionCard Component — `QuestionCard.kt`
Reusable composable for displaying a question card. Features:
- Full-width question image display
- Two equal-width buttons: "Ask AI" and "View Memo"
- Independent toggle to show/hide the memo
- Rounded corners, subtle elevation

Location: `app/src/main/java/com/example/myapplication/ui/components/`

### 3. Paper Demo Screen — `PaperDemoScreen.kt`
Full demo screen with a paper title at top and a scrollable list of 3 sample questions. Each question uses colored placeholder boxes with sample text to demonstrate the layout before real images are available.

Location: `app/src/main/java/com/example/myapplication/ui/screens/`

### 4. Navigation Integration
- Added `PaperDemo` route to `Screen.kt`
- Added composable route in `VibeStudyNavigation.kt`
- Added "Paper Demo" menu item to the drawer in `AppBar.kt` (light cyan background, subtitle: "Test paper viewing UI")

### 5. Placeholder Drawables
Six XML drawable files in `res/drawable/`:
- `demo_q1.xml`, `demo_q2.xml`, `demo_q3.xml`
- `demo_memo1.xml`, `demo_memo2.xml`, `demo_memo3.xml`

---

## How to Test

1. Build and run the app.
2. Open the hamburger menu.
3. Tap "Paper Demo".
4. Scroll through the 3 question cards.
5. Tap "View Memo" to toggle memo visibility; tap "Hide Memo" to collapse.
6. Verify each question's memo toggles independently.

---

## Question Card Layout

```
┌─────────────────────────────┐
│  [Question Box - Blue tint] │
│  QUESTION 1                 │
│  Sample question text...    │
├─────────────┬───────────────┤
│  Ask AI     │  View Memo    │
└─────────────┴───────────────┘
│  [Memo Box - Red tint]      │  <- shown when View Memo is tapped
│  MEMORANDUM - QUESTION 1    │
│  Solution steps...          │
└─────────────────────────────┘
```

Color scheme:
- Question boxes: Light blue (#F5F5FA), blue border (#646496)
- Memo boxes: Light beige (#FAFAF5), red border (#966464)
- Cards: White with subtle elevation

---

## Next Steps

1. Replace placeholder boxes with actual paper screenshots.
2. Implement Ask AI functionality.
3. Add real data models for papers, questions, and memos.
4. Implement image caching for offline access.
5. Add loading states and error handling.

---

**Created:** November 10, 2025

