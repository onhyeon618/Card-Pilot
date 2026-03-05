package com.toyprojects.card_pilot.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.toyprojects.card_pilot.model.ThemeType

val ThemeType.palette: CardPilotColorPalette
    get() = when (this) {
        ThemeType.PURPLE -> PurplePalette
        ThemeType.BLUE -> BluePalette
        ThemeType.YELLOW -> YellowPalette
        ThemeType.DARK -> DarkPalette
    }

private fun lightSchemeFrom(palette: CardPilotColorPalette) = lightColorScheme(
    primary = palette.primary,
    onPrimary = palette.surface,
    primaryContainer = palette.surface,
    onPrimaryContainer = palette.primary,

    secondary = palette.secondary,
    onSecondary = palette.surface,
    secondaryContainer = palette.gray50,
    onSecondaryContainer = palette.textPrimary,

    tertiary = palette.cta,
    onTertiary = palette.surface,

    background = palette.background,
    onBackground = palette.textPrimary,

    surface = palette.surface,
    onSurface = palette.textPrimary,
    surfaceVariant = palette.gray50,
    onSurfaceVariant = palette.textSecondary,

    error = palette.error,
    onError = palette.surface,

    outline = palette.outline,
)

private fun darkSchemeFrom(palette: CardPilotColorPalette) = darkColorScheme(
    primary = palette.primary,
    onPrimary = palette.surface,
    primaryContainer = palette.surface,
    onPrimaryContainer = palette.primary,

    secondary = palette.secondary,
    onSecondary = palette.surface,
    secondaryContainer = palette.gray50,
    onSecondaryContainer = palette.textPrimary,

    tertiary = palette.cta,
    onTertiary = palette.surface,

    background = palette.background,
    onBackground = palette.textPrimary,

    surface = palette.surface,
    onSurface = palette.textPrimary,
    surfaceVariant = palette.gray50,
    onSurfaceVariant = palette.textSecondary,

    error = palette.error,
    onError = palette.surface,

    outline = palette.outline,
)

@Composable
fun CardPilotTheme(
    themeType: ThemeType = ThemeType.PURPLE,
    content: @Composable () -> Unit
) {
    val palette = themeType.palette

    val isDarkTheme = themeType == ThemeType.DARK

    val colorScheme = if (isDarkTheme) {
        darkSchemeFrom(palette)
    } else {
        lightSchemeFrom(palette)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDarkTheme
            insetsController.isAppearanceLightNavigationBars = !isDarkTheme
        }
    }

    CompositionLocalProvider(LocalCardPilotColors provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
