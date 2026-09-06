package com.toyprojects.card_pilot.ui.feature.transaction.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.feature.transaction.TransactionFormData.Companion.TIME_FORMATTER
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import java.time.LocalTime

/// 시간 선택 다이얼로그
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTimePickerDialog(
    time: String,
    onTimeChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val parsedTime = LocalTime.parse(time, TIME_FORMATTER)

    var selectedHour by remember { mutableStateOf(parsedTime.hour) }
    var selectedMinute by remember { mutableStateOf(parsedTime.minute) }

    SolidDialog(
        onDismissRequest = onDismiss,
        gradientHeight = 120.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "시간 선택",
                style = MaterialTheme.typography.titleMedium,
                color = CardPilotColors.textPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            CardPilotTimePicker(
                hour = selectedHour,
                minute = selectedMinute,
                onHourChange = { selectedHour = it },
                onMinuteChange = { selectedMinute = it }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("취소", color = CardPilotColors.secondary)
                }
                TextButton(onClick = {
                    val formattedTime = "%02d:%02d".format(
                        selectedHour,
                        selectedMinute
                    )
                    onTimeChange(formattedTime)
                    onDismiss()
                }) {
                    Text("확인", color = CardPilotColors.cta)
                }
            }
        }
    }
}

@Preview
@Composable
fun TransactionTimePickerDialogPreview() {
    CardPilotTheme {
        TransactionTimePickerDialog(
            time = "14:30",
            onTimeChange = {},
            onDismiss = {}
        )
    }
}
