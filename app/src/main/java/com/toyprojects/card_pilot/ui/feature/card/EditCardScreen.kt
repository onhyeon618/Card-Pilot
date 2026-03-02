package com.toyprojects.card_pilot.ui.feature.card

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.card.components.BenefitItemRow
import com.toyprojects.card_pilot.ui.navigation.BenefitResult
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.EdgeToEdgeColumn
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCardRoute(
    viewModel: EditCardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    benefitResult: BenefitResult? = null,
    onAddBenefit: () -> Unit = {},
    onEditBenefit: (BenefitProperty, Int) -> Unit = { _, _ -> },
    onBenefitResultConsumed: () -> Unit = {},
    onSave: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCancelDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is EditCardEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    LaunchedEffect(benefitResult) {
        if (benefitResult != null) {
            viewModel.updateBenefit(benefitResult.property, benefitResult.index)
            onBenefitResultConsumed()
        }
    }

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
                    text = "등록 취소",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "카드 등록을 취소하시겠습니까?",
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

    EditCardScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNameChange = viewModel::updateCardName,
        onSaveClick = viewModel::saveCard,
        onAddBenefit = onAddBenefit,
        onEditBenefit = onEditBenefit,
        onRemoveBenefit = viewModel::removeBenefit,
        onMoveBenefit = viewModel::moveBenefit,
        onBack = handleBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCardScreen(
    uiState: EditCardUiState,
    snackbarHostState: SnackbarHostState,
    onNameChange: (String) -> Unit = {},
    onAddBenefit: () -> Unit = {},
    onEditBenefit: (BenefitProperty, Int) -> Unit = { _, _ -> },
    onRemoveBenefit: (Int) -> Unit = {},
    onMoveBenefit: (Int, Int) -> Unit = { _, _ -> },
    onSaveClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val cardName = uiState.formData.cardName
    val benefits = uiState.formData.benefits

    val lazyListState = rememberLazyListState()

    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onMoveBenefit(from.index - 2, to.index - 2) // 헤더 2개 제외한 인덱스 계산
    }

    if (uiState.isError) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "카드를 불러오는데 실패했습니다.", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack) {
                    Text("뒤로 가기")
                }
            }
        }
        return
    }

    GlassScaffold(
        topBar = {
            TopAppBar(
                title = { },
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = CardPilotColors.Primary,
                    contentColor = CardPilotColors.White,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = data.visuals.message,
                            color = CardPilotColors.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box {
            EdgeToEdgeColumn(
                paddingValues = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        item {
                            Column {
                                if (!uiState.isEdit) {
                                    Text(
                                        text = "새로운 카드 등록",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = CardPilotColors.TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                }

                                /// 카드 박스
                                // TODO: use card image as background
                                CardPilotRipple(color = CardPilotColors.White) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .shadow(
                                                elevation = 8.dp,
                                                shape = RoundedCornerShape(16.dp),
                                                spotColor = Color(0x33000000)
                                            )
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = CardPilotColors.PastelGradientColors
                                                )
                                            )
                                            .clickable {
                                                // TODO: implement image pick logic
                                            }
                                            .padding(24.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Bottom
                                        ) {
                                            /// 카드 이름 입력창
                                            Column {
                                                Text(
                                                    text = "CARD NAME",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = CardPilotColors.Violet800
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                BasicTextField(
                                                    value = cardName,
                                                    onValueChange = onNameChange,
                                                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                                                        color = CardPilotColors.Violet900
                                                    ),
                                                    decorationBox = { innerTextField ->
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .background(
                                                                    color = CardPilotColors.White.copy(
                                                                        alpha = 0.2f
                                                                    ),
                                                                    shape = RoundedCornerShape(2.dp)
                                                                )
                                                                .padding(
                                                                    horizontal = 8.dp,
                                                                    vertical = 4.dp
                                                                ),
                                                            contentAlignment = Alignment.CenterStart
                                                        ) {
                                                            if (cardName.isEmpty()) {
                                                                Text(
                                                                    text = "카드 이름 입력",
                                                                    style = MaterialTheme.typography.headlineSmall,
                                                                    color = CardPilotColors.Secondary
                                                                )
                                                            }
                                                            innerTextField()
                                                        }
                                                    },
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "혜택",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = CardPilotColors.TextPrimary
                                )

                                /// 혜택 추가하기 버튼
                                CardPilotRipple(color = CardPilotColors.GradientPeach) {
                                    FilledTonalButton(
                                        onClick = {
                                            onAddBenefit()
                                        },
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = CardPilotColors.SurfaceCard,
                                            contentColor = CardPilotColors.TextPrimary
                                        ),
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(
                                            horizontal = 12.dp,
                                            vertical = 8.dp
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("추가", style = MaterialTheme.typography.labelLarge)
                                    }
                                }
                            }
                        }

                        /// 혜택 목록
                        itemsIndexed(
                            items = benefits,
                            key = { _, benefit -> benefit.clientId }
                        ) { index, benefit ->
                            ReorderableItem(
                                state = reorderableState,
                                key = benefit.clientId,
                            ) { isDragging ->
                                val modifier = Modifier.graphicsLayer {
                                    scaleX = if (isDragging) 1.05f else 1f
                                    scaleY = if (isDragging) 1.05f else 1f
                                    alpha = if (isDragging) 0.9f else 1f
                                }

                                BenefitItemRow(
                                    modifier = modifier,
                                    name = benefit.name,
                                    description = benefit.explanation ?: "",
                                    onClick = {
                                        onEditBenefit(benefit, index)
                                    },
                                    onDelete = {
                                        onRemoveBenefit(index)
                                    }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }

                /// 카드 저장 버튼 (화면 하단 고정)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onSaveClick,
                    enabled = !uiState.isSaving && uiState.isFormValid,
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
                        disabledContainerColor = CardPilotColors.Gray300
                    )
                ) {
                    Text(
                        if (uiState.isEdit) "카드 저장" else "카드 등록",
                        style = MaterialTheme.typography.titleMedium.copy(color = CardPilotColors.White)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CardPilotColors.Primary)
            }
        }
    }
}

@Preview
@Composable
fun EditCardScreenPreview() {
    CardPilotTheme {
        EditCardScreen(
            uiState = EditCardUiState(),
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
