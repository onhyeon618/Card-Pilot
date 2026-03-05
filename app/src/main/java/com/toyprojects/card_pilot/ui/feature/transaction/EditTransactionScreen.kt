package com.toyprojects.card_pilot.ui.feature.transaction

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.CardSimpleInfo
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.transaction.components.InputItem
import com.toyprojects.card_pilot.ui.feature.transaction.components.TransactionDatePickerDialog
import com.toyprojects.card_pilot.ui.feature.transaction.components.TransactionTimePickerDialog
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.EdgeToEdgeColumn
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.shared.InputTextField
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

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
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = {
                Text(
                    text = "저장하지 않고 나가시겠어요?",
                    style = MaterialTheme.typography.titleMedium,
                    color = CardPilotColors.textPrimary
                )
            },
            text = {
                Text(
                    text = "수정한 내용이 저장되지 않습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CardPilotColors.secondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showExitConfirmation = false
                    onBack()
                }) {
                    Text("나가기", color = CardPilotColors.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmation = false }) {
                    Text("취소", color = CardPilotColors.primary)
                }
            },
            containerColor = CardPilotColors.surface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    val card = uiState.formData.selectedCard?.name ?: "카드 선택"
    val benefit = uiState.formData.selectedBenefit?.name ?: "혜택 선택"

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showCardPicker by remember { mutableStateOf(false) }
    var showBenefitPicker by remember { mutableStateOf(false) }

    // TODO: 기존 항목 수정 진입도 가능하게 변경

    GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (uiState.isEditMode) "지출 항목 수정" else "지출 항목 추가",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            EdgeToEdgeColumn(
                paddingValues = paddingValues,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                /// 금액
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "사용 금액",
                        style = MaterialTheme.typography.labelMedium,
                        color = CardPilotColors.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = amount,
                            onValueChange = { if (it.all { char -> char.isDigit() }) onAmountChange(it) },
                            textStyle = MaterialTheme.typography.displayMedium.copy(color = CardPilotColors.textPrimary),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                            text = "원",
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
                        label = "날짜",
                        value = date,
                        modifier = Modifier.weight(1.5f),
                        onClick = { showDatePicker = true }
                    )
                    InputItem(
                        icon = null,
                        label = "시간",
                        value = time,
                        modifier = Modifier.weight(1f),
                        onClick = { showTimePicker = true }
                    )
                }

                /// 사용처 (거래 내역 이름)
                InputTextField(
                    icon = Icons.Default.ShoppingCart,
                    label = "사용처",
                    value = merchant,
                    onValueChange = onMerchantChange,
                    placeholder = "사용처 입력"
                )

                /// 결제한 카드
                InputItem(
                    icon = Icons.Default.AccountBox,
                    label = "결제 카드",
                    value = card,
                    onClick = {
                        showCardPicker = true
                    }
                )

                /// 지출 항목에 해당하는 카드 혜택
                InputItem(
                    icon = Icons.AutoMirrored.Filled.List,
                    label = "혜택 카테고리",
                    value = benefit,
                    onClick = {
                        if (uiState.formData.selectedCard != null) {
                            showBenefitPicker = true
                        }
                    }
                )

                Spacer(modifier = Modifier.height(100.dp))
            }

            // 카드 선택 모달
            // TODO: 디자인 개선
            if (showCardPicker) {
                Dialog(onDismissRequest = { showCardPicker = false }) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = CardPilotColors.background
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "결제 카드 선택",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            LazyColumn {
                                items(uiState.cards.size) { index ->
                                    val currentCard = uiState.cards[index]
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onCardChange(currentCard)
                                                showCardPicker = false
                                            }
                                            .padding(vertical = 12.dp, horizontal = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .height(30.dp)
                                                .aspectRatio(1.58f)
                                                .background(
                                                    brush = Brush.linearGradient(
                                                        colors = CardPilotColors.pastelGradientColors
                                                    ),
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                        ) {
                                            if (currentCard.image.isNotEmpty()) {
                                                AsyncImage(
                                                    model = currentCard.image,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .clip(RoundedCornerShape(4.dp)),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = currentCard.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = CardPilotColors.textPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 혜택 선택 모달
            // TODO: 디자인 개선
            if (showBenefitPicker) {
                Dialog(onDismissRequest = { showBenefitPicker = false }) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = CardPilotColors.background
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "혜택 카테고리 선택",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            if (uiState.benefits.isEmpty()) {
                                Text(
                                    "등록된 혜택이 없습니다.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CardPilotColors.gray200,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            } else {
                                LazyColumn {
                                    items(uiState.benefits.size) { index ->
                                        val currentBenefit = uiState.benefits[index]
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    onBenefitChange(currentBenefit)
                                                    showBenefitPicker = false
                                                }
                                                .padding(vertical = 12.dp, horizontal = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = currentBenefit.name,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = CardPilotColors.textPrimary
                                            )
                                        }
                                    }
                                }
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

            /// 저장 버튼
            Button(
                onClick = onSaveClick,
                enabled = uiState.isSaveEnabled,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp + paddingValues.calculateBottomPadding())
                    .height(56.dp)
                    .shadow(
                        8.dp,
                        RoundedCornerShape(16.dp),
                        spotColor = CardPilotColors.primary.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardPilotColors.softAccent,
                    disabledContainerColor = CardPilotColors.gray300
                )
            ) {
                Text(
                    if (uiState.isEditMode) "내역 수정하기" else "내역 추가하기",
                    style = MaterialTheme.typography.titleMedium.copy(color = CardPilotColors.white)
                )
            }
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
