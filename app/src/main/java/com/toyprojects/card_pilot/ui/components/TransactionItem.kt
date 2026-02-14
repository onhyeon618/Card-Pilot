package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.screens.Transaction
import com.toyprojects.card_pilot.ui.theme.Secondary
import com.toyprojects.card_pilot.ui.theme.TextPrimary

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /// Date and Time
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.width(50.dp)
        ) {
            Text(
                text = transaction.date,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = transaction.time,
                style = MaterialTheme.typography.labelSmall,
                color = Secondary
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        /// Merchant
        Text(
            text = transaction.merchant,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        /// Amount
        Text(
            text = "%,.0f원".format(transaction.amount),
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    com.toyprojects.card_pilot.ui.theme.CardPilotTheme {
        TransactionItem(
            transaction = Transaction(
                merchant = "Starbucks",
                date = "02.14",
                time = "12:30",
                amount = 5600.0,
                monthGroup = "2026년 2월"
            )
        )
    }
}