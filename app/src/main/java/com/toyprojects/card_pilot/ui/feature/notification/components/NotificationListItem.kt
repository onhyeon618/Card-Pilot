package com.toyprojects.card_pilot.ui.feature.notification.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

@Composable
fun NotificationListItem(
    appName: String,
    timestamp: String,
    amount: String,
    content: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = appName,
                style = MaterialTheme.typography.bodySmall,
                color = CardPilotColors.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = CardPilotColors.secondary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = amount,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = CardPilotColors.textPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = CardPilotColors.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationListItemPreview() {
    NotificationListItem(
        appName = "현대카드",
        timestamp = "3/8 12:00",
        amount = "10,000원",
        content = "[Web발신] 현대카드 승인 임*석님 10,000원 03/08 12:00 투썸플레이스 강남역점",
        onClick = {}
    )
}
