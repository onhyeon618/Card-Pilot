package com.toyprojects.card_pilot.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.components.Benefit
import com.toyprojects.card_pilot.ui.components.BenefitTracker
import com.toyprojects.card_pilot.ui.components.CardDropdown
import com.toyprojects.card_pilot.ui.components.CardUsageSummary
import com.toyprojects.card_pilot.ui.components.MonthSelector
import com.toyprojects.card_pilot.ui.theme.Background
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import com.toyprojects.card_pilot.ui.theme.TextPrimary

@Composable
fun MainScreen() {
    // Mock Data
    val selectedCard = "현대카드 The Red"
    val cardList = listOf("현대카드 The Red", "삼성카드 taptap O", "신한카드 Mr.Life")
    val benefits = listOf(
        Benefit("바우처 (여행/호텔)", 150000.0, 200000.0, "항공권 및 호텔 예약 시 사용 가능"),
        Benefit("PP카드 라운지", 2.0, 10.0), // usage count
        Benefit("메탈 플레이트 발급", 1.0, 1.0, "발급 수수료 면제")
    )
    val usageAmount = 1250450.0

    Scaffold(
        topBar = {
            TopAppBar()
        },
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            /// Card selection dropdown
            CardDropdown(
                selectedCard = selectedCard,
                cardList = cardList,
                onCardSelected = {
                    // TODO: implement logic
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            MonthSelector(
                currentMonth = "2026년 2월",
                onMonthSelected = {
                    // TODO: implement logic
                },
                availableMonths = listOf("2026년 1월", "2026년 2월")
            )
            Spacer(modifier = Modifier.height(12.dp))

            /// Total usage of selected card
            CardUsageSummary(
                usedAmount = usageAmount
            )

            Spacer(modifier = Modifier.height(48.dp))

            /// Benefit tracker of selected card
            BenefitTracker(benefits = benefits)

            Spacer(modifier = Modifier.height(32.dp))

            /// Add benefit category button
            androidx.compose.material3.TextButton(
                onClick = {
                    // TODO: implement logic
                },
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                    contentColor = com.toyprojects.card_pilot.ui.theme.Secondary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "혜택 추가",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: create and apply app logo
        Text(
            text = "CardPilot",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        IconButton(
            onClick = {
                // TODO: Open Settings
            }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "설정",
                tint = TextPrimary
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun MainScreenPreview() {
    CardPilotTheme {
        MainScreen()
    }
}
