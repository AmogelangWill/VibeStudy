package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.Grade
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.components.SquareButton
import com.example.myapplication.ui.theme.AppBackground

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // First row with Grade 10 and Grade 11
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SquareButton(
                text = "Grade 10",
                onClick = {
                    navController.navigate(Screen.SubjectList.createRoute(Grade.GRADE_10))
                },
                modifier = Modifier.weight(1f),
                iconResId = R.drawable.ic_grade
            )

            SquareButton(
                text = "Grade 11",
                onClick = {
                    navController.navigate(Screen.SubjectList.createRoute(Grade.GRADE_11))
                },
                modifier = Modifier.weight(1f),
                iconResId = R.drawable.ic_grade
            )
        }

        // Second row with Grade 12 and MyStudySchedule
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SquareButton(
                text = "Grade 12",
                onClick = {
                    navController.navigate(Screen.SubjectList.createRoute(Grade.GRADE_12))
                },
                modifier = Modifier.weight(1f),
                iconResId = R.drawable.ic_grade
            )

            SquareButton(
                text = "My Study Schedule",
                onClick = {
                    navController.navigate(Screen.MyStudySchedule.route)
                },
                modifier = Modifier.weight(1f),
                iconResId = R.drawable.ic_schedule
            )
        }

        // Third row with Library
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SquareButton(
                text = "Library",
                onClick = {
                    navController.navigate(Screen.LibrarySubjects.route)
                },
                modifier = Modifier.weight(1f),
                iconResId = R.drawable.ic_library
            )

            // Empty space to balance the layout
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


