package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.toyprojects.card_pilot.ui.theme.Background
import com.toyprojects.card_pilot.ui.theme.GradientBlue
import com.toyprojects.card_pilot.ui.theme.GradientPeach
import com.toyprojects.card_pilot.ui.theme.GradientPurple

@Composable
fun GlassScaffold(
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background) // Fallback base color
    ) {
        // Warm Mesh Gradient Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GradientBlue.copy(alpha = 0.8f),
                            GradientPurple.copy(alpha = 0.6f),
                            GradientPeach.copy(alpha = 0.5f),
                            Background
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
    }
}
