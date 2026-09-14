package com.toyprojects.card_pilot.ui.feature.settings

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.MainActivity
import com.toyprojects.card_pilot.model.ThemeType
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.settings.components.BackupConfirmDialog
import com.toyprojects.card_pilot.ui.feature.settings.components.ResetDataDialog
import com.toyprojects.card_pilot.ui.feature.settings.components.RestoreConfirmDialog
import com.toyprojects.card_pilot.ui.feature.settings.components.SettingsRow
import com.toyprojects.card_pilot.ui.feature.settings.components.SettingsSection
import com.toyprojects.card_pilot.ui.feature.settings.components.SignInDialog
import com.toyprojects.card_pilot.ui.feature.settings.components.SignOutDialog
import com.toyprojects.card_pilot.ui.feature.settings.components.ThemeSelectDialog
import com.toyprojects.card_pilot.ui.feature.settings.components.UpdateDialog
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.EdgeToEdgeColumn
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.shared.LoadingOverlay
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

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
    val isUpdateAvailable by viewModel.isUpdateAvailable.collectAsStateWithLifecycle()
    val googleAccountEmail by viewModel.googleAccountEmail.collectAsStateWithLifecycle()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = uiState.isLoading
    val loadingMessage = uiState.loadingMessage

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val googleAuthUiClient = remember(context) { GoogleAuthUiClient(context) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.onSignInResult(googleAuthUiClient.handleSignInIntent(result.data))
        } else {
            viewModel.onSignInResult(SignInResult.Cancelled)
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

                is SettingsViewModel.UiEvent.RequestGoogleSignIn -> {
                    try {
                        googleSignInLauncher.launch(googleAuthUiClient.getSignInIntent())
                    } catch (_: ActivityNotFoundException) {
                        snackbarHostState.showSnackbar("구글 서비스에 연결할 수 없습니다.")
                    }
                }

                is SettingsViewModel.UiEvent.RestartApp -> {
                    val intent =
                        Intent(context, MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        }
                    context.startActivity(intent)
                    kotlin.system.exitProcess(0)
                }
            }
        }
    }

    val state = SettingsState(
        currentTheme = currentTheme,
        notiReceiveEnabled = notiReceiveEnabled,
        keepSelectedCard = keepSelectedCard,
        isUpdateAvailable = isUpdateAvailable,
        googleAccountEmail = googleAccountEmail,
        isLoading = isLoading,
        loadingMessage = loadingMessage
    )

    val actions = remember {
        object : SettingsActions {
            override fun onThemeSelected(themeType: ThemeType) = viewModel.updateTheme(themeType)
            override fun setKeepSelectedCard(keep: Boolean) = viewModel.setKeepSelectedCard(keep)
            override fun onBack() = onBack()
            override fun onCardListClick() = onCardListClick()
            override fun onAddCardClick() = onAddCardClick()
            override fun onNotificationSettingsClick() = onNotificationSettingsClick()
            override fun onResetDataClick() = viewModel.clearAllData()
            override fun onRequestGoogleSignIn() = viewModel.requestGoogleSignIn()
            override fun onBackupDataClick() = viewModel.backupToGoogleDrive()
            override fun onRestoreDataClick() = viewModel.restoreFromGoogleDrive()
            override fun onSignOutClick() = viewModel.signOutFromGoogle()
        }
    }

    SettingsScreen(
        state = state,
        actions = actions,
        snackbarHostState = snackbarHostState
    )
}

private sealed class DialogState {
    object None : DialogState()
    object ThemeSelect : DialogState()
    object ResetData : DialogState()
    object BackupConfirm : DialogState()
    object RestoreConfirm : DialogState()
    object SignIn : DialogState()
    object SignOut : DialogState()
    object Update : DialogState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    actions: SettingsActions,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val colors = CardPilotColors
    var currentDialog by remember { mutableStateOf<DialogState>(DialogState.None) }
    var pendingAction by remember { mutableStateOf(PendingGoogleAuthAction.NONE) }
    val context = LocalContext.current

    LaunchedEffect(state.googleAccountEmail) {
        if (state.googleAccountEmail != null) {
            when (pendingAction) {
                PendingGoogleAuthAction.BACKUP -> currentDialog = DialogState.BackupConfirm
                PendingGoogleAuthAction.RESTORE -> currentDialog = DialogState.RestoreConfirm
                PendingGoogleAuthAction.NONE -> {}
            }
            pendingAction = PendingGoogleAuthAction.NONE
        } else {
            pendingAction = PendingGoogleAuthAction.NONE
        }
    }

