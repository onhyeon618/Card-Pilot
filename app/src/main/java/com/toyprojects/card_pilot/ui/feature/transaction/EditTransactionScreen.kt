package com.toyprojects.card_pilot.ui.feature.transaction

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.transaction.components.InputItem
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

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            onSave()
        }
    }

    EditTransactionScreen(
        uiState = uiState,
        onAmountChange = viewModel::updateAmount,
        onDateChange = viewModel::updateDate,
        onTimeChange = viewModel::updateTime,
        onMerchantChange = viewModel::updateMerchant,
        onCardChange = viewModel::updateCard,
        onBenefitChange = viewModel::updateBenefit,
        onSaveClick = viewModel::saveTransaction,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    uiState: EditTransactionUiState,
    onAmountChange: (String) -> Unit = {},
    onDateChange: (String) -> Unit = {},
    onTimeChange: (String) -> Unit = {},
    onMerchantChange: (String) -> Unit = {},
    onCardChange: (String) -> Unit = {},
    onBenefitChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val amount = uiState.amount
    val date = uiState.date
    val time = uiState.time
    val merchant = uiState.merchant
    val card = uiState.card
    val benefit = uiState.benefit

    // TODO: 기존 항목 수정 진입도 가능하게 변경

    GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("지출 항목 추가", style = MaterialTheme.typography.titleLarge) },
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
                        color = CardPilotColors.Secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = amount,
                            onValueChange = { if (it.all { char -> char.isDigit() }) onAmountChange(it) },
                            textStyle = MaterialTheme.typography.displayMedium.copy(color = CardPilotColors.TextPrimary),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            decorationBox = { innerTextField ->
                                if (amount.isEmpty()) {
                                    Text(
                                        text = "0",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = CardPilotColors.Gray200
                                    )
                                }
                                innerTextField()
                            }
                        )
                        Text(
                            text = "원",
                            style = MaterialTheme.typography.headlineSmall,
                            color = CardPilotColors.TextPrimary,
                            modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                        )
                    }
                }

                /// 날짜 및 시각
                // TODO: 실제 동작 구현 - 피커를 열지 텍스트를 입력받을지도 결정 필요
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

                /// 사용처 (거래 내역 이름)
                InputTextField(
                    icon = Icons.Default.ShoppingCart,
                    label = "사용처",
                    value = merchant,
                    onValueChange = onMerchantChange,
                    placeholder = "사용처 입력"
                )

                /// 결제한 카드
                // TODO: 카드 목록 피커 구현
                InputItem(
                    icon = Icons.Default.AccountBox,
                    label = "결제 카드",
                    value = card,
                    onClick = {
                        // TODO: implement logic
                    }
                )

                /// 지출 항목에 해당하는 카드 혜택
                // TODO: 혜택 목록 피커 구현
                InputItem(
                    icon = Icons.AutoMirrored.Filled.List,
                    label = "카테고리",
                    value = benefit,
                    onClick = {
                        // TODO: implement logic
                    }
                )

                Spacer(modifier = Modifier.height(100.dp))
            }

            /// 저장 버튼
            Button(
                onClick = onSaveClick,
                // TODO: enable 로직 개선 - 혜택과 카테고리 선택 여부도 체크 필요
                enabled = !uiState.isSaving && amount.isNotBlank() && merchant.isNotBlank(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp + paddingValues.calculateBottomPadding())
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
                    "내역 추가하기",
                    style = MaterialTheme.typography.titleMedium.copy(color = CardPilotColors.White)
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
