package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.Secondary
import com.toyprojects.card_pilot.ui.theme.TextPrimary

@Composable
fun MonthSelector(
    currentMonth: String,
    onMonthSelected: (String) -> Unit,
    availableMonths: List<String>
) {
    val currentIndex = availableMonths.indexOf(currentMonth)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
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
                tint = if (currentIndex > 0) TextPrimary else Secondary.copy(alpha = 0.3f)
            )
        }

        Text(
            text = currentMonth,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
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
                tint = if (currentIndex < availableMonths.lastIndex) TextPrimary else Secondary.copy(alpha = 0.3f)
            )
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
