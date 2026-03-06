package com.toyprojects.card_pilot.ui.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.model.ThemeType
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.settings.components.SettingsRow
import com.toyprojects.card_pilot.ui.feature.settings.components.SettingsSection
import com.toyprojects.card_pilot.ui.feature.settings.components.ThemeSelectDialog
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.EdgeToEdgeColumn
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    currentTheme: ThemeType,
    onBack: () -> Unit,
    onCardListClick: () -> Unit,
    onAddCardClick: () -> Unit
) {
    val keepSelectedCard by viewModel.keepSelectedCard.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SettingsViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = androidx.compose.material3.SnackbarDuration.Short
                    )
                }
            }
        }
    }

    SettingsScreen(
        currentTheme = currentTheme,
        snackbarHostState = snackbarHostState,
        onThemeSelected = viewModel::updateTheme,
        keepSelectedCard = keepSelectedCard,
        setKeepSelectedCard = viewModel::setKeepSelectedCard,
        onBack = onBack,
        onCardListClick = onCardListClick,
        onAddCardClick = onAddCardClick,
        onResetDataClick = viewModel::clearAllData
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentTheme: ThemeType = ThemeType.PURPLE,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onThemeSelected: (ThemeType) -> Unit = {},
    keepSelectedCard: Boolean = false,
    setKeepSelectedCard: (Boolean) -> Unit = {},
    onBack: () -> Unit = {},
    onCardListClick: () -> Unit = {},
    onAddCardClick: () -> Unit = {},
    onResetDataClick: () -> Unit = {}
) {
    val colors = CardPilotColors
    var showThemeDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    if (showThemeDialog) {
        ThemeSelectDialog(
            currentTheme = currentTheme,
            onThemeSelected = { themeType ->
                onThemeSelected(themeType)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = "데이터 초기화",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "모든 데이터가 삭제되며 복구할 수 없습니다.\n정말 초기화하시겠습니까?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onResetDataClick()
                        showResetDialog = false
                    }
                ) {
                    Text("초기화", color = colors.primary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                    }
                ) {
                    Text("취소", color = colors.textPrimary)
                }
            },
            containerColor = colors.background,
            titleContentColor = colors.textPrimary,
            textContentColor = colors.textSecondary
        )
    }

    GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "설정",
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
                    navigationIconContentColor = colors.textPrimary,
                    titleContentColor = colors.textPrimary
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
        EdgeToEdgeColumn(
            paddingValues = paddingValues,
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            /// Ad
            // TODO: Add ad
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.gray50)
                    .border(
                        1.dp,
                        colors.outline,
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "광고 영역",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.gray300
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            /// 카드 관리 섹션
            SettingsSection(title = "카드 관리") {
                SettingsRow(
                    label = "내 카드 목록",
                    onClick = {
                        onCardListClick()
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "카드 추가",
                    onClick = {
                        onAddCardClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /// 일반 설정 섹션
            SettingsSection(title = "일반") {
                SettingsRow(
                    label = "테마 색상",
                    valueWidget = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(colors.backgroundGradientColors))
                        )
                    },
                    onClick = {
                        showThemeDialog = true
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "지출 알림 자동 수신",
                    value = "꺼짐",
                    onClick = {
                        // TODO
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "선택한 카드 유지",
                    value = if (keepSelectedCard) "켜짐" else "꺼짐",
                    onClick = {
                        setKeepSelectedCard(!keepSelectedCard)
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "데이터 초기화",
                    onClick = {
                        showResetDialog = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /// 앱 정보 섹션
            SettingsSection(title = "정보") {
                SettingsRow(
                    label = "앱 버전",
                    value = "1.0.0", // TODO: apply real version
                    showArrow = false
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "오픈소스 라이선스",
                    onClick = {
                        // TODO
                    }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            /// Footer
            // TODO: use app icon
            Text(
                text = "CardPilot",
                style = MaterialTheme.typography.bodySmall,
                color = colors.secondary
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    CardPilotTheme {
        SettingsScreen(
            onBack = {},
            onCardListClick = {},
            onAddCardClick = {},
            onResetDataClick = {}
        )
    }
}
