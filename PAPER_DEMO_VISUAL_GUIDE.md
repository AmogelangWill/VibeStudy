# Paper Demo — Visual Guide

## Accessing the Demo

1. Launch VibeStudy on your device or emulator.
2. Tap the hamburger menu icon (≡) in the top-left corner.
3. Scroll down in the drawer and tap **"Paper Demo"** (light cyan background, subtitle: "Test paper viewing UI").

---

## Screen Layout

```
┌─────────────────────────────────────┐
│  ☰  [LOGO]                          │
├─────────────────────────────────────┤
│  Mathematics Paper 1 - 2023         │
│                                     │
│  ┌───────────────────────────────┐  │
│  │ QUESTION 1                    │  │
│  │ Sample question text...       │  │
│  └───────────────────────────────┘  │
│  ┌─────────────┬─────────────────┐  │
│  │   Ask AI    │   View Memo     │  │
│  └─────────────┴─────────────────┘  │
│                                     │
│  ┌───────────────────────────────┐  │
│  │ QUESTION 2                    │  │
│  │ ...                           │  │
│  └───────────────────────────────┘  │
│  ┌─────────────┬─────────────────┐  │
│  │   Ask AI    │   View Memo     │  │
│  └─────────────┴─────────────────┘  │
│                                     │
│  ┌───────────────────────────────┐  │
│  │ QUESTION 3                    │  │
│  │ ...                           │  │
│  └───────────────────────────────┘  │
│  ┌─────────────┬─────────────────┐  │
│  │   Ask AI    │   View Memo     │  │
│  └─────────────┴─────────────────┘  │
└─────────────────────────────────────┘
```

### After tapping "View Memo"

```
│  ┌───────────────────────────────┐  │
│  │ QUESTION 1                    │  │
│  │ Sample question text...       │  │
│  └───────────────────────────────┘  │
│  ┌─────────────┬─────────────────┐  │
│  │   Ask AI    │   Hide Memo     │  │  <- button text changes
│  └─────────────┴─────────────────┘  │
│  ┌───────────────────────────────┐  │
│  │ MEMORANDUM - QUESTION 1       │  │  <- memo appears
│  │                               │  │
│  │ Solution:                     │  │
│  │ 2x + 5 = 15                   │  │
│  │ x = 5                         │  │
│  │ Marks: [5]                    │  │
│  └───────────────────────────────┘  │
```

---

## Color Coding

- Question boxes: Light purple-blue tint with darker blue border
- Memo boxes: Light beige tint with reddish border
- Background: Light gray (#F5F5F5)
- Cards: White with subtle shadow

---

## Interactive Features

- Scroll up/down to see all 3 questions
- Tap "View Memo" to show, "Hide Memo" to hide
- Each question's memo toggles independently
- Ask AI button logs to console (placeholder)

---

## Testing Checklist

- [ ] App builds without errors
- [ ] Navigation drawer opens
- [ ] "Paper Demo" item is visible in drawer
- [ ] Tapping "Paper Demo" navigates to the screen
- [ ] All 3 questions are visible and scrollable
- [ ] "View Memo" button toggles memo display
- [ ] Button text changes to "Hide Memo" when memo is shown
- [ ] Each question's memo works independently

---

## Current Limitations

- Questions are placeholder boxes with text, not actual paper images
- "Ask AI" button is non-functional
- Only 3 sample questions shown

---

## Next Steps

1. Prepare real screenshots of paper questions and memos.
2. Decide on storage: local assets or cloud URLs.
3. Implement image loading with Coil.
4. Add AI integration for the Ask AI button.
5. Create a data model for real papers with metadata.

