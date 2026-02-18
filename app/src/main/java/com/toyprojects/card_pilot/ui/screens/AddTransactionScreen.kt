package com.toyprojects.card_pilot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.components.InputItem
import com.toyprojects.card_pilot.ui.components.InputTextField
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import com.toyprojects.card_pilot.ui.theme.Gray200
import com.toyprojects.card_pilot.ui.theme.Primary
import com.toyprojects.card_pilot.ui.theme.Secondary
import com.toyprojects.card_pilot.ui.theme.SoftSlateIndigo
import com.toyprojects.card_pilot.ui.theme.TextPrimary
import com.toyprojects.card_pilot.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("2026.02.14") }
    var time by remember { mutableStateOf("18:30") }
    var merchant by remember { mutableStateOf("") }
    var card by remember { mutableStateOf("CardPilot Visa") }
    var benefit by remember { mutableStateOf("혜택 선택") }

    com.toyprojects.card_pilot.ui.components.GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("지출 항목 추가", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = TextPrimary,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 100.dp), // Extra padding for button
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                /// Amount
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "사용 금액",
                        style = MaterialTheme.typography.labelMedium,
                        color = Secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = amount,
                            onValueChange = { if (it.all { char -> char.isDigit() }) amount = it },
                            textStyle = MaterialTheme.typography.displayMedium.copy(color = TextPrimary),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            decorationBox = { innerTextField ->
                                if (amount.isEmpty()) {
                                    Text(
                                        text = "0",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = Gray200
                                    )
                                }
                                innerTextField()
                            }
                        )
                        Text(
                            text = "원",
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary,
                            modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                        )
                    }
                }

                /// Transaction date and time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InputItem(
                        icon = Icons.Default.DateRange,
                        label = "날짜",
                        value = date,
                        modifier = Modifier.weight(1.5f),
                        onClick = { /* Open Date Picker */ }
                    )
                    InputItem(
                        icon = null,
                        label = "시간",
                        value = time,
                        modifier = Modifier.weight(1f),
                        onClick = { /* Open Time Picker */ }
                    )
                }

                /// Transaction detail
                InputTextField(
                    icon = Icons.Default.ShoppingCart,
                    label = "사용처",
                    value = merchant,
                    onValueChange = { merchant = it },
                    placeholder = "사용처 입력"
                )

                /// Card
                InputItem(
                    icon = Icons.Default.AccountBox,
                    label = "결제 카드",
                    value = card,
                    onClick = {
                        // TODO: implement logic
                    }
                )

                /// Benefit
                InputItem(
                    icon = Icons.AutoMirrored.Filled.List,
                    label = "카테고리",
                    value = benefit,
                    onClick = {
                        // TODO: implement logic
                    }
                )
            }

            /// Save Button
            Button(
                onClick = onSave,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = Primary.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftSlateIndigo,
                    contentColor = White
                )
            ) {
                Text("내역 추가하기", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
            }
        }
    }
}

@Preview
@Composable
fun AddTransactionScreenPreview() {
    CardPilotTheme {
        AddTransactionScreen()
    }
}
