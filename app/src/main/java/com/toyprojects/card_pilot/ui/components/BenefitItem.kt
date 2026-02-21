package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun BenefitItem(
    benefit: Benefit,
    onClick: () -> Unit = {}
) {
    val progress = (benefit.used / benefit.total).toFloat().coerceIn(0f, 1f)

    val usedAmount = "%,.0f".format(benefit.used)
    val totalAmount = "%,.0f".format(benefit.total)

    CardPilotRipple(color = CardPilotColors.GradientPeach) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 24.dp)
                .padding(vertical = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /// Benefit name
                Text(
                    text = benefit.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = CardPilotColors.TextPrimary
                )
                /// Usage per benefit
                Text(
                    text = "$usedAmount / $totalAmount",
                    style = MaterialTheme.typography.labelMedium,
                    color = CardPilotColors.Secondary
                )
            }

            /// Optional Explanation
            if (!benefit.explanation.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = benefit.explanation,
                    style = MaterialTheme.typography.bodySmall,
                    color = CardPilotColors.Secondary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            /// Usage per benefit progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = CardPilotColors.CTA,
                trackColor = CardPilotColors.Gray200,
                strokeCap = StrokeCap.Round,
            )
        }
    }
}

@Preview
@Composable
fun BenefitTrackerPreview() {
    CardPilotTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BenefitItem(
                benefit = Benefit(
                    "바우처 (여행/호텔)",
                    150000.0,
                    200000.0,
                    "항공권 및 호텔 예약 시 사용 가능"
                ),
            )
        }
    }
}
