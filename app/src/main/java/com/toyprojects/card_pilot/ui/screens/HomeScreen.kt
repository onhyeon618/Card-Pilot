package com.toyprojects.card_pilot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.ui.components.BenefitItem
import com.toyprojects.card_pilot.ui.components.CardDropdown
import com.toyprojects.card_pilot.ui.components.CardUsageSummary
import com.toyprojects.card_pilot.ui.components.GlassScaffold
import com.toyprojects.card_pilot.ui.components.MonthSelector
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    onBenefitClick: (Benefit) -> Unit
) {
    // Mock Data
    val selectedCard = "현대카드 The Red"
    val cardList = listOf("현대카드 The Red", "삼성카드 taptap O", "신한카드 Mr.Life")
    val benefits = listOf(
        Benefit("바우처 (여행/호텔)", 150000.0, 200000.0, "항공권 및 호텔 예약 시 사용 가능"),
        Benefit("PP카드 라운지", 2.0, 10.0),
        Benefit("메탈 플레이트 발급", 1.0, 1.0, "발급 수수료 면제")
    )
    val usageAmount = 1250450.0

    GlassScaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + 48.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 12.dp, top = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // TODO: create and apply app logo
                    Text(
                        text = "CardPilot",
                        style = MaterialTheme.typography.titleLarge,
                        color = CardPilotColors.TextPrimary
                    )

                    CompositionLocalProvider(
                        LocalRippleConfiguration provides RippleConfiguration(color = CardPilotColors.PastelViolet)
                    ) {
                        IconButton(
                            onClick = {
                                onSettingsClick()
                            }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "설정",
                                tint = CardPilotColors.TextPrimary
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            /// Card selection dropdown
            item {
                CardDropdown(
                    selectedCard = selectedCard,
                    cardList = cardList,
                    onCardSelected = {
                        // TODO: implement logic
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                MonthSelector(
                    currentMonth = "2026년 2월",
                    onMonthSelected = {
                        // TODO: implement logic
                    },
                    availableMonths = listOf("2026년 1월", "2026년 2월")
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            /// Total usage of selected card
            item {
                CardUsageSummary(
                    usedAmount = usageAmount
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            /// Benefit tracker of selected card
            itemsIndexed(benefits) { index, benefit ->
                BenefitItem(
                    benefit = benefit,
                    onClick = { onBenefitClick(benefit) }
                )
                if (index < benefits.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = CardPilotColors.Gray200,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    CardPilotTheme {
        HomeScreen(
            onSettingsClick = {},
            onBenefitClick = {}
        )
    }
}
