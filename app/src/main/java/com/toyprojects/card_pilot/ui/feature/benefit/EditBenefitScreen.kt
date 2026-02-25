package com.toyprojects.card_pilot.ui.feature.benefit

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.EdgeToEdgeColumn
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.shared.InputTextField
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBenefitRoute(
    viewModel: EditBenefitViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSave: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            onSave()
        }
    }

    EditBenefitScreen(
        uiState = uiState,
        onNameChange = viewModel::updateName,
        onAmountChange = viewModel::updateAmount,
        onDailyLimitChange = viewModel::updateDailyLimit,
        onOneTimeLimitChange = viewModel::updateOneTimeLimit,
        onRateChange = viewModel::updateRate,
        onSaveClick = viewModel::saveBenefit,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBenefitScreen(
    uiState: EditBenefitUiState,
    onNameChange: (String) -> Unit = {},
    onAmountChange: (String) -> Unit = {},
    onDailyLimitChange: (String) -> Unit = {},
    onOneTimeLimitChange: (String) -> Unit = {},
    onRateChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val name = uiState.name
    val amount = uiState.amount
    val dailyLimit = uiState.dailyLimit
    val oneTimeLimit = uiState.oneTimeLimit
    val rate = uiState.rate

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
                label = "혜택 이름*",
                value = name,
                onValueChange = onNameChange,
                placeholder = "예: 여행, 주유, 마트",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "적립/할인 총 한도 (원)*",
                value = amount,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        onAmountChange(it)
                    }
                },
                placeholder = "예: 150000",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "적립/할인 일일 한도 (원)",
                value = dailyLimit,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        onDailyLimitChange(it)
                    }
                },
                placeholder = "예: 10000",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "적립/할인 1회 한도 (원)",
                value = oneTimeLimit,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        onOneTimeLimitChange(it)
                    }
                },
                placeholder = "예: 5000",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "적립/할인율 (%)*",
                value = rate,
                onValueChange = {
                    // 숫자와 소수점(1개) 허용
                    if (it.count { char -> char == '.' } <= 1 && it.all { char -> char.isDigit() || char == '.' }) {
                        onRateChange(it)
                    }
                },
                placeholder = "예: 1.5",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSaveClick,
                enabled = !uiState.isSaving && name.isNotBlank() && amount.isNotBlank() && rate.isNotBlank(),
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
                )
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
fun EditBenefitScreenPreview() {
    CardPilotTheme {
        EditBenefitScreen(
            uiState = EditBenefitUiState()
        )
    }
}
