package com.toyprojects.card_pilot.ui.feature.settings

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.model.ThemeType
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.settings.components.SettingsRow
import com.toyprojects.card_pilot.ui.feature.settings.components.SettingsSection
import com.toyprojects.card_pilot.ui.feature.settings.components.ThemeSelectDialog
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.EdgeToEdgeColumn
import com.toyprojects.card_pilot.ui.shared.GlassAlertDialog
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

enum class PendingGoogleAuthAction { BACKUP, RESTORE, NONE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    currentTheme: ThemeType,
    onBack: () -> Unit,
    onCardListClick: () -> Unit,
    onAddCardClick: () -> Unit,
    onNotificationSettingsClick: () -> Unit
) {
    val notiReceiveEnabled by viewModel.notiReceiveEnabled.collectAsStateWithLifecycle()
    val keepSelectedCard by viewModel.keepSelectedCard.collectAsStateWithLifecycle()
    val isUpdateAvailable = viewModel.isUpdateAvailable
    val googleAccountEmail = viewModel.googleAccountEmail

    val isLoading = viewModel.isLoading
    val loadingMessage = viewModel.loadingMessage

    val snackbarHostState = remember { SnackbarHostState() }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.handleSignInResult(result.data)
        } else {
            viewModel.handleSignInResult(null)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SettingsViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = androidx.compose.material3.SnackbarDuration.Short
                    )
                }

                is SettingsViewModel.UiEvent.LaunchGoogleSignIn -> {
                    try {
                        googleSignInLauncher.launch(event.intent)
                    } catch (_: ActivityNotFoundException) {
                        snackbarHostState.showSnackbar("구글 서비스에 연결할 수 없습니다.")
                    }
                }
            }
        }
    }

    SettingsScreen(
        currentTheme = currentTheme,
        snackbarHostState = snackbarHostState,
        onThemeSelected = viewModel::updateTheme,
        notiReceiveEnabled = notiReceiveEnabled,
        keepSelectedCard = keepSelectedCard,
        setKeepSelectedCard = viewModel::setKeepSelectedCard,
        isUpdateAvailable = isUpdateAvailable,
        googleAccountEmail = googleAccountEmail,
        isLoading = isLoading,
        loadingMessage = loadingMessage,
        onBack = onBack,
        onCardListClick = onCardListClick,
        onAddCardClick = onAddCardClick,
        onNotificationSettingsClick = onNotificationSettingsClick,
        onResetDataClick = viewModel::clearAllData,
        onRequestGoogleSignIn = viewModel::requestGoogleSignIn,
        onBackupDataClick = viewModel::backupToGoogleDrive,
        onRestoreDataClick = viewModel::restoreFromGoogleDrive,
        onSignOutClick = viewModel::signOutFromGoogle
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentTheme: ThemeType = ThemeType.PURPLE,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onThemeSelected: (ThemeType) -> Unit = {},
    notiReceiveEnabled: Boolean = false,
    keepSelectedCard: Boolean = false,
    setKeepSelectedCard: (Boolean) -> Unit = {},
    isUpdateAvailable: Boolean = false,
    googleAccountEmail: String? = null,
    isLoading: Boolean = false,
    loadingMessage: String? = null,
    onBack: () -> Unit = {},
    onCardListClick: () -> Unit = {},
    onAddCardClick: () -> Unit = {},
    onNotificationSettingsClick: () -> Unit = {},
    onResetDataClick: () -> Unit = {},
    onRequestGoogleSignIn: () -> Unit = {},
    onBackupDataClick: () -> Unit = {},
    onRestoreDataClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {}
) {
    val colors = CardPilotColors
    var showThemeDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showBackupDialog by remember { mutableStateOf(false) }
    var showRestoreDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showSignInDialog by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf(PendingGoogleAuthAction.NONE) }
    val context = LocalContext.current

    LaunchedEffect(googleAccountEmail) {
        if (googleAccountEmail != null) {
            when (pendingAction) {
                PendingGoogleAuthAction.BACKUP -> showBackupDialog = true
                PendingGoogleAuthAction.RESTORE -> showRestoreDialog = true
                PendingGoogleAuthAction.NONE -> {}
            }
            pendingAction = PendingGoogleAuthAction.NONE
        } else {
            pendingAction = PendingGoogleAuthAction.NONE
        }
    }

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
        GlassAlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = "데이터 초기화",
            description = "모든 데이터가 삭제되며 복구할 수 없습니다.\n정말 초기화하시겠습니까?",
            confirmText = "초기화",
            onConfirm = {
                onResetDataClick()
                showResetDialog = false
            },
            dismissText = "취소",
            onDismiss = { showResetDialog = false },
            isDestructive = true
        )
    }

    if (showBackupDialog) {
        if (googleAccountEmail == null) {
            GlassAlertDialog(
                onDismissRequest = { showBackupDialog = false },
                title = "구글 로그인 필요",
                description = "데이터 백업을 위해 구글 로그인이 필요합니다.\n\n※ 구글 계정은 백업 및 복원에만 사용됩니다.\n\n※ 연동 후 자동 동기화되지 않으며, 사용자가 원할 때만 데이터가 저장/복원됩니다.",
                confirmText = "로그인",
                onConfirm = {
                    pendingAction = PendingGoogleAuthAction.BACKUP
                    onRequestGoogleSignIn()
                    showBackupDialog = false
                },
                dismissText = "취소",
                onDismiss = { showBackupDialog = false }
            )
        } else {
            GlassAlertDialog(
                onDismissRequest = { showBackupDialog = false },
                title = "데이터 백업",
                description = "현재 기기의 데이터를 백업하시겠습니까?\n\n현재 계정: ${googleAccountEmail}\n\n※ 백업된 데이터는 사용자 본인의 구글 드라이브에만 보관되며, 개발자에게 절대 공유되지 않습니다.",
                confirmText = "백업",
                onConfirm = {
                    onBackupDataClick()
                    showBackupDialog = false
                },
                dismissText = "취소",
                onDismiss = { showBackupDialog = false }
            )
        }
    }

    if (showRestoreDialog) {
        if (googleAccountEmail == null) {
            GlassAlertDialog(
                onDismissRequest = { showRestoreDialog = false },
                title = "구글 로그인 필요",
                description = "데이터 복원을 위해 구글 로그인이 필요합니다.\n\n※ 구글 계정은 백업 및 복원에만 사용됩니다.\n\n※ 연동 후 자동 동기화되지 않으며, 사용자가 원할 때만 데이터가 저장/복원됩니다.",
                confirmText = "로그인",
                onConfirm = {
                    pendingAction = PendingGoogleAuthAction.RESTORE
                    onRequestGoogleSignIn()
                    showRestoreDialog = false
                },
                dismissText = "취소",
                onDismiss = { showRestoreDialog = false }
            )
        } else {
            GlassAlertDialog(
                onDismissRequest = { showRestoreDialog = false },
                title = "데이터 복원",
                description = "구글 드라이브에서 데이터를 복원(병합)하시겠습니까?\n\n현재 계정: ${googleAccountEmail}\n\n※ 기존 데이터는 삭제되지 않으며 안전하게 합쳐집니다.",
                confirmText = "복원",
                onConfirm = {
                    onRestoreDataClick()
                    showRestoreDialog = false
                },
                dismissText = "취소",
                onDismiss = { showRestoreDialog = false }
            )
        }
    }

    if (showSignInDialog) {
        GlassAlertDialog(
            onDismissRequest = { showSignInDialog = false },
            title = "구글 계정 연동",
            description = "구글 계정은 데이터 백업 및 복원 용도로만 사용됩니다.\n\n※ 연동 후 자동 동기화되지 않으며, 사용자가 원할 때만 데이터가 저장/복원됩니다.\n\n※ 백업된 데이터는 사용자 본인의 구글 드라이브에만 안전하게 보관되며, 개발자에게는 공유되지 않습니다.",
            confirmText = "로그인",
            onConfirm = {
                onRequestGoogleSignIn()
                showSignInDialog = false
            },
            dismissText = "취소",
            onDismiss = { showSignInDialog = false }
        )
    }

    if (showSignOutDialog) {
        GlassAlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = "구글 계정 연동 해제",
            description = "현재 구글 계정(${googleAccountEmail}) 연동을 해제하시겠습니까?\n\n※ 연동을 해제하더라도 기기의 현재 데이터나, 이미 구글 드라이브에 백업된 파일은 삭제되지 않습니다.",
            confirmText = "해제",
            onConfirm = {
                onSignOutClick()
                showSignOutDialog = false
            },
            dismissText = "취소",
            onDismiss = { showSignOutDialog = false },
            isDestructive = true
        )
    }

    if (showUpdateDialog) {
        GlassAlertDialog(
            onDismissRequest = { showUpdateDialog = false },
            title = "업데이트 알림",
            description = "새로운 버전이 출시되었습니다.\n지금 업데이트하시겠습니까?",
            confirmText = "업데이트",
            onConfirm = {
                showUpdateDialog = false
                try {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = "market://details?id=${context.packageName}".toUri()
                    }
                    context.startActivity(intent)
                } catch (_: ActivityNotFoundException) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = "https://play.google.com/store/apps/details?id=${context.packageName}".toUri()
                    }
                    context.startActivity(intent)
                }
            },
            dismissText = "다음에",
            onDismiss = {
                showUpdateDialog = false
            },
            isDestructive = false
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
                    value = if (notiReceiveEnabled) "켜짐" else "꺼짐",
                    onClick = onNotificationSettingsClick
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "선택한 카드 유지",
                    value = if (keepSelectedCard) "켜짐" else "꺼짐",
                    onClick = {
                        setKeepSelectedCard(!keepSelectedCard)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /// 데이터 섹션
            SettingsSection(title = "데이터") {
                SettingsRow(
                    label = "데이터 백업",
                    onClick = {
                        showBackupDialog = true
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "데이터 복원",
                    onClick = {
                        showRestoreDialog = true
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "데이터 초기화",
                    onClick = {
                        showResetDialog = true
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "구글 계정",
                    value = googleAccountEmail ?: "연동 안 됨",
                    onClick = {
                        if (googleAccountEmail != null) {
                            showSignOutDialog = true
                        } else {
                            showSignInDialog = true
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /// 앱 정보 섹션
            SettingsSection(title = "정보") {
                val packageInfo = try {
                    context.packageManager.getPackageInfo(context.packageName, 0)
                } catch (_: Exception) {
                    null
                }
                val versionName = packageInfo?.versionName ?: "-"

                SettingsRow(
                    label = "앱 버전",
                    valueWidget = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (isUpdateAvailable) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(colors.error)
                                )
                            }
                            Text(
                                text = versionName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.secondary
                            )
                        }
                    },
                    showArrow = false,
                    onClick = if (isUpdateAvailable) {
                        { showUpdateDialog = true }
                    } else null
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

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(color = CardPilotColors.primary)
                Text(
                    text = loadingMessage ?: "처리 중입니다...",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
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
