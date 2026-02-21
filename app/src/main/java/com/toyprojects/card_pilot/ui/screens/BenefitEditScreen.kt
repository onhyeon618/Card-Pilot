package com.toyprojects.card_pilot.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.components.CardPilotRipple
import com.toyprojects.card_pilot.ui.components.EdgeToEdgeColumn
import com.toyprojects.card_pilot.ui.components.GlassScaffold
import com.toyprojects.card_pilot.ui.components.InputTextField
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BenefitEditScreen(
    onBack: () -> Unit = {},
    onSave: (String, String, String?, String?, String?) -> Unit = { _, _, _, _, _ -> }
) {
    var name by remember { mutableStateOf("혜택 이름") }
    var amount by remember { mutableStateOf("200000") }
    var dailyLimit by remember { mutableStateOf("") }
    var oneTimeLimit by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }

    GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("혜택 정보 편집", style = MaterialTheme.typography.titleLarge) },
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
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            InputTextField(
                label = "혜택 이름",
                value = name,
                onValueChange = { name = it },
                placeholder = "예: 여행, 주유, 마트",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "적립/할인 총 한도 (원)",
                value = amount,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        amount = it
                    }
                },
                placeholder = "예: 150000",
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "적립/할인 일일 한도 (원)",
                value = dailyLimit,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        dailyLimit = it
                    }
                },
                placeholder = "예: 10000",
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "적립/할인 1회 한도 (원)",
                value = oneTimeLimit,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        oneTimeLimit = it
                    }
                },
                placeholder = "예: 5000",
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "적립/할인율 (%)",
                value = rate,
                onValueChange = {
                    // Allow digits and one decimal point
                    if (it.count { char -> char == '.' } <= 1 && it.all { char -> char.isDigit() || char == '.' }) {
                        rate = it
                    }
                },
                placeholder = "예: 1.5",
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onSave(
                        name,
                        amount,
                        dailyLimit.ifBlank { null },
                        oneTimeLimit.ifBlank { null },
                        rate.ifBlank { null })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        8.dp,
                        RoundedCornerShape(16.dp),
                        spotColor = CardPilotColors.Primary.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardPilotColors.SoftSlateIndigo,
                    contentColor = CardPilotColors.White
                ),
                enabled = name.isNotBlank() && amount.isNotBlank()
            ) {
                Text(
                    "저장하기",
                    style = MaterialTheme.typography.titleMedium.copy(color = CardPilotColors.White)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
fun BenefitEditScreenPreview() {
    CardPilotTheme {
        BenefitEditScreen()
    }
}
