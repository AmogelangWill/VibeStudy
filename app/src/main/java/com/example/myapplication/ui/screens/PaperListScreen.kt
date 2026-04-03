package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.ExamMonth
import com.example.myapplication.data.Grade
import com.example.myapplication.data.Subject
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.components.RectangularButton
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.CardBackground
import com.example.myapplication.ui.theme.TextColor

@Composable
fun PaperListScreen(
    grade: Grade,
    subject: Subject,
    year: Int,
    month: ExamMonth,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(20.dp)
    ) {
        Text(
            text = subject.displayName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "${grade.displayName} • $year • ${month.displayName}",
            fontSize = 14.sp,
            color = TextColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // English FAL has 3 papers, others have 2
            val paperCount = if (subject == Subject.ENGLISH_FAL) 3 else 2

            items((1..paperCount).toList()) { paperNumber ->
                // Check if we have a manifest for this paper
                val manifestName = buildManifestName(grade, subject, year, month, paperNumber)

                RectangularButton(
                    text = "Paper $paperNumber",
                    onClick = {
                        if (manifestName != null) {
                            navController.navigate(Screen.PaperView.createRoute(manifestName))
                        }
                    },
                    backgroundColor = CardBackground
                )
            }
        }
    }
}

private fun buildManifestName(
    grade: Grade,
    subject: Subject,
    year: Int,
    month: ExamMonth,
    paperNumber: Int
): String? {
    // Only Grade 12, Mathematics and Physical Sciences, 2024 November P1 are available
    if (grade != Grade.GRADE_12 || year != 2024 || month != ExamMonth.NOVEMBER || paperNumber != 1) {
        return null
    }

    return when (subject) {
        Subject.MATHEMATICS -> "Grade_12_Mathematics_2024_November_P1.json"
        Subject.PHYSICAL_SCIENCES -> "Grade_12_Physical_Sciences_2024_November_P1.json"
        else -> null
    }
}

