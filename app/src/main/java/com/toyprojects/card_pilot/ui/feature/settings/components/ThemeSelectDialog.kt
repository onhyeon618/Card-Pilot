package com.toyprojects.card_pilot.ui.feature.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.toyprojects.card_pilot.model.ThemeType
import com.toyprojects.card_pilot.ui.theme.CardPilotColorPalette
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import com.toyprojects.card_pilot.ui.theme.palette

@Composable
fun ThemeSelectDialog(
    currentTheme: ThemeType,
    onThemeSelected: (ThemeType) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = CardPilotColors

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = colors.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "테마 색상",
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.textPrimary
                )

                Spacer(modifier = Modifier.height(24.dp))

                ThemeType.entries.forEachIndexed { index, themeType ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    ThemeOption(
                        label = themeType.label,
                        palette = themeType.palette,
                        isSelected = currentTheme == themeType,
                        onClick = { onThemeSelected(themeType) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeOption(
    label: String,
    palette: CardPilotColorPalette,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = CardPilotColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = colors.softAccent,
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = colors.gray200,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 미리보기
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(palette.backgroundGradientColors)
                )
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "선택됨",
                tint = colors.softAccent,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun ThemeSelectDialogPreview() {
    CardPilotTheme {
        ThemeSelectDialog(
            currentTheme = ThemeType.PURPLE,
            onThemeSelected = {},
            onDismiss = {}
        )
    }
}
