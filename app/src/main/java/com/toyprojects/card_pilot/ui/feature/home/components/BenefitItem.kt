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
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.ui.model.BenefitUiModel
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun BenefitItem(
    uiModel: BenefitUiModel,
    onClick: () -> Unit = {}
) {
    val viewDetailLabel = stringResource(R.string.desc_view_benefit_detail)
    val explanationText = if (!uiModel.explanation.isNullOrBlank()) "${uiModel.explanation}\n" else ""
    val usageLimitText = if (uiModel.isUnlimited) {
        stringResource(R.string.desc_benefit_usage_unlimited, uiModel.formattedUsedAmount)
    } else {
        stringResource(R.string.desc_benefit_usage_limited, uiModel.formattedTotalAmount, uiModel.formattedUsedAmount)
    }
    val benefitDescription = "${uiModel.name}\n$explanationText$usageLimitText"

    CardPilotRipple(color = CardPilotColors.gradientEnd) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .clickable(onClick = onClick)
                .clearAndSetSemantics {
                    role = Role.Button
                    contentDescription = benefitDescription
                    onClick(label = viewDetailLabel) {
                        onClick()
                        true
                    }
                }
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
