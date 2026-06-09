package com.example.xspoof.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1f6feb),
    secondary = Color(0xFF6c757d),
    tertiary = Color(0xFF0969da),
    background = Color(0xFFFAFBFC),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF24292F),
    onSurface = Color(0xFF24292F),
    primaryContainer = Color(0xFFDDF4FF),
    onPrimaryContainer = Color(0xFF001D36),
    surfaceVariant = Color(0xFFEAEEF2)
)

@Composable
fun XSpoofTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
