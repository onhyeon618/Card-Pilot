package com.toyprojects.card_pilot.ui.feature.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.feature.settings.model.CardCompanyApp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

@Composable
fun CardAppListItem(
    app: CardCompanyApp,
    isChecked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = CardPilotColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(enabled = enabled) { onCheckedChange(!isChecked) }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (app.icon != null) {
                Image(
                    bitmap = app.icon,
                    contentDescription = "${app.displayName} 아이콘",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.gray200)
                )
            }
            Text(
                text = app.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textPrimary
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            modifier = Modifier.scale(0.8f),
            colors = SwitchDefaults.colors(
                checkedThumbColor = colors.white,
                checkedTrackColor = colors.cta,
                uncheckedThumbColor = colors.white,
                uncheckedTrackColor = colors.gray300,
                uncheckedBorderColor = Color.Transparent,
                disabledCheckedThumbColor = colors.white,
                disabledCheckedTrackColor = colors.cta.copy(alpha = 0.5f),
                disabledUncheckedThumbColor = colors.white,
                disabledUncheckedTrackColor = colors.gray300.copy(alpha = 0.5f)
            )
        )
    }
}

@Preview
@Composable
fun CardAppListItemPreview() {
    CardAppListItem(
        app = CardCompanyApp(
            displayName = "샘플 앱",
            packageName = ""
        ),
        isChecked = true,
        enabled = true,
        onCheckedChange = {}
    )
}
