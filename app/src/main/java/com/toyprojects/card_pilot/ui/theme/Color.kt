package com.toyprojects.card_pilot.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class CardPilotColorPalette(
    // Base Colors
    val primary: Color,
    val secondary: Color,
    val cta: Color,
    val background: Color,
    val surface: Color,

    // Text Colors
    val textPrimary: Color,
    val textSecondary: Color,

    // UI Elements
    val outline: Color,
    val outlineInput: Color,
    val outlineButton: Color,
    val surfaceGlass: Color,
    val surfaceGlassInput: Color,
    val surfaceGlassButton: Color,

    // Semantic Colors
    val success: Color,
    val warning: Color,
    val error: Color,

    // Gradients/Accents
    val gradientStart: Color,
    val gradientMiddle: Color,
    val gradientEnd: Color,

    // Grays
    val gray50: Color,
    val gray100: Color,
    val gray200: Color,
    val gray300: Color,
    val surfaceCard: Color,
    val white: Color,

    // Shared Gradients (for card placeholders, etc.)
    val pastelStart: Color,
    val pastelEnd: Color,

    // Gentle Accents
    val softAccent: Color,

    // High Contrast for Pastel backgrounds
    val accent500: Color,
    val accent800: Color,
    val accent900: Color,
) {
    val backgroundGradientColors: List<Color>
        get() = listOf(gradientStart, gradientMiddle, gradientEnd)

    val pastelGradientColors: List<Color>
        get() = listOf(pastelStart, pastelEnd)
}

val PurplePalette = CardPilotColorPalette(
    // Base Colors - Warm Stone & Indigo Palette
    primary = Color(0xFF1C1917),    // Stone-900
    secondary = Color(0xFF78716C),  // Stone-500
    cta = Color(0xFF6366F1),        // Indigo-500
    background = Color(0xFFFAFAF9), // Stone-50
    surface = Color(0xFFFFFFFF),

    // Text Colors
    textPrimary = Color(0xFF0C0A09),   // Stone-950
    textSecondary = Color(0xFF57534E), // Stone-600

    // UI Elements
    outline = Color(0xFFFFFFFF).copy(alpha = 0.5f),
    outlineInput = Color(0xFFFFFFFF).copy(alpha = 0.7f),
    outlineButton = Color(0xFFFFFFFF).copy(alpha = 0.9f),
    surfaceGlass = Color(0xFFFFFFFF).copy(alpha = 0.4f),
    surfaceGlassInput = Color(0xFFFFFFFF).copy(alpha = 0.6f),
    surfaceGlassButton = Color(0xFFFFFFFF).copy(alpha = 0.75f),

    // Semantic Colors
    success = Color(0xFF10B981),
    warning = Color(0xFFF59E0B),
    error = Color(0xFFEF4444),

    // Gradients/Accents (Warm Mesh)
    gradientStart = Color(0xFFE0E7FF),   // Indigo-100
    gradientMiddle = Color(0xFFFAE8FF),  // Fuchsia-100
    gradientEnd = Color(0xFFFFEDD5),     // Orange-100

    // Grays
    gray50 = Color(0xFFFAFAF9),
    gray100 = Color(0xFFF5F5F4),
    gray200 = Color(0xFFE7E5E4),
    gray300 = Color(0xFFD6D3D1),
    surfaceCard = Color.White.copy(alpha = 0.75f),
    white = Color.White,

    // Shared Gradients
    pastelStart = Color(0xFFE9D5FF),  // PastelViolet
    pastelEnd = Color(0xFFC7D2FE),    // PastelIndigo

    // Gentle Accents
    softAccent = Color(0xFF5B507A),   // SoftSlateIndigo

    // High Contrast for Pastel backgrounds
    accent500 = Color(0xFF8B5CF6),    // Violet-500
    accent800 = Color(0xFF5B21B6),    // Violet-800
    accent900 = Color(0xFF4C1D95),    // Violet-900
)

val BluePalette = CardPilotColorPalette(
    // Base Colors - Cool Slate & Blue Palette
    primary = Color(0xFF0F172A),    // Slate-900
    secondary = Color(0xFF64748B),  // Slate-500
    cta = Color(0xFF3B82F6),        // Blue-500
    background = Color(0xFFF8FAFC), // Slate-50
    surface = Color(0xFFFFFFFF),

    // Text Colors
    textPrimary = Color(0xFF020617),   // Slate-950
    textSecondary = Color(0xFF475569), // Slate-600

    // UI Elements
    outline = Color(0xFFFFFFFF).copy(alpha = 0.5f),
    outlineInput = Color(0xFFFFFFFF).copy(alpha = 0.7f),
    outlineButton = Color(0xFFFFFFFF).copy(alpha = 0.9f),
    surfaceGlass = Color(0xFFFFFFFF).copy(alpha = 0.4f),
    surfaceGlassInput = Color(0xFFFFFFFF).copy(alpha = 0.6f),
    surfaceGlassButton = Color(0xFFFFFFFF).copy(alpha = 0.75f),

    // Semantic Colors
    success = Color(0xFF10B981),
    warning = Color(0xFFF59E0B),
    error = Color(0xFFEF4444),

    // Gradients/Accents (Cool Mesh)
    gradientStart = Color(0xFFDBEAFE),   // Blue-100
    gradientMiddle = Color(0xFFE0E7FF),  // Indigo-100
    gradientEnd = Color(0xFFCFFAFE),     // Cyan-100

    // Grays
    gray50 = Color(0xFFF8FAFC),
    gray100 = Color(0xFFF1F5F9),
    gray200 = Color(0xFFE2E8F0),
    gray300 = Color(0xFFCBD5E1),
    surfaceCard = Color.White.copy(alpha = 0.75f),
    white = Color.White,

    // Shared Gradients
    pastelStart = Color(0xFFBFDBFE),  // Blue-200
    pastelEnd = Color(0xFFC7D2FE),    // Indigo-200

    // Gentle Accents
    softAccent = Color(0xFF3B5998),

    // High Contrast for Pastel backgrounds
    accent500 = Color(0xFF3B82F6),    // Blue-500
    accent800 = Color(0xFF1E40AF),    // Blue-800
    accent900 = Color(0xFF1E3A8A),    // Blue-900
)

