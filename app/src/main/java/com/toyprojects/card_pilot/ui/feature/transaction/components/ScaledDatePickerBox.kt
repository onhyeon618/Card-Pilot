package com.toyprojects.card_pilot.ui.feature.transaction.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScaledDatePickerBox(
    modifier: Modifier = Modifier,
    targetWidthDp: Dp = 360.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val datePicker = measurables.first()

        val targetWidth = targetWidthDp.roundToPx()
        val placeable = datePicker.measure(
            constraints.copy(
                minWidth = targetWidth,
                maxWidth = targetWidth
            )
        )

        val scale = if (constraints.maxWidth < placeable.width) {
            constraints.maxWidth.toFloat() / placeable.width.toFloat()
        } else {
            1f
        }

        val scaledWidth = (placeable.width * scale).toInt()
        val scaledHeight = (placeable.height * scale).toInt()

        layout(scaledWidth, scaledHeight) {
            placeable.placeWithLayer(0, 0) {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0f, 0f)
            }
        }
    }
}

@Preview(widthDp = 300)
@Composable
private fun ScaledDatePickerBoxPreview() {
    ScaledDatePickerBox(targetWidthDp = 360.dp) {
        Box(
            modifier = Modifier
                .size(width = 360.dp, height = 400.dp)
                .background(Color.LightGray)
        )
    }
}