    when (currentDialog) {
        is DialogState.ThemeSelect -> {
            ThemeSelectDialog(
                currentTheme = state.currentTheme,
                onThemeSelected = { themeType ->
                    actions.onThemeSelected(themeType)
                    currentDialog = DialogState.None
                },
                onDismiss = { currentDialog = DialogState.None }
            )
        }

        is DialogState.ResetData -> {
            ResetDataDialog(
                onConfirm = {
                    actions.onResetDataClick()
                    currentDialog = DialogState.None
                },
                onDismiss = { currentDialog = DialogState.None }
            )
        }

        is DialogState.BackupConfirm -> {
            BackupConfirmDialog(
                googleAccountEmail = state.googleAccountEmail,
                onConfirm = {
                    actions.onBackupDataClick()
                    currentDialog = DialogState.None
                },
                onRequestSignIn = {
                    pendingAction = PendingGoogleAuthAction.BACKUP
                    actions.onRequestGoogleSignIn()
                    currentDialog = DialogState.None
                },
                onDismiss = { currentDialog = DialogState.None }
            )
        }

        is DialogState.RestoreConfirm -> {
            RestoreConfirmDialog(
                googleAccountEmail = state.googleAccountEmail,
                onConfirm = {
                    actions.onRestoreDataClick()
                    currentDialog = DialogState.None
                },
                onRequestSignIn = {
                    pendingAction = PendingGoogleAuthAction.RESTORE
                    actions.onRequestGoogleSignIn()
                    currentDialog = DialogState.None
                },
                onDismiss = { currentDialog = DialogState.None }
            )
        }

        is DialogState.SignIn -> {
            SignInDialog(
                onConfirm = {
                    actions.onRequestGoogleSignIn()
                    currentDialog = DialogState.None
                },
                onDismiss = { currentDialog = DialogState.None }
            )
        }

        is DialogState.SignOut -> {
            SignOutDialog(
                googleAccountEmail = state.googleAccountEmail ?: "",
                onConfirm = {
                    actions.onSignOutClick()
                    currentDialog = DialogState.None
                },
                onDismiss = { currentDialog = DialogState.None }
            )
        }

        is DialogState.Update -> {
            UpdateDialog(
                onDismiss = { currentDialog = DialogState.None }
            )
        }

        is DialogState.None -> {}
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
                        IconButton(onClick = actions::onBack) {
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
                        actions.onCardListClick()
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "카드 추가",
                    onClick = {
                        actions.onAddCardClick()
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
                        currentDialog = DialogState.ThemeSelect
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "지출 알림 자동 수신",
                    value = if (state.notiReceiveEnabled) "켜짐" else "꺼짐",
                    onClick = actions::onNotificationSettingsClick
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "선택한 카드 유지",
                    value = if (state.keepSelectedCard) "켜짐" else "꺼짐",
                    onClick = {
                        actions.setKeepSelectedCard(!state.keepSelectedCard)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /// 데이터 섹션
            SettingsSection(title = "데이터") {
                SettingsRow(
                    label = "데이터 백업",
                    onClick = {
                        currentDialog = DialogState.BackupConfirm
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "데이터 복원",
                    onClick = {
                        currentDialog = DialogState.RestoreConfirm
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "데이터 초기화",
                    onClick = {
                        currentDialog = DialogState.ResetData
                    }
                )
                HorizontalDivider(color = colors.gray100, thickness = 1.dp)
                SettingsRow(
                    label = "구글 계정",
                    value = state.googleAccountEmail ?: "연동 안 됨",
                    onClick = {
                        if (state.googleAccountEmail != null) {
                            currentDialog = DialogState.SignOut
                        } else {
                            currentDialog = DialogState.SignIn
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
                            if (state.isUpdateAvailable) {
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
                    onClick = if (state.isUpdateAvailable) {
                        { currentDialog = DialogState.Update }
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

    if (state.isLoading) {
        LoadingOverlay(message = state.loadingMessage)
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    CardPilotTheme {
        SettingsScreen(
            state = SettingsState(),
            actions = object : SettingsActions {
                override fun onThemeSelected(themeType: ThemeType) {}
                override fun setKeepSelectedCard(keep: Boolean) {}
                override fun onBack() {}
                override fun onCardListClick() {}
                override fun onAddCardClick() {}
                override fun onNotificationSettingsClick() {}
                override fun onResetDataClick() {}
                override fun onRequestGoogleSignIn() {}
                override fun onBackupDataClick() {}
                override fun onRestoreDataClick() {}
                override fun onSignOutClick() {}
            }
        )
    }
}
