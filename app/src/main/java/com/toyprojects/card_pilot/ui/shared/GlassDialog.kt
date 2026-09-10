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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun GlassDialog(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
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
                    spotColor = CardPilotColors.primary.copy(alpha = 0.15f)
                )
                .background(
                    color = lerp(CardPilotColors.pastelStart, CardPilotColors.background, 0.85f),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = CardPilotColors.white.copy(alpha = 0.8f),
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
private fun GlassDialogPreview() {
    CardPilotTheme {
        GlassDialog(
            onDismissRequest = {}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Glass Dialog Preview", color = CardPilotColors.textPrimary)
            }
        }
    }
}
