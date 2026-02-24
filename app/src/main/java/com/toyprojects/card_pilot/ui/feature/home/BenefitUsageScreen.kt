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
import com.toyprojects.card_pilot.model.Transaction
import com.toyprojects.card_pilot.ui.feature.home.components.BenefitDetailHeader
import com.toyprojects.card_pilot.ui.feature.home.components.MonthSelector
import com.toyprojects.card_pilot.ui.feature.home.components.TransactionItem
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BenefitUsageScreen(
    benefitName: String = "여행 (Travel)",
    usedAmount: Long = 150000L,
    totalLimit: Long = 200000L,
    onBack: () -> Unit = {},
    onAddTransactionClick: () -> Unit = {}
) {
    // Mock Transactions
    // TODO: 실제 날짜값 받아와서 처리하는 방식으로 변경
    // TODO: eligible 필드는 Benefit 으로 이동
    val transactions = listOf(
        Transaction(
            id = 1,
            merchant = "대한항공",
            date = "02.14",
            time = "14:30",
            amount = 50000L,
            eligible = 60000L,
            monthGroup = "2026년 2월"
        ),
        Transaction(
            id = 2,
            merchant = "호텔 신라",
            date = "02.12",
            time = "09:00",
            amount = 100000L,
            eligible = 60000L,
            monthGroup = "2026년 2월"
        )
    )

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
                title = { Text(benefitName, style = MaterialTheme.typography.titleLarge) },
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
            /// Total usage of current benefit
            BenefitDetailHeader(
                description = "혜택 상세 내역입니다.",
                usedAmount = usedAmount,
                totalLimit = totalLimit
            )
            Spacer(modifier = Modifier.height(24.dp))

            /// Month Selector
            MonthSelector(
                currentMonth = "2026년 2월",
                onMonthSelected = {
                    // TODO: implement logic
                },
                availableMonths = listOf("2026년 1월", "2026년 2월")
            )
            Spacer(modifier = Modifier.height(16.dp))

            /// Add Item Button
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

            /// Transaction List
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
            onBack = {},
            onAddTransactionClick = {}
        )
    }
}
