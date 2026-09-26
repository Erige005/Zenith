package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CyanNeon,
    onPrimary = Color(0xFF03223A),
    primaryContainer = Color(0xFF0C4A6E),
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = IndigoNeon,
    onSecondary = Color(0xFF1E1B4B),
    secondaryContainer = Color(0xFF312E81),
    onSecondaryContainer = Color(0xFFE0E7FF),
    tertiary = VioletNeon,
    onTertiary = Color(0xFF3B0764),
    tertiaryContainer = Color(0xFF581C87),
    onTertiaryContainer = Color(0xFFF3E8FF),
    background = ZenBgDark,
    onBackground = ZenTextPrimaryDark,
    surface = ZenSurfaceDark,
    onSurface = ZenTextPrimaryDark,
    surfaceVariant = ZenSurfaceVariantDark,
    onSurfaceVariant = ZenTextSecondaryDark,
    outline = ZenSurfaceElevatedDark
)

private val LightColorScheme = lightColorScheme(
    primary = CyanGlow,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = IndigoNeon,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEF2FF),
    onSecondaryContainer = Color(0xFF3730A3),
    tertiary = VioletNeon,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFAF5FF),
    onTertiaryContainer = Color(0xFF6B21A8),
    background = ZenBgLight,
    onBackground = ZenTextPrimaryLight,
    surface = ZenSurfaceLight,
    onSurface = ZenTextPrimaryLight,
    surfaceVariant = ZenSurfaceVariantLight,
    onSurfaceVariant = ZenTextSecondaryLight,
    outline = ZenSurfaceElevatedLight
)

@Composable
fun ZenithTheme(
    darkTheme: Boolean = true, // Default to sleek atmospheric dark theme for arcade aesthetics
    dynamicColor: Boolean = false, // Preserve crafted minimalist neon aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
