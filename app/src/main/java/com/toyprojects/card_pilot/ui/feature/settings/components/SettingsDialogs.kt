package com.toyprojects.card_pilot.ui.feature.settings.components

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.ui.shared.GlassAlertDialog

/// 데이터 초기화 다이얼로그
@Composable
fun ResetDataDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    GlassAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.setting_data_reset),
        description = stringResource(R.string.msg_reset),
        confirmText = stringResource(R.string.btn_reset),
        onConfirm = onConfirm,
        dismissText = stringResource(R.string.btn_cancel),
        onDismiss = onDismiss,
        isDestructive = true
    )
}

/// 데이터 백업 다이얼로그
@Composable
fun BackupConfirmDialog(
    googleAccountEmail: String?,
    onConfirm: () -> Unit,
    onRequestSignIn: () -> Unit,
    onDismiss: () -> Unit
) {
    if (googleAccountEmail == null) {
        GlassAlertDialog(
            onDismissRequest = onDismiss,
            title = stringResource(R.string.title_login_required),
            description = stringResource(R.string.msg_login_required_backup),
            confirmText = stringResource(R.string.btn_login),
            onConfirm = onRequestSignIn,
            dismissText = stringResource(R.string.btn_cancel),
            onDismiss = onDismiss
        )
    } else {
        GlassAlertDialog(
            onDismissRequest = onDismiss,
            title = stringResource(R.string.setting_data_backup),
            description = stringResource(R.string.msg_backup, googleAccountEmail),
            confirmText = stringResource(R.string.btn_backup),
            onConfirm = onConfirm,
            dismissText = stringResource(R.string.btn_cancel),
            onDismiss = onDismiss
        )
    }
}

/// 데이터 복원 다이얼로그
@Composable
fun RestoreConfirmDialog(
    googleAccountEmail: String?,
    onConfirm: () -> Unit,
    onRequestSignIn: () -> Unit,
    onDismiss: () -> Unit
) {
    if (googleAccountEmail == null) {
        GlassAlertDialog(
            onDismissRequest = onDismiss,
            title = stringResource(R.string.title_login_required),
            description = stringResource(R.string.msg_login_required_restore),
            confirmText = stringResource(R.string.btn_login),
            onConfirm = onRequestSignIn,
            dismissText = stringResource(R.string.btn_cancel),
            onDismiss = onDismiss
        )
    } else {
        GlassAlertDialog(
            onDismissRequest = onDismiss,
            title = stringResource(R.string.setting_data_restore),
            description = stringResource(R.string.msg_restore, googleAccountEmail),
            confirmText = stringResource(R.string.btn_restore),
            onConfirm = onConfirm,
            dismissText = stringResource(R.string.btn_cancel),
            onDismiss = onDismiss
        )
    }
}

/// 로그인 다이얼로그
@Composable
fun SignInDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    GlassAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.title_login),
        description = stringResource(R.string.msg_google_account_desc),
        confirmText = stringResource(R.string.btn_login),
        onConfirm = onConfirm,
        dismissText = stringResource(R.string.btn_cancel),
        onDismiss = onDismiss
    )
}

/// 로그아웃 다이얼로그
@Composable
fun SignOutDialog(googleAccountEmail: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    GlassAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.title_logout),
        description = "현재 구글 계정(${googleAccountEmail}) 연동을 해제하시겠습니까?\n\n※ 연동을 해제하더라도 기기의 현재 데이터나, 이미 구글 드라이브에 백업된 파일은 삭제되지 않습니다.",
        confirmText = stringResource(R.string.btn_logout),
        onConfirm = onConfirm,
        dismissText = stringResource(R.string.btn_cancel),
        onDismiss = onDismiss,
        isDestructive = true
    )
}

/// 업데이트 알림 다이얼로그
@Composable
fun UpdateDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    GlassAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.title_app_update),
        description = stringResource(R.string.msg_app_version),
        confirmText = stringResource(R.string.btn_update),
        onConfirm = {
            onDismiss()
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
        dismissText = stringResource(R.string.btn_later),
        onDismiss = onDismiss,
        isDestructive = false
    )
}
