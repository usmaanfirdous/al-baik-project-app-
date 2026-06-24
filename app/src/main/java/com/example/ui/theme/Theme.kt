package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryCrimson,
    onPrimary = Color.White,
    primaryContainer = PrimaryCrimson,
    onPrimaryContainer = Color.White,
    secondary = SecondaryGold,
    onSecondary = Color(0xFF3C2F00),
    secondaryContainer = SecondaryGoldContainer,
    onSecondaryContainer = OnSurfaceLight,
    background = BaseBackground,
    onBackground = OnSurfaceLight,
    surface = SurfaceLow,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceMedium,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineColor,
    outlineVariant = OutlineVariantColor
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
