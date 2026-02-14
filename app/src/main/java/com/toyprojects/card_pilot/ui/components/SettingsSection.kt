package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.Gray100
import com.toyprojects.card_pilot.ui.theme.Secondary

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        /// Section title
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = Secondary,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        /// menu items
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(com.toyprojects.card_pilot.ui.theme.SurfaceGlass, RoundedCornerShape(24.dp))
                .border(1.dp, com.toyprojects.card_pilot.ui.theme.Outline, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
        ) {
            content()
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun SettingsSectionPreview() {
    com.toyprojects.card_pilot.ui.theme.CardPilotTheme {
        SettingsSection(title = "카드 관리") {
            SettingsRow(
                label = "내 카드 목록",
                onClick = { }
            )
            HorizontalDivider(color = Gray100, thickness = 1.dp)
            SettingsRow(
                label = "카드 추가",
                onClick = { }
            )
        }
    }
}
