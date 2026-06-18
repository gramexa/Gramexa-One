package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkMinimalPrimary,
    secondary = DarkMinimalSecondary,
    tertiary = DarkMinimalPrimary,
    background = DarkMinimalBg,
    surface = DarkMinimalSurface,
    onPrimary = DarkMinimalBg,
    onSecondary = DarkMinimalBg,
    onTertiary = DarkMinimalBg,
    onBackground = MinimalOffWhiteBg,
    onSurface = MinimalOffWhiteBg,
    surfaceVariant = DarkMinimalBorder,
    onSurfaceVariant = MinimalOffWhiteBg
)

private val LightColorScheme = lightColorScheme(
    primary = MinimalBluePrimary,
    secondary = MinimalLavenderSecondary,
    tertiary = MinimalDarkNavy,
    background = MinimalOffWhiteBg,
    surface = PureWhiteSurface,
    onPrimary = PureWhiteSurface,
    onSecondary = PureWhiteSurface,
    onTertiary = PureWhiteSurface,
    onBackground = TextDarkGray,
    onSurface = TextDarkGray,
    surfaceVariant = BorderLightGray,
    onSurfaceVariant = TextMediumGray
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep rural theme signature consistent
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
