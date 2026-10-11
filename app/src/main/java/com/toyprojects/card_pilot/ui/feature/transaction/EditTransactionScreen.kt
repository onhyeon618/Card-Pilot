package com.toyprojects.card_pilot.ui.feature.transaction

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.CardSimpleInfo
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.transaction.components.BenefitPickerItem
import com.toyprojects.card_pilot.ui.feature.transaction.components.CardPickerItem
import com.toyprojects.card_pilot.ui.feature.transaction.components.InputItem
import com.toyprojects.card_pilot.ui.feature.transaction.components.TransactionDatePickerDialog
import com.toyprojects.card_pilot.ui.feature.transaction.components.TransactionTimePickerDialog
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.CurrencyVisualTransformation
import com.toyprojects.card_pilot.ui.shared.GlassAlertDialog
import com.toyprojects.card_pilot.ui.shared.GlassBottomSheet
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.shared.InputTextField
import com.toyprojects.card_pilot.ui.shared.PrimaryFormButton
import com.toyprojects.card_pilot.ui.shared.StickyFooterFormLayout
import com.toyprojects.card_pilot.ui.shared.rememberKeyboardDismissLauncher
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionRoute(
    viewModel: EditTransactionViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSave: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is EditTransactionEvent.SaveSuccess -> onSave()
                is EditTransactionEvent.SaveError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    var showExitConfirmation by remember { mutableStateOf(false) }

    val handleBack = {
        if (uiState.isModified) {
            showExitConfirmation = true
        } else {
            onBack()
        }
    }

    BackHandler {
        handleBack()
    }

    EditTransactionScreen(
        uiState = uiState,
        onAmountChange = viewModel::updateAmount,
        onDateChange = viewModel::updateDate,
        onTimeChange = viewModel::updateTime,
        onMerchantChange = viewModel::updateMerchant,
        onCardChange = { viewModel.updateCard(it) },
        onBenefitChange = { viewModel.updateBenefit(it) },
        onSaveClick = viewModel::saveTransaction,
        onBack = handleBack,
        snackbarHostState = snackbarHostState
    )

    if (showExitConfirmation) {
        GlassAlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = stringResource(R.string.msg_leave_without_saving),
            description = stringResource(R.string.msg_changes_not_saved),
            confirmText = stringResource(R.string.btn_exit),
            onConfirm = {
                showExitConfirmation = false
                onBack()
            },
            dismissText = stringResource(R.string.btn_cancel),
            onDismiss = { showExitConfirmation = false },
            isDestructive = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditTransactionScreen(
    uiState: EditTransactionUiState,
    onAmountChange: (String) -> Unit = {},
    onDateChange: (String) -> Unit = {},
    onTimeChange: (String) -> Unit = {},
    onMerchantChange: (String) -> Unit = {},
    onCardChange: (CardSimpleInfo) -> Unit = {},
    onBenefitChange: (BenefitProperty) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onBack: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val amount = uiState.formData.amount
    val date = uiState.formData.date
    val time = uiState.formData.time
    val merchant = uiState.formData.merchant
    val card = uiState.formData.selectedCard?.name ?: stringResource(R.string.text_select_card)
    val benefit = uiState.formData.selectedBenefit?.name ?: stringResource(R.string.text_select_benefit)

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showCardPicker by remember { mutableStateOf(false) }
    var showBenefitPicker by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val cardSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val benefitSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val bottomSheetLauncher = rememberKeyboardDismissLauncher()

    GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (uiState.isEditMode) stringResource(R.string.title_edit_payment) else stringResource(R.string.title_add_payment),
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = CardPilotColors.primary,
                    contentColor = CardPilotColors.white,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = data.visuals.message,
                            color = CardPilotColors.white
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        StickyFooterFormLayout(
            contentPadding = paddingValues,
            footerContent = {
                PrimaryFormButton(
                    text = if (uiState.isEditMode) stringResource(R.string.btn_edit_payment) else stringResource(R.string.btn_add_payment),
                    onClick = onSaveClick,
                    enabled = uiState.isSaveEnabled
                )
            }
        ) {

            /// 금액
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                val amountLabel = stringResource(R.string.label_amount)

                Text(
                    text = amountLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = CardPilotColors.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = amount,
                        onValueChange = { incoming ->
                            val pureDigits = incoming.filter { it in '0'..'9' }
                            onAmountChange(pureDigits)
                        },
                        textStyle = MaterialTheme.typography.displayMedium.copy(color = CardPilotColors.textPrimary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = CurrencyVisualTransformation(),
                        decorationBox = { innerTextField ->
                            if (amount.isEmpty()) {
                                Text(
                                    text = "0",
                                    style = MaterialTheme.typography.displayMedium,
                                    color = CardPilotColors.gray200
                                )
                            }
                            innerTextField()
                        }
                    )
                    Text(
                        text = stringResource(R.string.text_won),
                        style = MaterialTheme.typography.headlineSmall,
                        color = CardPilotColors.textPrimary,
                        modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                    )
                }
            }

            /// 날짜 및 시각
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InputItem(
                    icon = Icons.Default.DateRange,
                    label = stringResource(R.string.label_date),
                    value = date,
                    modifier = Modifier.weight(1.5f),
                    onClick = {
                        showDatePicker = true
                    }
                )
                InputItem(
                    icon = null,
                    label = stringResource(R.string.label_time),
                    value = time,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showTimePicker = true
                    }
                )
            }

            /// 사용처 (거래 내역 이름)
            InputTextField(
                icon = Icons.Default.ShoppingCart,
                label = stringResource(R.string.label_merchant),
                value = merchant,
                onValueChange = onMerchantChange,
                placeholder = stringResource(R.string.hint_merchant),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            /// 결제한 카드
            InputItem(
                icon = Icons.Default.AccountBox,
                label = stringResource(R.string.label_card),
                value = card,
                onClick = {
                    bottomSheetLauncher.launch {
                        showCardPicker = true
                    }
                }
            )

            /// 지출 항목에 해당하는 카드 혜택
            InputItem(
                icon = Icons.AutoMirrored.Filled.List,
                label = stringResource(R.string.label_benefit),
                value = benefit,
                onClick = {
                    if (uiState.formData.selectedCard != null) {
                        bottomSheetLauncher.launch {
                            showBenefitPicker = true
                        }
                    }
                }
            )
        }

        /// 카드 선택 모달
        if (showCardPicker) {
            GlassBottomSheet(
                onDismissRequest = { showCardPicker = false },
                sheetState = cardSheetState
            ) {
                Text(
                    text = stringResource(R.string.title_select_card),
                    style = MaterialTheme.typography.titleLarge,
                    color = CardPilotColors.textPrimary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = uiState.cards.size,
                        key = { index -> uiState.cards[index].id }
                    ) { index ->
                        CardPickerItem(
                            card = uiState.cards[index],
                            isSelected = uiState.cards[index].id == uiState.formData.selectedCard?.id,
                            onClick = {
                                onCardChange(it)
                                scope.launch {
                                    cardSheetState.hide()
                                    showCardPicker = false
                                }
                            }
                        )
                    }
                }
            }
        }

        /// 혜택 선택 모달
        if (showBenefitPicker) {
            GlassBottomSheet(
                onDismissRequest = { showBenefitPicker = false },
                sheetState = benefitSheetState
            ) {
                Text(
                    text = stringResource(R.string.title_select_benefit),
                    style = MaterialTheme.typography.titleLarge,
                    color = CardPilotColors.textPrimary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                if (uiState.benefits.isEmpty()) {
                    Text(
                        text = stringResource(R.string.msg_no_benefit),
                        style = MaterialTheme.typography.bodyLarge,
                        color = CardPilotColors.textSecondary,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            count = uiState.benefits.size,
                            key = { index -> uiState.benefits[index].id }
                        ) { index ->
                            BenefitPickerItem(
                                benefit = uiState.benefits[index],
                                isSelected = uiState.benefits[index].id == uiState.formData.selectedBenefit?.id,
                                onClick = {
                                    onBenefitChange(it)
                                    scope.launch {
                                        benefitSheetState.hide()
                                        showBenefitPicker = false
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        /// 날짜 선택 다이얼로그
        if (showDatePicker) {
            TransactionDatePickerDialog(
                date = date,
                onDateChange = onDateChange,
                onDismiss = { showDatePicker = false }
            )
        }

        /// 시간 선택 다이얼로그
        if (showTimePicker) {
            TransactionTimePickerDialog(
                time = time,
                onTimeChange = onTimeChange,
                onDismiss = { showTimePicker = false }
            )
        }
    }
}

@Preview
@Composable
fun EditTransactionScreenPreview() {
    CardPilotTheme {
        EditTransactionScreen(
            uiState = EditTransactionUiState()
        )
    }
}
