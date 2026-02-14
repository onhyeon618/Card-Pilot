package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CTA
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import com.toyprojects.card_pilot.ui.theme.Gray200
import com.toyprojects.card_pilot.ui.theme.Secondary
import com.toyprojects.card_pilot.ui.theme.TextPrimary

data class Benefit(
    val category: String,
    val used: Double,
    val total: Double,
    val explanation: String? = null
)

@Composable
fun BenefitTracker(
    benefits: List<Benefit>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        benefits.forEachIndexed { index, benefit ->
            BenefitItem(benefit)
            if (index < benefits.lastIndex) {
                Spacer(modifier = Modifier.height(32.dp))
                HorizontalDivider(
                    color = Gray200,
                    thickness = 0.5.dp
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun BenefitItem(benefit: Benefit) {
    val progress = (benefit.used / benefit.total).toFloat().coerceIn(0f, 1f)

    val usedAmount = "%,.0f".format(benefit.used)
    val totalAmount = "%,.0f".format(benefit.total)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            /// Category name
            Text(
                text = benefit.category,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            /// Usage per category
            Text(
                text = "$usedAmount / $totalAmount",
                style = MaterialTheme.typography.labelMedium,
                color = Secondary
            )
        }

        /// Optional Explanation
        if (!benefit.explanation.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = benefit.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = Secondary,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        /// Usage per category progress bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = CTA,
            trackColor = Gray200,
            strokeCap = StrokeCap.Round,
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun BenefitTrackerPreview() {
    val sampleBenefits = listOf(
        Benefit("바우처 (여행/호텔)", 150000.0, 200000.0, "항공권 및 호텔 예약 시 사용 가능"),
        Benefit("PP카드 라운지", 2.0, 10.0),
        Benefit("메탈 플레이트 발급", 1.0, 1.0)
    )

    CardPilotTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BenefitTracker(benefits = sampleBenefits)
        }
    }
}
