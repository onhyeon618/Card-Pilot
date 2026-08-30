package com.toyprojects.card_pilot.ui.feature.transaction.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.CardSimpleInfo
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

@Composable
fun CardPickerItem(
    card: CardSimpleInfo,
    isSelected: Boolean,
    onClick: (CardSimpleInfo) -> Unit
) {
    val backgroundColor = if (isSelected) CardPilotColors.cta.copy(alpha = 0.05f) else CardPilotColors.surfaceGlass
    val borderColor = if (isSelected) CardPilotColors.cta.copy(alpha = 0.5f) else CardPilotColors.outline

    CardPilotRipple {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                .clickable { onClick(card) }
                .padding(vertical = 16.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .aspectRatio(1.58f)
                    .background(
                        brush = Brush.linearGradient(
                            colors = CardPilotColors.pastelGradientColors
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(0.5.dp, CardPilotColors.outline, RoundedCornerShape(6.dp))
            ) {
                if (card.image.isNotEmpty()) {
                    AsyncImage(
                        model = card.image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(6.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = card.name,
                style = MaterialTheme.typography.titleMedium,
                color = CardPilotColors.textPrimary
            )
        }
    }
}

@Composable
fun BenefitPickerItem(
    benefit: BenefitProperty,
    isSelected: Boolean,
    onClick: (BenefitProperty) -> Unit
) {
    val backgroundColor = if (isSelected) CardPilotColors.cta.copy(alpha = 0.05f) else CardPilotColors.surfaceGlass
    val borderColor = if (isSelected) CardPilotColors.cta.copy(alpha = 0.5f) else CardPilotColors.outline

    CardPilotRipple {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                .clickable { onClick(benefit) }
                .padding(vertical = 16.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = benefit.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = CardPilotColors.textPrimary
                )
                if (!benefit.explanation.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = benefit.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CardPilotColors.textSecondary
                    )
                }
            }
            if (benefit.rate > 0f) {
                val rateText = if (benefit.rate % 1f == 0f) {
                    benefit.rate.toInt().toString()
                } else {
                    benefit.rate.toString()
                }
                Box(
                    modifier = Modifier
                        .background(CardPilotColors.cta.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${rateText}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = CardPilotColors.cta
                    )
                }
            }
        }
    }
}
