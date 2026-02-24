package com.toyprojects.card_pilot.ui.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.feature.settings.components.SettingsRow
import com.toyprojects.card_pilot.ui.feature.settings.components.SettingsSection
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.EdgeToEdgeColumn
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onCardListClick: () -> Unit = {},
    onAddCardClick: () -> Unit = {}
) {
    GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "설정",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    CardPilotRipple {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "뒤로"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = CardPilotColors.TextPrimary,
                    titleContentColor = CardPilotColors.TextPrimary
                )
            )
        }
    ) { paddingValues ->
        EdgeToEdgeColumn(
            paddingValues = paddingValues,
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            /// Ad
            // TODO: Add ad
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardPilotColors.Gray50)
                    .border(
                        1.dp,
                        CardPilotColors.Outline,
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "광고 영역",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CardPilotColors.Gray300
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            /// Card Management Section
            SettingsSection(title = "카드 관리") {
                SettingsRow(
                    label = "내 카드 목록",
                    onClick = {
                        onCardListClick()
                    }
                )
                HorizontalDivider(color = CardPilotColors.Gray100, thickness = 1.dp)
                SettingsRow(
                    label = "카드 추가",
                    onClick = {
                        onAddCardClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /// General Section
            SettingsSection(title = "일반") {
                SettingsRow(
                    label = "노출 기준",
                    value = "실제 금액 대비",
                    onClick = {
                        // TODO
                    }
                )
                HorizontalDivider(color = CardPilotColors.Gray100, thickness = 1.dp)
                SettingsRow(
                    label = "데이터 초기화",
                    onClick = {
                        // TODO
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /// Info Section
            SettingsSection(title = "정보") {
                SettingsRow(
                    label = "앱 버전",
                    value = "1.0.0", // TODO: apply real version
                    showArrow = false
                )
                HorizontalDivider(color = CardPilotColors.Gray100, thickness = 1.dp)
                SettingsRow(
                    label = "오픈소스 라이선스",
                    onClick = {
                        // TODO
                    }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            /// Footer
            // TODO: use app icon
            Text(
                text = "CardPilot",
                style = MaterialTheme.typography.bodySmall,
                color = CardPilotColors.Secondary
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    CardPilotTheme {
        SettingsScreen(
            onBack = {},
            onCardListClick = {},
            onAddCardClick = {}
        )
    }
}
