package com.toyprojects.card_pilot.ui.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

/// 입력 폼 화면 하단에 사용되는 공통 기본 액션 버튼
@Composable
fun PrimaryFormButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val buttonShape = remember { RoundedCornerShape(16.dp) }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 8.dp else 0.dp,
                shape = buttonShape,
                spotColor = CardPilotColors.primary.copy(alpha = 0.3f)
            ),
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = CardPilotColors.softAccent,
            contentColor = CardPilotColors.white,
            disabledContainerColor = CardPilotColors.gray300,
            disabledContentColor = CardPilotColors.textSecondary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryFormButtonPreview() {
    CardPilotTheme {
        PrimaryFormButton(
            text = "저장하기",
            onClick = {}
        )
    }
}
