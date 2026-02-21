package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthSelector(
    currentMonth: String,
    onMonthSelected: (String) -> Unit,
    availableMonths: List<String>
) {
    val currentIndex = availableMonths.indexOf(currentMonth)

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Surface(
            shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
            color = CardPilotColors.SurfaceGlass,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                CardPilotColors.Outline
            )
        ) {
            CompositionLocalProvider(
                LocalRippleConfiguration provides RippleConfiguration(color = CardPilotColors.PastelViolet)
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (currentIndex > 0) {
                                onMonthSelected(availableMonths[currentIndex - 1])
                            }
                        },
                        enabled = currentIndex > 0,
                        modifier = Modifier.size(32.dp)
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
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    IconButton(
                        onClick = {
                            if (currentIndex < availableMonths.lastIndex) {
                                onMonthSelected(availableMonths[currentIndex + 1])
                            }
                        },
                        enabled = currentIndex < availableMonths.lastIndex,
                        modifier = Modifier.size(32.dp)
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

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun MonthSelectorPreview() {
    com.toyprojects.card_pilot.ui.theme.CardPilotTheme {
        MonthSelector(
            currentMonth = "2026년 2월",
            onMonthSelected = {},
            availableMonths = listOf("2026년 1월", "2026년 2월", "2026년 3월")
        )
    }
}
