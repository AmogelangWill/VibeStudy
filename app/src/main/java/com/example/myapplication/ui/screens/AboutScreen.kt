package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.PrimaryButton
import com.example.myapplication.ui.theme.SecondaryButton
import com.example.myapplication.ui.theme.TextColor
import com.example.myapplication.ui.theme.CardBackground

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Vibe Study Logo",
            modifier = Modifier
                .height(120.dp)
                .padding(bottom = 16.dp)
        )

        // App Name
        Text(
            text = "Vibe Study",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor
        )

        Text(
            text = "Version 1.0.0",
            fontSize = 14.sp,
            color = TextColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Mission Card
        InfoCard(
            title = "Our Mission",
            description = "Empowering South African students in Grades 10-12 to achieve academic excellence through easy access to past exam papers, study notes, and smart scheduling tools.",
            backgroundColor = Color(0xFFFFF3E0) // Soft peach
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Features Card
        InfoCard(
            title = "Features",
            description = "• Past exam papers from 2015-2025\n• Comprehensive study library\n• Smart study scheduler\n• Subject-specific resources\n• Video tutorials",
            backgroundColor = Color(0xFFE3F2FD) // Soft blue
        )

        Spacer(modifier = Modifier.height(12.dp))

        // For Students Card
        InfoCard(
            title = "Made for Students",
            description = "Designed specifically for South African curriculum (DBE), covering Mathematics, Sciences, Languages, and all major subjects for Grades 10, 11, and 12.",
            backgroundColor = Color(0xFFF3E5F5) // Soft purple
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Footer
        Text(
            text = "Study smart, succeed faster 📚",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PrimaryButton,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "© 2025 Vibe Study\nAll rights reserved",
            fontSize = 12.sp,
            color = TextColor.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun InfoCard(
    title: String,
    description: String,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = description,
                fontSize = 14.sp,
                color = TextColor.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}

