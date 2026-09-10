package com.toyprojects.card_pilot.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun GradientDialog(
    onDismissRequest: () -> Unit,
    gradientHeight: androidx.compose.ui.unit.Dp? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val density = LocalDensity.current
    val endY = gradientHeight?.let { with(density) { it.toPx() } } ?: Float.POSITIVE_INFINITY

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = CardPilotColors.cta.copy(alpha = 0.2f)
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CardPilotColors.pastelStart,
                            CardPilotColors.surface
                        ),
                        endY = endY
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = CardPilotColors.white.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                content = content
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GradientDialogPreview() {
    CardPilotTheme {
        GradientDialog(
            onDismissRequest = {},
            gradientHeight = 120.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(24.dp)
            ) {
                Text("Dialog Content", color = CardPilotColors.textPrimary)
            }
        }
    }
}
