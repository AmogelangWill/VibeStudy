# Vibe Study - Android App Implementation

## Overview
Vibe Study is an Android app designed for South African students (Grades 10-12) to study using past exam papers, access a library of notes, and manage their study schedules.

## Features Implemented 

### 1. **Home Screen**
- 4 square buttons displayed in a grid layout:
  - Grade 10
  - Grade 11
  - Grade 12
  - MyStudySchedule
  - Library

### 2. **Grade Navigation (Grades 10, 11, 12)**
- Each grade shows a list of subjects in **alphabetical order**:
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

### 3. **Subject Navigation**
- **Years**: 2015-2025 (displayed in reverse chronological order)
- **Months**: March, June, September, November
- **Papers**: 
  - Paper 1 and Paper 2 for most subjects
  - Paper 1, 2, and 3 for English FAL
  - Papers are currently empty (ready for future content)

### 4. **MyStudySchedule Feature**
- Empty state shows: "No tasks or reminders available"
- Floating Action Button (+) to add new tasks
- Task creation with fields:
  - Subject
  - Type of task (Study or Home work)
  - Date (with date picker)
  - Time (with time picker)
  - Duration (in minutes)
  - Topic
- **Conflict Detection**: App checks for overlapping tasks and notifies user
- Task display shows all details in a card format
- Delete functionality for each task

### 5. **Library Feature**
- List of all subjects (alphabetically ordered)
- Each subject leads to topics page
- Topics are currently blank (ready for DBE Curriculum implementation)
- Placeholder for future Notes and Videos sections

### 6. **UI/UX Implementation**

#### App Bar (Header)
- Logo/App name in the center: "VIBE STUDY"
- Hamburger menu icon on the left
- Clean, professional design

#### Hamburger Menu
- Displays all three grades
- Quick navigation to any grade's subjects

#### Color Palette (Fully Implemented)
| Component | Color | Hex Code |
|-----------|-------|----------|
| App Background | White | #FFFFFF |
| Primary Buttons | Soft Orange | #FF9E4A |
| Secondary Buttons | Sky Blue | #4FA3F7 |
| Text | Deep Navy | #1C2A39 |
| Disabled/Placeholder | Light Gray | #A0A4A8 |
| Card Background | Very Light Gray | #F7F8FA |

#### Design Elements
- Rectangular buttons for list views
- Square buttons for main navigation
- Card-based layouts
- Rounded corners (12-16dp)
- Proper spacing and elevation
- Material Design 3 components

## Project Structure

```
app/src/main/java/com/example/myapplication/
├── data/
│   ├── Subject.kt (Subject enum, Grade enum, ExamMonth enum)
│   └── Task.kt (Task data class and TaskType enum)
├── navigation/
│   ├── Screen.kt (Navigation routes)
│   └── VibeStudyNavigation.kt (Navigation setup)
├── ui/
│   ├── components/
│   │   ├── AppBar.kt (Header and Drawer)
│   │   └── Buttons.kt (Reusable button components)
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
│       ├── Color.kt (Custom color palette)
│       ├── Theme.kt (App theme)
│       └── Type.kt (Typography)
├── viewmodel/
│   └── TaskViewModel.kt (Task management logic)
└── MainActivity.kt

```

## Technologies Used
- **Kotlin** for Android development
- **Jetpack Compose** for modern UI
- **Material Design 3** for UI components
- **Navigation Compose** for app navigation
- **ViewModel** for state management
- **Coroutines & Flow** for reactive data

## Dependencies Added
- `androidx.navigation:navigation-compose` - Navigation
- `androidx.compose.material:material-icons-extended` - Icons
- `androidx.lifecycle:lifecycle-viewmodel-compose` - ViewModel
- `androidx.work:work-runtime-ktx` - Background tasks (for future notifications)

## Permissions Added
- `POST_NOTIFICATIONS` - For task reminders
- `SCHEDULE_EXACT_ALARM` - For scheduled task notifications

## Features Ready for Future Implementation

### 1. **Exam Papers Content**
- PDF viewer integration
- Download functionality
- Offline access

### 2. **Library Content**
- DBE Curriculum topic breakdown per subject
- Notes section (handwritten/curated)
- Videos section (recommended educational videos)

### 3. **Guided Support Integration**
- Assisted study recommendations
- Smart revision planning
- Question answering

### 4. **Task Notifications**
- Actual notification delivery when task time arrives
- Background service for notification scheduling
- Notification sound and vibration

### 5. **Additional Features**
- User authentication
- Cloud sync for tasks and progress
- Study statistics and analytics
- Subject-specific icons from Resources folder

## How to Build and Run

1. Open the project in Android Studio
2. Sync Gradle dependencies
3. Connect an Android device or start an emulator (API 24+)
4. Click "Run" or press Shift+F10

## Notes

- All subjects are displayed in alphabetical order as requested
- English FAL has 3 papers, other subjects have 2 papers
- Task conflict detection prevents scheduling overlapping tasks
- App uses a forced light theme with the custom Vibe Study color palette
- Papers are currently empty placeholders
- Library topics are placeholders for DBE Curriculum content

## App Name
**Vibe Study** - Displayed in the Android launcher and app header

---

**Implementation Status**:  Complete (Phase 1)
All core features from the ReadMe.md have been implemented as specified.

