package com.toyprojects.card_pilot.ui.feature.settings

import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.settings.components.AppListBottomSheetContent
import com.toyprojects.card_pilot.ui.feature.settings.components.CardAppListItem
import com.toyprojects.card_pilot.ui.feature.settings.components.GlobalNotificationCard
import com.toyprojects.card_pilot.ui.feature.settings.model.CardCompanyApp
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.EdgeToEdgeColumn
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsRoute(
    viewModel: NotificationSettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBack: () -> Unit
) {
    val notiReceiveEnabled by viewModel.notiReceiveEnabled.collectAsStateWithLifecycle()
    val installedCardApps by viewModel.installedCardApps.collectAsStateWithLifecycle()
    val notiReceiveApps by viewModel.notiReceiveApps.collectAsStateWithLifecycle()
    val allInstalledApps by viewModel.allInstalledApps.collectAsStateWithLifecycle()
    val isLoadingAllApps by viewModel.isLoadingAllApps.collectAsStateWithLifecycle()
    val showPermissionDialog by viewModel.showPermissionDialog.collectAsStateWithLifecycle()

    var showAddAppBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.checkNotificationPermissionAfterResult()
    }

    NotificationSettingsScreen(
        notiReceiveEnabled = notiReceiveEnabled,
        onToggleNotiReceive = viewModel::toggleNotiReceive,
        installedCardApps = installedCardApps,
        notiReceiveApps = notiReceiveApps,
        onToggleAppNotiReceive = viewModel::toggleAppNotiReceive,
        onAddCustomAppClick = {
            viewModel.loadAllInstalledApps()
            showAddAppBottomSheet = true
        },
        onBack = onBack
    )

    if (showAddAppBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddAppBottomSheet = false },
            sheetState = sheetState,
            containerColor = CardPilotColors.white,
            tonalElevation = 0.dp
        ) {
            AppListBottomSheetContent(
                isLoadingAllApps = isLoadingAllApps,
                allInstalledApps = allInstalledApps,
                onAppSelected = { packageName ->
                    viewModel.addCustomApp(packageName)
                    showAddAppBottomSheet = false
                }
            )
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.dismissPermissionDialog()
            },
            title = { Text(text = "알림 접근 권한 필요") },
            text = { Text(text = "지출 알림을 수신하려면 알림 접근 권한을 허용해야 합니다. 설정 화면으로 이동하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissPermissionDialog()
                        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                        permissionLauncher.launch(intent)
                    }
                ) {
                    Text("설정으로 이동")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissPermissionDialog()
                    }
                ) {
                    Text("취소")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    notiReceiveEnabled: Boolean = false,
    onToggleNotiReceive: () -> Unit = {},
    installedCardApps: List<CardCompanyApp> = emptyList(),
    notiReceiveApps: Set<String> = emptySet(),
    onToggleAppNotiReceive: (String) -> Unit = {},
    onAddCustomAppClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val colors = CardPilotColors

    GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "지출 알림 수신 설정",
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
        }
    ) { paddingValues ->
        EdgeToEdgeColumn(
            paddingValues = paddingValues,
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 전체 on/off 설정
            GlobalNotificationCard(
                notiReceiveEnabled = notiReceiveEnabled,
                onToggleNotiReceive = { onToggleNotiReceive() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 앱별 설정
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .alpha(if (notiReceiveEnabled) 1f else 0.5f)
            ) {
                Text(
                    text = "카드사별 수신 설정",
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.secondary,
                    modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.surfaceGlass)
                        .border(
                            width = 1.dp,
                            color = colors.outline,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    if (installedCardApps.isEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "설치된 카드사 앱이 없습니다.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.secondary
                            )
                        }
                    } else {
                        installedCardApps.forEachIndexed { index, app ->
                            CardAppListItem(
                                app = app,
                                isChecked = notiReceiveApps.contains(app.packageName),
                                enabled = notiReceiveEnabled,
                                onCheckedChange = { onToggleAppNotiReceive(app.packageName) }
                            )
                            if (index < installedCardApps.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    color = colors.outline.copy(alpha = 0.8f),
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 앱 추가 버튼
            OutlinedButton(
                onClick = onAddCustomAppClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp),
                enabled = notiReceiveEnabled,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.textPrimary,
                    disabledContentColor = colors.textPrimary.copy(alpha = 0.5f)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (notiReceiveEnabled) colors.outline else colors.outline.copy(alpha = 0.3f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "등록 안 된 앱 추가하기",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview
@Composable
fun NotificationSettingsScreenPreview() {
    CardPilotTheme {
        NotificationSettingsScreen(
            notiReceiveEnabled = true,
            installedCardApps = listOf(
                CardCompanyApp("KB국민카드", "com.kbcard.cxh.appcard"),
                CardCompanyApp("현대카드", "com.hyundaicard.appcard")
            ),
            notiReceiveApps = setOf("com.kbcard.cxh.appcard")
        )
    }
}
