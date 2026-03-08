package com.toyprojects.card_pilot.ui.feature.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

@Composable
fun GlobalNotificationCard(
    notiReceiveEnabled: Boolean,
    onToggleNotiReceive: () -> Unit
) {
    val colors = CardPilotColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(
                elevation = if (notiReceiveEnabled) 8.dp else 2.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = colors.primary.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(colors.white.copy(alpha = 0.9f))
            .clickable { onToggleNotiReceive() }
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (notiReceiveEnabled) colors.primary.copy(alpha = 0.1f) else colors.gray100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = if (notiReceiveEnabled) colors.primary else colors.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column {
                Text(
                    text = "지출 알림 수신",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "결제 승인 내역을 가져옵니다",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.secondary
                )
            }
        }

        Switch(
            checked = notiReceiveEnabled,
            onCheckedChange = { onToggleNotiReceive() },
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
fun GlobalNotificationCardPreview() {
    GlobalNotificationCard(
        notiReceiveEnabled = true,
        onToggleNotiReceive = {}
    )
}
