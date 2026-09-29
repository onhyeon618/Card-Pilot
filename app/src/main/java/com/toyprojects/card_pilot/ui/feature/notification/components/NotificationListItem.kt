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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("M/d HH:mm")

@Composable
fun NotificationListItem(
    appName: String,
    timestamp: LocalDateTime,
    amount: String,
    content: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayTimestamp = timestamp.format(DISPLAY_FORMATTER)
    val accessibleTimestamp =
        "${timestamp.monthValue}월 ${timestamp.dayOfMonth}일 ${timestamp.hour}시 ${timestamp.minute}분"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                onClickLabel = stringResource(R.string.desc_action_register_transaction),
                onClick = onClick
            )
            .semantics(mergeDescendants = true) {}
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /// 알림 발생 앱 이름
            Text(
                text = appName,
                style = MaterialTheme.typography.bodySmall,
                color = CardPilotColors.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            /// 알림 수신 일시
            Text(
                text = displayTimestamp,
                style = MaterialTheme.typography.bodySmall,
                color = CardPilotColors.secondary,
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = accessibleTimestamp
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        /// 결제 금액
        Text(
            text = amount,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = CardPilotColors.textPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        /// 실제로 수신한 알림 내용
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
        timestamp = LocalDateTime.of(2026, 3, 8, 12, 0),
        amount = "10,000원",
        content = "[Web발신] 현대카드 승인 임*석님 10,000원 03/08 12:00 투썸플레이스 강남역점",
        onClick = {}
    )
}
