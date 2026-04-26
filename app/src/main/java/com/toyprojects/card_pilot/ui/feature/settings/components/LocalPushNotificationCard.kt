package com.toyprojects.card_pilot.ui.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

@Composable
fun LocalPushNotificationCard(
    localPushEnabled: Boolean,
    onToggleLocalPush: () -> Unit
) {
    val colors = CardPilotColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleLocalPush() }
            .padding(start = 32.dp, end = 24.dp, bottom = 16.dp, top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "요약 알림 받기",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "지출 내역을 요약해서 띄워드려요",
                style = MaterialTheme.typography.bodySmall,
                color = colors.secondary
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Switch(
            checked = localPushEnabled,
            onCheckedChange = { onToggleLocalPush() },
            modifier = Modifier.scale(0.8f),
            colors = SwitchDefaults.colors(
                checkedThumbColor = colors.white,
                checkedTrackColor = colors.cta,
                uncheckedThumbColor = colors.white,
                uncheckedTrackColor = colors.gray300,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LocalPushNotificationCardPreview() {
    LocalPushNotificationCard(
        localPushEnabled = true,
        onToggleLocalPush = {}
    )
}
