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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.model.Transaction
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

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
                color = CardPilotColors.TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = transaction.time,
                style = MaterialTheme.typography.labelSmall,
                color = CardPilotColors.Secondary
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            /// Merchant
            Text(
                text = transaction.merchant,
                style = MaterialTheme.typography.bodyMedium,
                color = CardPilotColors.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            /// Amount
            val amountText = buildAnnotatedString {
                append("%,.0f원".format(transaction.amount))
                if (transaction.eligible != null && transaction.eligible < transaction.amount) {
                    withStyle(style = SpanStyle(color = CardPilotColors.Secondary)) {
                        append(" (적용 금액 %,.0f원)".format(transaction.eligible))
                    }
                }
            }

            Text(
                text = amountText,
                style = MaterialTheme.typography.bodyMedium,
                color = CardPilotColors.TextPrimary
            )
        }
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