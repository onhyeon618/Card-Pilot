package com.toyprojects.card_pilot.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

/// 블러 및 글래스모피즘 효과가 적용된 공통 모달 바텀시트
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    contentWindowInsets: @Composable () -> WindowInsets = { WindowInsets.statusBars },
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetShape = remember { RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp) }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        dragHandle = null,
        contentWindowInsets = contentWindowInsets
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 24.dp,
                    shape = sheetShape,
                    spotColor = CardPilotColors.primary.copy(alpha = 0.15f)
                )
                .background(
                    color = lerp(CardPilotColors.pastelStart, CardPilotColors.background, 0.85f),
                    shape = sheetShape
                )
                .border(
                    width = 1.dp,
                    color = CardPilotColors.white.copy(alpha = 0.8f),
                    shape = sheetShape
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                content = content
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun GlassBottomSheetPreview() {
    CardPilotTheme {
        GlassBottomSheet(
            onDismissRequest = {}
        ) {
            Text(
                text = "바텀시트 내용 예시",
                color = CardPilotColors.textPrimary
            )
        }
    }
}
