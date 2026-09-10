package com.toyprojects.card_pilot.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun GlassAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    description: String,
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
    isDestructive: Boolean = false
) {
    GlassDialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CardPilotColors.textPrimary
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CardPilotColors.textSecondary
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (dismissText != null && onDismiss != null) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardPilotColors.surfaceGlassButton)
                            .border(1.dp, CardPilotColors.outlineButton, RoundedCornerShape(16.dp))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dismissText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = CardPilotColors.textPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }

                val confirmBgColor =
                    if (isDestructive) CardPilotColors.error.copy(alpha = 0.1f) else CardPilotColors.cta
                val confirmTextColor = if (isDestructive) CardPilotColors.error else CardPilotColors.white

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(confirmBgColor)
                        .then(
                            if (isDestructive) Modifier.border(
                                1.dp,
                                CardPilotColors.error.copy(alpha = 0.3f),
                                RoundedCornerShape(16.dp)
                            )
                            else Modifier
                        )
                        .clickable { onConfirm() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = confirmText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = confirmTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GlassAlertDialogPreview() {
    CardPilotTheme {
        GlassAlertDialog(
            onDismissRequest = {},
            title = "저장하지 않고 나가시겠어요?",
            description = "수정한 내용이 저장되지 않습니다.",
            confirmText = "나가기",
            onConfirm = {},
            dismissText = "취소",
            onDismiss = {},
            isDestructive = true
        )
    }
}
