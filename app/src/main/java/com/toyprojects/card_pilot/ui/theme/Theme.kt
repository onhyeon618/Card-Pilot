package com.toyprojects.card_pilot.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val cardPilotColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Surface,        // Text on Primary (e.g. Buttons) should be White/Surface
    primaryContainer = Surface, // For containers using primary color
    onPrimaryContainer = Primary,

    secondary = Secondary,
    onSecondary = Surface,
    secondaryContainer = Gray50,
    onSecondaryContainer = TextPrimary,

    tertiary = CTA,
    onTertiary = Surface,

    background = Background,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = Gray50,
    onSurfaceVariant = TextSecondary,

    error = Error,
    onError = Surface,

    outline = Outline,
)


@Composable
fun CardPilotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = cardPilotColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}