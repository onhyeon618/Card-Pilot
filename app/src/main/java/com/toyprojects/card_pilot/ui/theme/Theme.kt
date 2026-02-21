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
    primary = CardPilotColors.Primary,
    onPrimary = CardPilotColors.Surface,        // Text on Primary (e.g. Buttons) should be White/Surface
    primaryContainer = CardPilotColors.Surface, // For containers using primary color
    onPrimaryContainer = CardPilotColors.Primary,

    secondary = CardPilotColors.Secondary,
    onSecondary = CardPilotColors.Surface,
    secondaryContainer = CardPilotColors.Gray50,
    onSecondaryContainer = CardPilotColors.TextPrimary,

    tertiary = CardPilotColors.CTA,
    onTertiary = CardPilotColors.Surface,

    background = CardPilotColors.Background,
    onBackground = CardPilotColors.TextPrimary,

    surface = CardPilotColors.Surface,
    onSurface = CardPilotColors.TextPrimary,
    surfaceVariant = CardPilotColors.Gray50,
    onSurfaceVariant = CardPilotColors.TextSecondary,

    error = CardPilotColors.Error,
    onError = CardPilotColors.Surface,

    outline = CardPilotColors.Outline,
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
            window.statusBarColor = CardPilotColors.GradientBlue.toArgb()
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