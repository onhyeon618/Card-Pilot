package com.toyprojects.card_pilot.ui.feature.benefit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            onSave()
        }
    }

    val handleBack = remember(uiState.isModified, onBack) {
        {
            if (uiState.isModified) {
                showCancelDialog = true
            } else {
                onBack()
            }
        }
    }

    BackHandler(enabled = true, onBack = handleBack)

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = {
                Text(
                    text = "작성 취소",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "작성을 취소하시겠습니까?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showCancelDialog = false
                    onBack()
                }) {
                    Text("확인", color = CardPilotColors.SoftSlateIndigo)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("취소", color = CardPilotColors.TextSecondary)
                }
            },
            containerColor = CardPilotColors.White,
            titleContentColor = CardPilotColors.TextPrimary,
            textContentColor = CardPilotColors.TextSecondary
        )
    }

    EditBenefitScreen(
        uiState = uiState,
        onNameChange = viewModel::updateName,
        onExplanationChange = viewModel::updateExplanation,
        onAmountChange = viewModel::updateAmount,
        onDailyLimitChange = viewModel::updateDailyLimit,
        onOneTimeLimitChange = viewModel::updateOneTimeLimit,
        onRateChange = viewModel::updateRate,
        onSaveClick = viewModel::saveBenefit,
        onBack = handleBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBenefitScreen(
    uiState: EditBenefitUiState,
    onNameChange: (String) -> Unit = {},
    onExplanationChange: (String) -> Unit = {},
    onAmountChange: (String) -> Unit = {},
    onDailyLimitChange: (String) -> Unit = {},
    onOneTimeLimitChange: (String) -> Unit = {},
    onRateChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val name = uiState.formData.name
    val explanation = uiState.formData.explanation
    val capAmount = uiState.formData.capAmount
    val dailyLimit = uiState.formData.dailyLimit
    val oneTimeLimit = uiState.formData.oneTimeLimit
    val rate = uiState.formData.rate

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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InputTextField(
                label = "혜택 이름 *",
                value = name,
                onValueChange = onNameChange,
                placeholder = "예: 여행, 주유, 마트",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "상세 설명",
                value = explanation,
                onValueChange = onExplanationChange,
                placeholder = "예: 국내외 여행 시 1.5% 적립",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "적립/할인 한도 *",
                value = capAmount,
                onValueChange = onAmountChange,
                placeholder = "예: 150000",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "적립/할인율 (%) *",
                value = rate,
                onValueChange = onRateChange,
                placeholder = "예: 1.5",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "1일 최대 적용 이용금액 (원)",
                value = dailyLimit,
                onValueChange = onDailyLimitChange,
                placeholder = "예: 10000",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = "1회 최대 적용 이용금액 (원)",
                value = oneTimeLimit,
                onValueChange = onOneTimeLimitChange,
                placeholder = "예: 5000",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onSaveClick,
                enabled = !uiState.isSaving && uiState.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = if (uiState.isFormValid) 8.dp else 0.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = CardPilotColors.Primary.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardPilotColors.SoftSlateIndigo,
                    disabledContainerColor = CardPilotColors.Gray300
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
