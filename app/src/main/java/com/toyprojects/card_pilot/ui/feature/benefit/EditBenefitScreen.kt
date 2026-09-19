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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.EdgeToEdgeColumn
import com.toyprojects.card_pilot.ui.shared.GlassAlertDialog
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.shared.InputTextField
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBenefitRoute(
    viewModel: EditBenefitViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSave: (BenefitProperty, Int) -> Unit = { _, _ -> },
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is EditBenefitEvent.SaveSuccess -> onSave(event.benefit, event.benefitIndex)
            }
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
        GlassAlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = stringResource(R.string.title_cancel_edit),
            description = stringResource(R.string.msg_cancel_edit),
            confirmText = stringResource(R.string.btn_confirm),
            onConfirm = {
                showCancelDialog = false
                onBack()
            },
            dismissText = stringResource(R.string.btn_cancel),
            onDismiss = { showCancelDialog = false },
            isDestructive = true
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
                title = {
                    Text(
                        stringResource(R.string.title_edit_benefit),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
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
        EdgeToEdgeColumn(
            paddingValues = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InputTextField(
                label = stringResource(R.string.label_benefit_name),
                value = name,
                onValueChange = onNameChange,
                placeholder = stringResource(R.string.hint_benefit_name),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = stringResource(R.string.label_benefit_detail),
                value = explanation,
                onValueChange = onExplanationChange,
                placeholder = stringResource(R.string.hint_benefit_detail),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = stringResource(R.string.label_benefit_limit),
                value = capAmount,
                onValueChange = onAmountChange,
                placeholder = stringResource(R.string.hint_benefit_limit),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                helperText = stringResource(R.string.text_guide_limt),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = stringResource(R.string.label_benefit_rate),
                value = rate,
                onValueChange = onRateChange,
                placeholder = stringResource(R.string.hint_benefit_rate),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = stringResource(R.string.label_max_per_day),
                value = dailyLimit,
                onValueChange = onDailyLimitChange,
                placeholder = stringResource(R.string.hint_max_per_day),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                helperText = stringResource(R.string.text_guide_max),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputTextField(
                label = stringResource(R.string.label_max_per_payment),
                value = oneTimeLimit,
                onValueChange = onOneTimeLimitChange,
                placeholder = stringResource(R.string.hint_max_per_payment),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                helperText = stringResource(R.string.text_guide_max2),
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
                        spotColor = CardPilotColors.primary.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardPilotColors.softAccent,
                    disabledContainerColor = CardPilotColors.gray300
                )
            ) {
                Text(
                    stringResource(R.string.btn_save),
                    style = MaterialTheme.typography.titleMedium.copy(color = CardPilotColors.white)
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
