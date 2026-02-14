package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import com.toyprojects.card_pilot.ui.theme.Secondary
import com.toyprojects.card_pilot.ui.theme.SurfaceCard
import com.toyprojects.card_pilot.ui.theme.TextPrimary

@Composable
fun CardUsageSummary(
    usedAmount: Double
) {
    val formattedUsed = "%,.0f원".format(usedAmount)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(SurfaceCard, RoundedCornerShape(12.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "이번 달 사용 금액",
            style = MaterialTheme.typography.labelMedium,
            color = Secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formattedUsed,
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CardUsageSummaryPreview() {
    CardPilotTheme {
        Box(modifier = Modifier
            .padding(16.dp)
            .background(Color.White)) {
            CardUsageSummary(
                usedAmount = 1250450.0
            )
        }
    }
}
