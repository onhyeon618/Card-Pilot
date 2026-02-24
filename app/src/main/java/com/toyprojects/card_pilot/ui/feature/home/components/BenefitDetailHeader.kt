package com.toyprojects.card_pilot.ui.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun BenefitDetailHeader(
    description: String? = null,
    usedAmount: Long,
    totalLimit: Long
) {
    val progress = if (totalLimit > 0) (usedAmount.toFloat() / totalLimit.toFloat()).coerceIn(0f, 1f) else 0f
    val usedStr = "%,d".format(usedAmount)
    val totalStr = "%,d".format(totalLimit)
    val remainingStr = "%,d".format(totalLimit - usedAmount)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(CardPilotColors.SurfaceGlass, RoundedCornerShape(24.dp))
            .border(1.dp, CardPilotColors.Outline, RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        /// Benefit description
        if (!description.isNullOrEmpty()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = CardPilotColors.Secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        /// Benefit total usage progress bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(CircleShape),
            color = CardPilotColors.CTA,
            trackColor = CardPilotColors.Surface,
            strokeCap = StrokeCap.Round,
        )

        Spacer(modifier = Modifier.height(8.dp))

        /// Benefit total usage in number
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$usedStr / $totalStr",
                style = MaterialTheme.typography.titleMedium,
                color = CardPilotColors.TextPrimary
            )
            Text(
                text = "남은 한도: $remainingStr",
                style = MaterialTheme.typography.labelSmall,
                color = CardPilotColors.Secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BenefitDetailHeaderPreview() {
    CardPilotTheme {
        BenefitDetailHeader(
            description = "바우처 및 할인 혜택 상세 내역입니다.",
            usedAmount = 150000L,
            totalLimit = 200000L
        )
    }
}
