package com.toyprojects.card_pilot.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

@Composable
fun GlassScaffold(
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CardPilotColors.Background) // Fallback base color
    ) {
        // Warm Mesh Gradient Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CardPilotColors.GradientBlue.copy(alpha = 0.8f),
                            CardPilotColors.GradientPurple.copy(alpha = 0.6f),
                            CardPilotColors.GradientPeach.copy(alpha = 0.5f),
                            CardPilotColors.Background
                        ),
                        startY = 0f,
                        endY = 1800f
                    )
                )
        )

        // Use standard Scaffold but make it transparent to show gradient
        Scaffold(
            topBar = topBar,
            floatingActionButton = floatingActionButton,
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.systemBars
        ) { paddingValues ->
            content(paddingValues)
        }

        // Draw explicit status bar background to override edge-to-edge transparency
        // Placed after Scaffold in Z-order so it hides content scrolling underneath
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .background(
                    lerp(
                        CardPilotColors.White,
                        CardPilotColors.GradientBlue,
                        0.8f
                    )
                )
        )
    }
}
