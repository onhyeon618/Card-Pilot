package com.toyprojects.card_pilot.ui.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.home.components.BenefitDetailHeader
import com.toyprojects.card_pilot.ui.feature.home.components.MonthSelector
import com.toyprojects.card_pilot.ui.feature.home.components.TransactionItem
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BenefitUsageRoute(
    // TODO: 홈화면에서 실제 Benefit argument 받아와야 함
    viewModel: BenefitUsageViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onAddTransactionClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BenefitUsageScreen(
        uiState = uiState,
        benefit = Benefit(
            name = "스타벅스 50% 할인",
            explanation = "스타벅스 월 최대 1만원 한도내",
            capAmount = 10000L,
            usedAmount = 4500L,
            displayOrder = 1,
        ),
        onMonthSelected = viewModel::selectMonth,
        onAddTransactionClick = onAddTransactionClick,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BenefitUsageScreen(
    uiState: BenefitUsageUiState,
    benefit: Benefit,
    onMonthSelected: (YearMonth) -> Unit = {},
    onAddTransactionClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val transactions = uiState.transactions

    var revealedItemIndex by remember { mutableStateOf<Int?>(null) }
    val listState = rememberLazyListState()

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            revealedItemIndex = null
        }
    }

    GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(benefit.name, style = MaterialTheme.typography.titleLarge) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { revealedItemIndex = null }
                    )
                }
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            /// 혜택 상세 정보 - 설명, 한도 사용량
            BenefitDetailHeader(
                description = benefit.explanation,
                usedAmount = benefit.usedAmount,
                totalLimit = benefit.capAmount
            )
            Spacer(modifier = Modifier.height(24.dp))

            /// 월 선택 박스
            MonthSelector(
                selectedMonth = uiState.selectedYearMonth,
                onMonthSelected = onMonthSelected
            )
            Spacer(modifier = Modifier.height(16.dp))

            /// 지출 항목 추가하기 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                CardPilotRipple(color = CardPilotColors.GradientPeach) {
                    OutlinedButton(
                        onClick = {
                            onAddTransactionClick()
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(
                            1.dp,
                            CardPilotColors.Outline
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = CardPilotColors.Secondary,
                            containerColor = CardPilotColors.Surface
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "지출 항목 추가",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /// 지출 내역
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp + paddingValues.calculateBottomPadding())
            ) {
                if (transactions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "사용 내역이 없습니다.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CardPilotColors.Secondary
                            )
                        }
                    }
                } else {
                    itemsIndexed(
                        items = transactions,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        TransactionItem(
                            transaction = item,
                            isRevealed = revealedItemIndex == index,
                            onRevealChange = { isRevealed ->
                                if (isRevealed) {
                                    revealedItemIndex = index
                                } else if (revealedItemIndex == index) {
                                    revealedItemIndex = null
                                }
                            },
                            onEdit = {
                                // TODO: Navigate to Edit screen or show Edit dialog
                            },
                            onDelete = {
                                // TODO: Show confirm dialog and delete
                            }
                        )
                        HorizontalDivider(
                            color = CardPilotColors.Gray200,
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BenefitUsageScreenPreview() {
    CardPilotTheme {
        BenefitUsageScreen(
            uiState = BenefitUsageUiState(),
            benefit = Benefit(
                name = "스타벅스 50% 할인",
                explanation = "스타벅스 월 최대 1만원 한도내",
                capAmount = 10000L,
                usedAmount = 4500L,
                displayOrder = 1,
            ),
        )
    }
}
