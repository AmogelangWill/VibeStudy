package com.example.myapplication.data

enum class Subject(val displayName: String) {
    ACCOUNTING("Accounting"),
    BUSINESS("Business"),
    ECONOMICS("Economics"),
    ENGLISH_FAL("English FAL"),
    GEOGRAPHY("Geography"),
    HISTORY("History"),
    LIFE_ORIENTATION("Life Orientation"),
    LIFE_SCIENCES("Life Sciences"),
    MATHEMATICS("Mathematics"),
    MATHEMATICS_LITERACY("Mathematics Literacy"),
    PHYSICAL_SCIENCES("Physical Sciences");

    companion object {
        fun getAllSorted(): List<Subject> = values().sortedBy { it.displayName }
    }
}

data class ExamPaper(
    val subject: Subject,
    val year: Int,
    val month: ExamMonth,
    val paperNumber: Int
)

enum class ExamMonth(val displayName: String) {
    MARCH("March"),
    JUNE("June"),
    SEPTEMBER("September"),
    NOVEMBER("November")
}

enum class Grade(val displayName: String) {
    GRADE_10("Grade 10"),
    GRADE_11("Grade 11"),
    GRADE_12("Grade 12")
}

