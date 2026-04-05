package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.myapplication.model.Question

@Composable
fun QuestionCard(
    question: Question,
    onAskTutor: (Question) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMemo by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Question screenshot image - fills width
            Image(
                painter = painterResource(id = question.imageRes),
                contentDescription = "Question ${question.id}",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Row with two equal-width buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onAskTutor(question) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(text = "Ask Tutor")
                }

                Button(
                    onClick = { showMemo = !showMemo },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(text = if (showMemo) "Hide Memo" else "View Memo")
                }
            }

            // Memo screenshot toggled below, same width as question
            if (showMemo) {
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    painter = painterResource(id = question.memoRes),
                    contentDescription = "Memo ${question.id}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

