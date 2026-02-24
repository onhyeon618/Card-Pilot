package com.toyprojects.card_pilot.ui.feature.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun MonthSelector(
    currentMonth: String,
    onMonthSelected: (String) -> Unit,
    availableMonths: List<String>
) {
    val currentIndex = availableMonths.indexOf(currentMonth)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = CardPilotColors.SurfaceGlass,
            border = BorderStroke(
                1.dp,
                CardPilotColors.Outline
            )
        ) {
            CardPilotRipple {
                Row(
                    modifier = Modifier
                        .padding(vertical = 0.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (currentIndex > 0) {
                                onMonthSelected(availableMonths[currentIndex - 1])
                            }
                        },
                        enabled = currentIndex > 0
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "이전달",
                            tint = if (currentIndex > 0) CardPilotColors.TextPrimary else CardPilotColors.Secondary.copy(
                                alpha = 0.3f
                            ),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = currentMonth,
                        style = MaterialTheme.typography.titleMedium,
                        color = CardPilotColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = {
                            if (currentIndex < availableMonths.lastIndex) {
                                onMonthSelected(availableMonths[currentIndex + 1])
                            }
                        },
                        enabled = currentIndex < availableMonths.lastIndex
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "다음달",
                            tint = if (currentIndex < availableMonths.lastIndex) CardPilotColors.TextPrimary else CardPilotColors.Secondary.copy(
                                alpha = 0.3f
                            ),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthSelectorPreview() {
    CardPilotTheme {
        MonthSelector(
            currentMonth = "2026년 2월",
            onMonthSelected = {},
            availableMonths = listOf("2026년 1월", "2026년 2월", "2026년 3월")
        )
    }
}
