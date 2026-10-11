package com.toyprojects.card_pilot.ui.shared

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

/// 키보드가 없을 때는 하단 고정, 스크롤 발생 시 최하단으로 이어지는 폼 스크롤 레이아웃
@Composable
fun StickyFooterFormLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalPadding: Dp = 24.dp,
    bodyVerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(24.dp),
    scrollState: ScrollState = rememberScrollState(),
    footerContent: @Composable ColumnScope.() -> Unit,
    bodyContent: @Composable ColumnScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding)
            .imePadding()
    ) {
        val minimumScrollHeight = this.maxHeight

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .verticalScroll(scrollState)
                .defaultMinSize(minHeight = minimumScrollHeight),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = bodyVerticalArrangement,
                content = bodyContent
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 24.dp),
                content = footerContent
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StickyFooterFormLayoutPreview() {
    CardPilotTheme {
        StickyFooterFormLayout(
            contentPadding = PaddingValues(0.dp),
            footerContent = {
                PrimaryFormButton(
                    text = "저장하기",
                    onClick = {}
                )
            }
        ) {
            Text(
                text = "폼 본문 예시",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}
