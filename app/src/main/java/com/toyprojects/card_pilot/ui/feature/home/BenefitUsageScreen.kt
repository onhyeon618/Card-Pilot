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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.home.components.BenefitDetailHeader
import com.toyprojects.card_pilot.ui.feature.home.components.MonthSelector
import com.toyprojects.card_pilot.ui.feature.home.components.TransactionItem
import com.toyprojects.card_pilot.ui.model.BenefitUiModel
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.GlassAlertDialog
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import java.time.YearMonth

@Composable
fun BenefitUsageRoute(
    viewModel: BenefitUsageViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onAddTransactionClick: (Long, Long) -> Unit = { _, _ -> },
    onEditTransactionClick: (Long, Long, Long) -> Unit = { _, _, _ -> },
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var transactionToDelete by remember { mutableStateOf<Long?>(null) }

    BenefitUsageScreen(
        uiState = uiState,
        cardId = uiState.cardId,
        benefit = uiState.benefit,
        onMonthSelected = viewModel::selectMonth,
        onAddTransactionClick = onAddTransactionClick,
        onEditTransactionClick = onEditTransactionClick,
        onDeleteRequest = { transactionToDelete = it },
        onToggleMode = viewModel::toggleAmountDisplayMode,
        onBack = onBack
    )

    if (transactionToDelete != null) {
        GlassAlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = stringResource(R.string.msg_delete_payment),
            description = stringResource(R.string.msg_cannot_be_undone),
            confirmText = stringResource(R.string.btn_delete),
            onConfirm = {
                transactionToDelete?.let { viewModel.deleteTransaction(it) }
                transactionToDelete = null
            },
            dismissText = stringResource(R.string.btn_cancel),
            onDismiss = { transactionToDelete = null },
            isDestructive = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BenefitUsageScreen(
    uiState: BenefitUsageUiState,
    cardId: Long,
    benefit: BenefitUiModel?,
    onMonthSelected: (YearMonth) -> Unit = {},
    onAddTransactionClick: (Long, Long) -> Unit = { _, _ -> },
    onEditTransactionClick: (Long, Long, Long) -> Unit = { _, _, _ -> },
    onDeleteRequest: (Long) -> Unit = {},
    onToggleMode: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val transactions = uiState.transactions

    // 지출 항목 스와이프 관련
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
                title = { Text(benefit?.name ?: "-", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    CardPilotRipple {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.desc_back)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = CardPilotColors.textPrimary,
                    titleContentColor = CardPilotColors.textPrimary
                )
            )
        }
    ) { paddingValues ->
        if (benefit != null) {
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
                    uiModel = benefit,
                    amountDisplayMode = uiState.amountDisplayMode,
                    onToggleMode = onToggleMode
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
                    CardPilotRipple(color = CardPilotColors.gradientEnd) {
                        OutlinedButton(
                            onClick = {
                                onAddTransactionClick(cardId, benefit.id)
                            },
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(
                                1.dp,
                                CardPilotColors.outline
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = CardPilotColors.secondary,
                                containerColor = CardPilotColors.surface
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
                                text = stringResource(R.string.title_add_payment),
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
                                    text = stringResource(R.string.msg_no_usage),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CardPilotColors.secondary
                                )
                            }
                        }
                    } else {
                        itemsIndexed(
                            items = transactions,
                            key = { index, item -> if (item.id != 0L) item.id else "item_${index}_${item.merchant}" }
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
                                    onEditTransactionClick(item.id, cardId, benefit.id)
                                },
                                onDelete = {
                                    revealedItemIndex = null
                                    onDeleteRequest(item.id)
                                }
                            )
                            HorizontalDivider(
                                color = CardPilotColors.gray200,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    Text(text = stringResource(R.string.msg_loading), color = CardPilotColors.secondary)
                } else {
                    Text(text = stringResource(R.string.msg_benefit_not_found), color = CardPilotColors.secondary)
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
            cardId = 1L,
            benefit = BenefitUiModel(
                id = 1L,
                name = "스타벅스 50% 할인",
                explanation = "스타벅스 (최대 1만원 한도)",
                progress = 0.45f,
                formattedUsedAmount = "4,500",
                formattedTotalAmount = "10,000",
                formattedRemainingAmount = "5,500",
                isUnlimited = false
            ),
        )
    }
}
