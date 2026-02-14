package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.Primary
import com.toyprojects.card_pilot.ui.theme.Secondary
import com.toyprojects.card_pilot.ui.theme.SurfaceCard
import com.toyprojects.card_pilot.ui.theme.TextPrimary

@Composable
fun CategoryHeader(
    description: String? = null,
    usedAmount: Double,
    totalLimit: Double
) {
    val progress = (usedAmount / totalLimit).toFloat().coerceIn(0f, 1f)
    val usedStr = "%,.0f".format(usedAmount)
    val totalStr = "%,.0f".format(totalLimit)
    val remainingStr = "%,.0f".format(totalLimit - usedAmount)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(SurfaceCard, RoundedCornerShape(16.dp))
            .padding(24.dp)
    ) {
        /// Category description
        if (!description.isNullOrEmpty()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Secondary
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
            color = Primary,
            trackColor = Color.White,
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
                color = TextPrimary
            )
            Text(
                text = "남은 한도: $remainingStr",
                style = MaterialTheme.typography.labelSmall,
                color = Secondary
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun CategoryHeaderPreview() {
    com.toyprojects.card_pilot.ui.theme.CardPilotTheme {
        CategoryHeader(
            description = "바우처 및 할인 혜택 상세 내역입니다.",
            usedAmount = 150000.0,
            totalLimit = 200000.0
        )
    }
}