package com.toyprojects.card_pilot.ui.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun CardUsageSummary(
    usedAmount: Long
) {
    val formattedUsed = "%,d원".format(usedAmount)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(CardPilotColors.SurfaceGlass, RoundedCornerShape(24.dp))
            .border(1.dp, CardPilotColors.Outline, RoundedCornerShape(24.dp))
            .padding(vertical = 32.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "이번 달 사용 금액",
            style = MaterialTheme.typography.titleMedium,
            color = CardPilotColors.Secondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formattedUsed,
            style = MaterialTheme.typography.displaySmall,
            color = CardPilotColors.TextPrimary
        )
    }
}

@Preview
@Composable
fun CardUsageSummaryPreview() {
    CardPilotTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(CardPilotColors.White)
        ) {
            CardUsageSummary(
                usedAmount = 1250450L
            )
        }
    }
}
