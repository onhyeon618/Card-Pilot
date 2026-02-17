package com.toyprojects.card_pilot.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.model.Transaction
import com.toyprojects.card_pilot.ui.components.CategoryHeader
import com.toyprojects.card_pilot.ui.components.MonthSelector
import com.toyprojects.card_pilot.ui.components.TransactionItem
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import com.toyprojects.card_pilot.ui.theme.Gray200
import com.toyprojects.card_pilot.ui.theme.Secondary
import com.toyprojects.card_pilot.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryDetailScreen(
    categoryName: String = "여행 (Travel)",
    usedAmount: Double = 150000.0,
    totalLimit: Double = 200000.0,
    onBack: () -> Unit = {}
) {
    // Mock Transactions
    // TODO: 실제 날짜값 받아와서 처리하는 방식으로 변경
    val transactions = listOf(
        Transaction("대한항공", "02.14", "14:30", 50000.0, "2026년 2월"),
        Transaction("호텔 신라", "02.12", "09:00", 100000.0, "2026년 2월")
    )

    com.toyprojects.card_pilot.ui.components.GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(categoryName, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
                    navigationIconContentColor = TextPrimary,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            /// Total usage of current category
            item {
                CategoryHeader(
                    description = "혜택 상세 내역입니다.",
                    usedAmount = usedAmount,
                    totalLimit = totalLimit
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            /// Month Selector
            item {
                MonthSelector(
                    currentMonth = "2026년 2월",
                    onMonthSelected = {
                        // TODO: implement logic
                    },
                    availableMonths = listOf("2026년 1월", "2026년 2월")
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            /// Add Item Button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    androidx.compose.material3.OutlinedButton(
                        onClick = {
                            // TODO: implement logic
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            com.toyprojects.card_pilot.ui.theme.Outline
                        ),
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            contentColor = com.toyprojects.card_pilot.ui.theme.Secondary,
                            containerColor = Color.White
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

            /// Month picker
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
                            color = Secondary
                        )
                    }
                }
            } else {
                items(transactions) { item ->
                    TransactionItem(item)
                    HorizontalDivider(
                        color = Gray200,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoryDetailScreenPreview() {
    CardPilotTheme {
        CategoryDetailScreen()
    }
}
