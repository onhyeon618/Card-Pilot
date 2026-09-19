package com.toyprojects.card_pilot.ui.feature.home.components

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
import com.toyprojects.card_pilot.ui.model.BenefitUiModel
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun BenefitItem(
    uiModel: BenefitUiModel,
    onClick: () -> Unit = {}
) {
    CardPilotRipple(color = CardPilotColors.gradientEnd) {
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
                /// 혜택 이름
                Text(
                    text = uiModel.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = CardPilotColors.textPrimary
                )
                /// 혜택 사용량 / 혜택 한도
                Text(
                    text = "${uiModel.formattedUsedAmount} / ${uiModel.formattedTotalAmount}",
                    style = MaterialTheme.typography.labelMedium,
                    color = CardPilotColors.secondary
                )
            }

            /// 혜택 설명
            if (!uiModel.explanation.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = uiModel.explanation,
                    style = MaterialTheme.typography.bodySmall,
                    color = CardPilotColors.secondary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            /// 혜택 사용량 그래프
            LinearProgressIndicator(
                progress = { uiModel.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = CardPilotColors.cta,
                trackColor = CardPilotColors.gray200,
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
                uiModel = BenefitUiModel(
                    id = 0L,
                    name = "바우처 (여행/호텔)",
                    explanation = "항공권 및 호텔 예약 시 사용 가능",
                    progress = 0.75f,
                    formattedUsedAmount = "150,000",
                    formattedTotalAmount = "200,000",
                    formattedRemainingAmount = "50,000",
                    isUnlimited = false
                )
            )
        }
    }
}
