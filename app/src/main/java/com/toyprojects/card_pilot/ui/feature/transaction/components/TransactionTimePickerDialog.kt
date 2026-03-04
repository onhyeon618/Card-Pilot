package com.toyprojects.card_pilot.ui.feature.transaction.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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

    val timePickerState = rememberTimePickerState(
        initialHour = parsedTime.hour,
        initialMinute = parsedTime.minute,
        is24Hour = true
    )

    // TODO: 디자인 개선
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = CardPilotColors.Surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "시간 선택",
                    style = MaterialTheme.typography.titleMedium,
                    color = CardPilotColors.TextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                TimePicker(state = timePickerState)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("취소", color = CardPilotColors.Secondary)
                    }
                    TextButton(onClick = {
                        val formattedTime = "%02d:%02d".format(
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        onTimeChange(formattedTime)
                        onDismiss()
                    }) {
                        Text("확인", color = CardPilotColors.Primary)
                    }
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
