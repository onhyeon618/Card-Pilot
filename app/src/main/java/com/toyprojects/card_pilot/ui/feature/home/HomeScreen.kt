package com.toyprojects.card_pilot.ui.feature.home

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.home.components.BenefitItem
import com.toyprojects.card_pilot.ui.feature.home.components.CardDropdown
import com.toyprojects.card_pilot.ui.feature.home.components.CardUsageSummary
import com.toyprojects.card_pilot.ui.feature.home.components.MonthSelector
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

import java.time.YearMonth

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBenefitClick: (Benefit) -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onCardSelected = viewModel::selectCard,
        onMonthSelected = viewModel::selectMonth,
        onBenefitClick = onBenefitClick,
        onSettingsClick = onSettingsClick
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onCardSelected: (Long) -> Unit,
    onMonthSelected: (YearMonth) -> Unit,
    onBenefitClick: (Benefit) -> Unit,
    onSettingsClick: () -> Unit
) {

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

                    CardPilotRipple {
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

            /// 카드 선택 드롭다운 박스
            // TODO: 카드 목록 비어있을 때
            item {
                CardDropdown(
                    selectedCard = uiState.cardList.find { it.id == uiState.selectedCardId },
                    cardList = uiState.cardList,
                    onCardSelected = onCardSelected
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            /// 월 선택 박스
            item {
                MonthSelector(
                    selectedMonth = uiState.selectedYearMonth,
                    onMonthSelected = onMonthSelected
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            /// 선택한 카드의 이번달 총 사용 금액
            item {
                CardUsageSummary(
                    usedAmount = uiState.cardInfo?.usageAmount ?: 0L
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            val benefits = uiState.cardInfo?.benefits ?: emptyList()

            /// 선택한 카드의 혜택 사용 현황
            // TODO: 카드 목록 or 혜택 목록 비어있을 때
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
            uiState = HomeUiState(),
            onMonthSelected = {},
            onCardSelected = {},
            onSettingsClick = {},
            onBenefitClick = {}
        )
    }
}