val YellowPalette = CardPilotColorPalette(
    // Base Colors - Warm Amber & Gold Palette
    primary = Color(0xFF1C1917),    // Stone-900
    secondary = Color(0xFF78716C),  // Stone-500
    cta = Color(0xFFD97706),        // Amber-600
    background = Color(0xFFFFFBEB), // Amber-50
    surface = Color(0xFFFFFFFF),

    // Text Colors
    textPrimary = Color(0xFF0C0A09),   // Stone-950
    textSecondary = Color(0xFF57534E), // Stone-600

    // UI Elements
    outline = Color(0xFFFFFFFF).copy(alpha = 0.5f),
    outlineInput = Color(0xFFFFFFFF).copy(alpha = 0.7f),
    outlineButton = Color(0xFFFFFFFF).copy(alpha = 0.9f),
    surfaceGlass = Color(0xFFFFFFFF).copy(alpha = 0.4f),
    surfaceGlassInput = Color(0xFFFFFFFF).copy(alpha = 0.6f),
    surfaceGlassButton = Color(0xFFFFFFFF).copy(alpha = 0.75f),

    // Semantic Colors
    success = Color(0xFF10B981),
    warning = Color(0xFFF59E0B),
    error = Color(0xFFEF4444),

    // Gradients/Accents (Warm Gold Mesh)
    gradientStart = Color(0xFFFEF3C7),   // Amber-100
    gradientMiddle = Color(0xFFFDE68A),  // Amber-200
    gradientEnd = Color(0xFFFFEDD5),     // Orange-100

    // Grays
    gray50 = Color(0xFFFFFBEB),
    gray100 = Color(0xFFFEF3C7),
    gray200 = Color(0xFFE7E5E4),
    gray300 = Color(0xFFD6D3D1),
    surfaceCard = Color.White.copy(alpha = 0.75f),
    white = Color.White,

    // Shared Gradients
    pastelStart = Color(0xFFFDE68A),  // Amber-200
    pastelEnd = Color(0xFFFED7AA),    // Orange-200

    // Gentle Accents
    softAccent = Color(0xFF92400E),   // Amber-800

    // High Contrast for Pastel backgrounds
    accent500 = Color(0xFFD97706),    // Amber-600
    accent800 = Color(0xFF92400E),    // Amber-800
    accent900 = Color(0xFF78350F),    // Amber-900
)

val DarkPalette = CardPilotColorPalette(
    // Base Colors - Dark Mode
    primary = Color(0xFFE2E8F0),    // Slate-200 (light text on dark)
    secondary = Color(0xFF94A3B8),  // Slate-400
    cta = Color(0xFF818CF8),        // Indigo-400
    background = Color(0xFF0F172A), // Slate-900
    surface = Color(0xFF1E293B),    // Slate-800

    // Text Colors
    textPrimary = Color(0xFFF1F5F9),   // Slate-100
    textSecondary = Color(0xFF94A3B8), // Slate-400

    // UI Elements
    outline = Color(0xFFFFFFFF).copy(alpha = 0.12f),
    outlineInput = Color(0xFFFFFFFF).copy(alpha = 0.2f),
    outlineButton = Color(0xFFFFFFFF).copy(alpha = 0.3f),
    surfaceGlass = Color(0xFFFFFFFF).copy(alpha = 0.08f),
    surfaceGlassInput = Color(0xFFFFFFFF).copy(alpha = 0.12f),
    surfaceGlassButton = Color(0xFFFFFFFF).copy(alpha = 0.18f),

    // Semantic Colors
    success = Color(0xFF34D399),  // Emerald-400
    warning = Color(0xFFFBBF24),  // Amber-400
    error = Color(0xFFF87171),    // Red-400

    // Gradients/Accents (Dark Mesh)
    gradientStart = Color(0xFF1E293B),   // Slate-800
    gradientMiddle = Color(0xFF1E1B4B),  // Indigo-950
    gradientEnd = Color(0xFF172554),     // Blue-950

    // Grays
    gray50 = Color(0xFF1E293B),   // Slate-800 (inverted role)
    gray100 = Color(0xFF334155),  // Slate-700
    gray200 = Color(0xFF475569),  // Slate-600
    gray300 = Color(0xFF64748B),  // Slate-500
    surfaceCard = Color(0xFF1E293B).copy(alpha = 0.9f),
    white = Color(0xFFF1F5F9),    // Slate-100 (acts as "light" in dark mode)

    // Shared Gradients
    pastelStart = Color(0xFF312E81),  // Indigo-900
    pastelEnd = Color(0xFF1E3A8A),   // Blue-900

    // Gentle Accents
    softAccent = Color(0xFFA5B4FC),   // Indigo-300

    // High Contrast for Dark backgrounds
    accent500 = Color(0xFF818CF8),    // Indigo-400
    accent800 = Color(0xFFA5B4FC),    // Indigo-300
    accent900 = Color(0xFFC7D2FE),    // Indigo-200
)

val LocalCardPilotColors = compositionLocalOf { PurplePalette }

/// Global accessor to get the current theme's color palette via CompositionLocal
val CardPilotColors: CardPilotColorPalette
    @Composable
    get() = LocalCardPilotColors.current
