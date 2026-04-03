package com.example.myapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val VibeStudyColorScheme = lightColorScheme(
    primary = PrimaryButton,
    secondary = SecondaryButton,
    tertiary = SecondaryButton,
    background = AppBackground,
    surface = CardBackground,
    onPrimary = AppBackground,
    onSecondary = AppBackground,
    onTertiary = AppBackground,
    onBackground = TextColor,
    onSurface = TextColor,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Force light theme for Vibe Study
    dynamicColor: Boolean = false, // Disable dynamic color to use our custom palette
    content: @Composable () -> Unit
) {
    val colorScheme = VibeStudyColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}