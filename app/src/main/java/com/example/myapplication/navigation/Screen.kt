package com.example.myapplication.navigation

import com.example.myapplication.data.ExamMonth
import com.example.myapplication.data.Grade
import com.example.myapplication.data.Subject

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object About : Screen("about")
    object GradeSelection : Screen("grade_selection")
    object SubjectList : Screen("subject_list/{grade}") {
        fun createRoute(grade: Grade) = "subject_list/${grade.name}"
    }
    object YearList : Screen("year_list/{grade}/{subject}") {
        fun createRoute(grade: Grade, subject: Subject) = "year_list/${grade.name}/${subject.name}"
    }
    object MonthList : Screen("month_list/{grade}/{subject}/{year}") {
        fun createRoute(grade: Grade, subject: Subject, year: Int) =
            "month_list/${grade.name}/${subject.name}/$year"
    }
    object PaperList : Screen("paper_list/{grade}/{subject}/{year}/{month}") {
        fun createRoute(grade: Grade, subject: Subject, year: Int, month: ExamMonth) =
            "paper_list/${grade.name}/${subject.name}/$year/${month.name}"
    }
    object MyStudySchedule : Screen("my_study_schedule")
    object Library : Screen("library")
    object LibrarySubjects : Screen("library_subjects")
    object LibraryTopics : Screen("library_topics/{subject}") {
        fun createRoute(subject: Subject) = "library_topics/${subject.name}"
    }
    object PaperView : Screen("paper_view/{manifest}") {
        fun createRoute(manifest: String) = "paper_view/$manifest"
    }
    object Schedule : Screen("schedule")
}

