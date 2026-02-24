package com.toyprojects.card_pilot.ui.shared

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

/// Wrapper that applies a custom ripple color to all clickable children
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPilotRipple(
    color: Color = CardPilotColors.PastelViolet,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(color = color)
    ) {
        content()
    }
}
