package com.example.handyman.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HandymanColorScheme = lightColorScheme(
    primary = Color(0xFFFF6B00),   // main orange
    secondary = Color(0xFFFFA04D),
    onPrimary = Color.White,
    background = Color(0xFFF4F5F7),
    surface = Color.White
)

@Composable
fun HandymanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HandymanColorScheme,
        content = content
    )
}
