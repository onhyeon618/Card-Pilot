package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun BenefitItemRow(
    name: String,
    description: String? = null,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    CardPilotRipple(color = CardPilotColors.GradientPeach) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = CardPilotColors.SurfaceGlassButton,
                contentColor = CardPilotColors.TextPrimary
            ),
            border = BorderStroke(1.dp, CardPilotColors.OutlineButton),
            contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 8.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                /// Drag Handle
                // TODO: make icon actually work as handle
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "끌어서 순서 바꾸기",
                    tint = CardPilotColors.Secondary.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (name.isBlank()) "혜택 이름 없음" else name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = CardPilotColors.TextPrimary
                    )

                    if (!description.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CardPilotColors.Secondary
                        )
                    }
                }

                CardPilotRipple {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = DeleteThin,
                            contentDescription = "삭제",
                            tint = CardPilotColors.Error
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BenefitItemRowPreview() {
    CardPilotTheme {
        BenefitItemRow(
            name = "여행 (Travel)",
            description = "여행 관련 혜택",
            onClick = {},
            onDelete = {}
        )
    }
}
