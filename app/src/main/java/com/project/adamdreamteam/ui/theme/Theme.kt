package com.project.adamdreamteam.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ---------- Color schemes (use your brand) ----------
private val LightColors = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    secondary = BrandOrange,
    onSecondary = Color.White,
    tertiary = BrandBlueLight,

    background = BrandBackgroundLight,
    surface = BrandSurfaceLight,
    onBackground = Color(0xFF0F172A), // dark slate text
    onSurface = Color(0xFF111827)
)

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small      = RoundedCornerShape(10.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

private val DarkColors = darkColorScheme(
    primary = BrandBlueLight,
    onPrimary = Color.Black,
    secondary = BrandOrange,
    onSecondary = Color.Black,
    tertiary = BrandBlue,

    background = BrandBackgroundDark,
    surface = BrandSurfaceDark,
    onBackground = Color(0xFFE5E7EB),
    onSurface = Color(0xFFF3F4F6)
)

// ---------- Brand palette available anywhere ----------
@Stable
data class BrandPalette(
    val gradient: List<Color>
)

val LocalBrand = staticCompositionLocalOf {
    BrandPalette(
        gradient = listOf(BrandBlue, BrandBlueLight, BrandOrange)
    )
}

// ---------- Theme ----------
@Composable
fun AdamDreamTeamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val brand = BrandPalette(
        gradient = listOf(BrandBlue, BrandBlueLight, BrandOrange)
    )

    CompositionLocalProvider(LocalBrand provides brand) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography, // keep if you have Typography.kt
            shapes = AppShapes,      // <-- use the instance we just defined
            content = content
        )
    }
}
