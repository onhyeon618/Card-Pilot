package com.toyprojects.card_pilot.ui.feature.transaction.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.feature.transaction.TransactionFormData.Companion.DATE_FORMATTER
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/// 날짜 선택 다이얼로그
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDatePickerDialog(
    date: String,
    onDateChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val currentDate = LocalDate.parse(date, DATE_FORMATTER)
    val initialMillis = currentDate
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    // TODO: 디자인 개선
    DatePickerDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val selectedDate = Instant.ofEpochMilli(millis)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate()
                    onDateChange(selectedDate.format(DATE_FORMATTER))
                }
                onDismiss()
            }) {
                Text("확인", color = CardPilotColors.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소", color = CardPilotColors.secondary)
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = CardPilotColors.surface
        )
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = CardPilotColors.accent500,
                todayDateBorderColor = CardPilotColors.accent500
            )
        )
    }
}

@Preview
@Composable
fun TransactionDatePickerDialogPreview() {
    CardPilotTheme {
        TransactionDatePickerDialog(
            date = "2026.03.05",
            onDateChange = {},
            onDismiss = {}
        )
    }
}
