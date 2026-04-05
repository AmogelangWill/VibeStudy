# Vibe Study — Android App Implementation

## Overview

Vibe Study is an Android app for South African students (Grades 10–12) covering past exam papers, a library of notes, and study schedule management.

## Features Implemented

### 1. Home Screen
- Grid of square buttons: Grade 10, Grade 11, Grade 12, MyStudySchedule, Library

### 2. Grade Navigation (Grades 10, 11, 12)
Subjects per grade (alphabetical order):
- Accounting
- Business
- Economics
- English FAL
- Geography
- History
- Life Orientation
- Life Sciences
- Mathematics
- Mathematics Literacy
- Physical Sciences

### 3. Subject Navigation
- **Years:** 2015–2025 (reverse chronological)
- **Months:** March, June, September, November
- **Papers:** Paper 1 and Paper 2 for most subjects; Paper 1, 2, and 3 for English FAL

### 4. MyStudySchedule
- Empty state: "No tasks or reminders available"
- Floating Action Button (+) to add tasks
- Task fields: Subject, Type (Study / Homework), Date, Time, Duration (minutes), Topic
- Conflict detection: warns if tasks overlap
- Task cards with delete functionality

### 5. Library
- Alphabetical subject list
- Each subject leads to a topics page (blank — ready for DBE Curriculum content)
- Placeholder structure for Notes and Videos

### 6. UI/UX

#### App Bar
- Logo/app name centered: "VIBE STUDY"
- Hamburger menu icon on the left

#### Hamburger Menu
- Shows Grade 10, 11, 12 for quick navigation

#### Color Palette

| Component | Color | Hex |
|-----------|-------|-----|
| App Background | White | #FFFFFF |
| Primary Buttons | Soft Orange | #FF9E4A |
| Secondary Buttons | Sky Blue | #4FA3F7 |
| Text | Deep Navy | #1C2A39 |
| Disabled/Placeholder | Light Gray | #A0A4A8 |
| Card Background | Very Light Gray | #F7F8FA |

#### Design
- Rectangular buttons for list views
- Square buttons for main navigation
- Card-based layouts, rounded corners (12–16dp)
- Material Design 3

## Project Structure

```
app/src/main/java/com/example/myapplication/
├── data/
│   ├── Subject.kt
│   └── Task.kt
├── navigation/
│   ├── Screen.kt
│   └── VibeStudyNavigation.kt
├── ui/
│   ├── components/
│   │   ├── AppBar.kt
│   │   └── Buttons.kt
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── SubjectListScreen.kt
│   │   ├── YearListScreen.kt
│   │   ├── MonthListScreen.kt
│   │   ├── PaperListScreen.kt
│   │   ├── MyStudyScheduleScreen.kt
│   │   ├── LibrarySubjectsScreen.kt
│   │   └── LibraryTopicsScreen.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── viewmodel/
│   └── TaskViewModel.kt
└── MainActivity.kt
```

## Technologies

- Kotlin
- Jetpack Compose
- Material Design 3
- Navigation Compose
- ViewModel + Coroutines/Flow

## Dependencies Added

- `androidx.navigation:navigation-compose`
- `androidx.compose.material:material-icons-extended`
- `androidx.lifecycle:lifecycle-viewmodel-compose`
- `androidx.work:work-runtime-ktx`

## Permissions

- `POST_NOTIFICATIONS`
- `SCHEDULE_EXACT_ALARM`

## Planned Features (not yet implemented)

- PDF viewer and offline paper access
- DBE Curriculum topic breakdown per subject
- Notes and video content per topic
- AI-assisted study recommendations
- Actual notification delivery
- User authentication and cloud sync

## How to Build and Run

1. Open the project in Android Studio
2. Sync Gradle dependencies
3. Connect an Android device or start an emulator (API 24+)
4. Click Run (Shift+F10)

## Notes

- Subjects are displayed in alphabetical order
- English FAL has 3 papers; all other subjects have 2
- Task conflict detection prevents overlapping tasks
- App uses a forced light theme with the custom color palette
- Papers and library topics are currently placeholders

