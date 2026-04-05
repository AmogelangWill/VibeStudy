# Paper Demo - Visual Guide

## How to Access the Demo

### Step 1: Open the App
Launch VibeStudy on your device/emulator

### Step 2: Open Navigation Drawer
Tap the **hamburger menu icon** (≡) in the top-left corner

### Step 3: Navigate to Paper Demo
Scroll down in the drawer and tap the **"Paper Demo"** item
- It has a light cyan/blue background
- Subtitle: "Test paper viewing UI"
- Located above the "About" section

## What You'll See

### Screen Layout:

```
┌─────────────────────────────────────┐
│    [LOGO]                          │  <- App Bar
├─────────────────────────────────────┤
│  Mathematics Paper 1 - 2023         │  <- Paper Title
│                                     │
│  ┌───────────────────────────────┐ │
│  │ QUESTION 1                    │ │
│  │ This is a sample question 1   │ │
│  │ from the past paper.          │ │
│  │                               │ │
│  │ Lorem ipsum dolor sit amet... │ │
│  │                               │ │
│  │ Solve for x: 2x + 5 = 15      │ │
│  │                               │ │
│  │ a) Show your working          │ │
│  │ b) Verify your answer         │ │
│  └───────────────────────────────┘ │
│  ┌─────────────┬─────────────────┐ │
│  │ Ask Tutor   │   View Memo     │ │
│  └─────────────┴─────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ QUESTION 2                    │ │
│  │ ...                           │ │
│  └───────────────────────────────┘ │
│  ┌─────────────┬─────────────────┐ │
│  │ Ask Tutor   │   View Memo     │ │
│  └─────────────┴─────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ QUESTION 3                    │ │
│  │ ...                           │ │
│  └───────────────────────────────┘ │
│  ┌─────────────┬─────────────────┐ │
│  │ Ask Tutor   │   View Memo     │ │
│  └─────────────┴─────────────────┘ │
└─────────────────────────────────────┘
```

### When You Tap "View Memo":

```
┌─────────────────────────────────────┐
│  ┌───────────────────────────────┐ │
│  │ QUESTION 1                    │ │
│  │ Sample question text...       │ │
│  └───────────────────────────────┘ │
│  ┌─────────────┬─────────────────┐ │
│  │ Ask Tutor   │   Hide Memo     │ │  <- Button text changes
│  └─────────────┴─────────────────┘ │
│  ┌───────────────────────────────┐ │
│  │ MEMORANDUM - QUESTION 1       │ │  <- Memo appears
│  │                               │ │
│  │ Solution:                     │ │
│  │ 2x + 5 = 15                   │ │
│  │ 2x = 15 - 5                   │ │
│  │ 2x = 10                       │ │
│  │ x = 5                         │ │
│  │                               │ │
│  │ Verification: 2(5)+5=10+5=15 │ │
│  │                               │ │
│  │ Marks: [5]                    │ │
│  └───────────────────────────────┘ │
└─────────────────────────────────────┘
```

## Color Coding

- **Question boxes**: Light purple-blue tint with darker blue border
- **Memo boxes**: Light beige/cream tint with reddish border
- **Background**: Light gray (#F5F5F5)
- **Cards**: White with subtle shadow
- **Text**: Dark gray/blue for readability

## Interactive Features

 **Scroll**: Swipe up/down to see all 3 questions
 **Toggle Memo**: Tap "View Memo" to show, "Hide Memo" to hide
 **Independent Cards**: Each question's memo toggles independently
 **Ask Tutor Button**: Currently a placeholder (shows print in console)

## Testing Checklist

- [ ] App builds without errors
- [ ] Navigation drawer opens
- [ ] "Paper Demo" item is visible in drawer
- [ ] Tapping "Paper Demo" navigates to the screen
- [ ] All 3 questions are visible and scrollable
- [ ] "View Memo" button toggles memo display
- [ ] Button text changes to "Hide Memo" when memo is shown
- [ ] Each question's memo works independently
- [ ] Layout looks clean on your device

## Current Limitations (By Design)

 Questions are placeholder boxes with text (not actual paper images)
 "Ask Tutor" button doesn't do anything yet
 Only 3 sample questions shown
 No actual PDF or image loading

## Next Steps After Testing

Once you verify this works:
1. **Prepare real screenshots** of paper questions and memos
2. **Decide on storage**: Local files vs. cloud URLs
3. **Implement image loading** with Coil or similar
4. **Add guided-help integration** for the "Ask Tutor" feature
5. **Create a data model** for real papers with metadata

---

This is a **functional prototype** to validate the UX flow!

