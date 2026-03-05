package com.toyprojects.card_pilot.ui.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun SettingsRow(
    label: String,
    value: String? = null,
    valueWidget: @Composable (() -> Unit)? = null,
    showArrow: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    CardPilotRipple {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            /// 메뉴 타이틀
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = CardPilotColors.textPrimary
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                /// 추가 정보
                if (valueWidget != null) {
                    valueWidget()
                } else if (value != null) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CardPilotColors.secondary
                    )
                }

                /// Arrow icon
                if (showArrow) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = CardPilotColors.gray300
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsRowPreview() {
    CardPilotTheme {
        SettingsRow(
            label = "내 카드 목록",
            onClick = { }
        )
    }
}
