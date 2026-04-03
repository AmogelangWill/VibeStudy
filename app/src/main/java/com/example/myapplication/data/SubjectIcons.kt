package com.example.myapplication.data

import com.example.myapplication.R

object SubjectIcons {
    fun getIconForSubject(subject: Subject): Int {
        return when (subject) {
            Subject.ACCOUNTING -> R.drawable.ic_subject_accounting
            Subject.BUSINESS -> R.drawable.ic_grade // Using grade icon as placeholder for Business
            Subject.ECONOMICS -> R.drawable.ic_subject_economics
            Subject.ENGLISH_FAL -> R.drawable.ic_subject_english
            Subject.GEOGRAPHY -> R.drawable.ic_subject_geography
            Subject.HISTORY -> R.drawable.ic_subject_history
            Subject.LIFE_ORIENTATION -> R.drawable.ic_subject_life_orientation
            Subject.LIFE_SCIENCES -> R.drawable.ic_subject_life_science
            Subject.MATHEMATICS -> R.drawable.ic_subject_mathematics
            Subject.MATHEMATICS_LITERACY -> R.drawable.ic_subject_mathematics_literacy
            Subject.PHYSICAL_SCIENCES -> R.drawable.ic_subject_physical_science
        }
    }
}

