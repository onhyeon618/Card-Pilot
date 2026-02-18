package com.toyprojects.card_pilot.ui.theme

import androidx.compose.ui.graphics.Color

// Glassmorphism Palette - Modern, Professional, Friendly

// Base Colors - Warm Stone & Indigo Palette
val Primary = Color(0xFF1C1917)    // Stone-900: Warm, deep charcoal
val Secondary = Color(0xFF78716C)  // Stone-500: Warm grey
val CTA = Color(0xFF6366F1)        // Indigo-500: Friendly, vibrant
val Background = Color(0xFFFAFAF9) // Stone-50: Warm off-white
val Surface = Color(0xFFFFFFFF)    // Pure White

// Text Colors
val TextPrimary = Color(0xFF0C0A09)   // Stone-950
val TextSecondary = Color(0xFF57534E) // Stone-600

// UI Elements
val Outline = Color(0xFFFFFFFF).copy(alpha = 0.5f)    // Glass Border: Semi-transparent white
val OutlineHigh = Color(0xFFFFFFFF).copy(alpha = 0.65f)    // Glass Border: Semi-transparent white
val SurfaceGlass = Color(0xFFFFFFFF).copy(alpha = 0.4f) // 40% opacity for strong glass effect
val SurfaceGlassHigh = Color(0xFFFFFFFF).copy(alpha = 0.55f) // 40% opacity for strong glass effect

// Semantic Colors
val Success = Color(0xFF10B981) // Emerald-500
val Warning = Color(0xFFF59E0B) // Amber-500
val Error = Color(0xFFEF4444)   // Red-500

// Gradients/Accents (Warm Mesh)
val GradientBlue = Color(0xFFE0E7FF)   // Indigo-100
val GradientPurple = Color(0xFFFAE8FF) // Fuchsia-100
val GradientPeach = Color(0xFFFFEDD5)  // Orange-100

val Gray50 = Color(0xFFFAFAF9)       // Stone-50 (Warm) - Matches Background
val Gray100 = Color(0xFFF5F5F4)      // Stone-100
val Gray200 = Color(0xFFE7E5E4)      // Stone-200
val Gray300 = Color(0xFFD6D3D1)      // Stone-300
val SurfaceCard = Color.White.copy(alpha = 0.75f) // High transparency for cards
val White = Color.White              // Alias for convenience

// Shared Gradients
val PastelViolet = Color(0xFFE9D5FF)
val PastelIndigo = Color(0xFFC7D2FE)
val PastelGradientColors = listOf(PastelViolet, PastelIndigo)

// Gentle Accents
val SoftSlateIndigo = Color(0xFF5B507A)

// High Contrast for Pastel backgrounds
val Violet800 = Color(0xFF5B21B6)
val Violet900 = Color(0xFF4C1D95)