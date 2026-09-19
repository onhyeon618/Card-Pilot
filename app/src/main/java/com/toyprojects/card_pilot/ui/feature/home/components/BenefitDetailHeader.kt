package com.toyprojects.card_pilot.ui.feature.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.model.BenefitDisplayMode
import com.toyprojects.card_pilot.ui.model.BenefitUiModel
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun BenefitDetailHeader(
    uiModel: BenefitUiModel,
    amountDisplayMode: BenefitDisplayMode = BenefitDisplayMode.BENEFIT,
    onToggleMode: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(CardPilotColors.surfaceGlass, RoundedCornerShape(24.dp))
            .border(1.dp, CardPilotColors.outline, RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        /// 혜택 상세 설명
        if (!uiModel.explanation.isNullOrEmpty()) {
            Text(
                text = uiModel.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = CardPilotColors.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        /// 혜택 한도 사용량
        LinearProgressIndicator(
            progress = { uiModel.progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(CircleShape),
            color = CardPilotColors.cta,
            trackColor = CardPilotColors.surface,
            strokeCap = StrokeCap.Round,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            /// 사용/한도 표시 방식 선택 버튼
            Surface(
                onClick = onToggleMode,
                shape = CircleShape,
                color = CardPilotColors.gradientStart,
                border = BorderStroke(1.dp, CardPilotColors.outline)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (amountDisplayMode == BenefitDisplayMode.PAYMENT) "사용금액" else "적립/할인",
                        style = MaterialTheme.typography.labelSmall,
                        color = CardPilotColors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.icon_switch),
                        contentDescription = "기준 변경",
                        tint = CardPilotColors.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            /// 사용/한도 금액
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${uiModel.formattedUsedAmount} / ${uiModel.formattedTotalAmount}",
                    style = MaterialTheme.typography.titleMedium,
                    color = CardPilotColors.textPrimary
                )
                Text(
                    text = "남은 한도: ${uiModel.formattedRemainingAmount}",
                    style = MaterialTheme.typography.labelSmall,
                    color = CardPilotColors.secondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BenefitDetailHeaderPreview() {
    CardPilotTheme {
        BenefitDetailHeader(
            uiModel = BenefitUiModel(
                id = 0L,
                name = "바우처",
                explanation = "바우처 및 할인 혜택 상세 내역입니다.",
                progress = 0.75f,
                formattedUsedAmount = "150,000",
                formattedTotalAmount = "200,000",
                formattedRemainingAmount = "50,000",
                isUnlimited = false
            )
        )
    }
}
