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
import com.example.myapplication.data.Grade
import com.example.myapplication.data.Subject
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.components.RectangularButton
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.CardBackground
import com.example.myapplication.ui.theme.TextColor

@Composable
fun YearListScreen(
    grade: Grade,
    subject: Subject,
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
            text = grade.displayName,
            fontSize = 14.sp,
            color = TextColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Years from 2015 to 2025
            items((2015..2025).toList().reversed()) { year ->
                RectangularButton(
                    text = year.toString(),
                    onClick = {
                        navController.navigate(Screen.MonthList.createRoute(grade, subject, year))
                    },
                    backgroundColor = CardBackground
                )
            }
        }
    }
}

