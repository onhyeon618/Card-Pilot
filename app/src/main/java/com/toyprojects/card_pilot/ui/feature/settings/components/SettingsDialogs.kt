package com.toyprojects.card_pilot.ui.feature.settings.components

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.toyprojects.card_pilot.ui.shared.GlassAlertDialog

/// 데이터 초기화 확인 다이얼로그
@Composable
fun ResetDataDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    GlassAlertDialog(
        onDismissRequest = onDismiss,
        title = "데이터 초기화",
        description = "모든 데이터가 삭제되며 복구할 수 없습니다.\n정말 초기화하시겠습니까?",
        confirmText = "초기화",
        onConfirm = onConfirm,
        dismissText = "취소",
        onDismiss = onDismiss,
        isDestructive = true
    )
}

/// 백업 확인 다이얼로그
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
            title = "구글 로그인 필요",
            description = "데이터 백업을 위해 구글 로그인이 필요합니다.\n\n※ 구글 계정은 백업 및 복원에만 사용됩니다.\n\n※ 연동 후 자동 동기화되지 않으며, 사용자가 원할 때만 데이터가 저장/복원됩니다.",
            confirmText = "로그인",
            onConfirm = onRequestSignIn,
            dismissText = "취소",
            onDismiss = onDismiss
        )
    } else {
        GlassAlertDialog(
            onDismissRequest = onDismiss,
            title = "데이터 백업",
            description = "현재 기기의 데이터를 백업하시겠습니까?\n\n현재 계정: ${googleAccountEmail}\n\n※ 백업된 데이터는 사용자 본인의 구글 드라이브에만 보관되며, 개발자에게 절대 공유되지 않습니다.",
            confirmText = "백업",
            onConfirm = onConfirm,
            dismissText = "취소",
            onDismiss = onDismiss
        )
    }
}

/// 복원 확인 다이얼로그
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
            title = "구글 로그인 필요",
            description = "데이터 복원을 위해 구글 로그인이 필요합니다.\n\n※ 구글 계정은 백업 및 복원에만 사용됩니다.\n\n※ 연동 후 자동 동기화되지 않으며, 사용자가 원할 때만 데이터가 저장/복원됩니다.",
            confirmText = "로그인",
            onConfirm = onRequestSignIn,
            dismissText = "취소",
            onDismiss = onDismiss
        )
    } else {
        GlassAlertDialog(
            onDismissRequest = onDismiss,
            title = "데이터 복원",
            description = "구글 드라이브에서 데이터를 복원하시겠습니까?\n\n현재 계정: ${googleAccountEmail}\n\n※ 주의: 현재 기기의 데이터는 삭제되고 백업된 데이터로 완전히 덮어씌워집니다.\n※ 복원 완료 후 앱이 자동으로 재시작됩니다.",
            confirmText = "복원",
            onConfirm = onConfirm,
            dismissText = "취소",
            onDismiss = onDismiss
        )
    }
}

/// 로그인 확인 다이얼로그
@Composable
fun SignInDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    GlassAlertDialog(
        onDismissRequest = onDismiss,
        title = "구글 계정 연동",
        description = "구글 계정은 데이터 백업 및 복원 용도로만 사용됩니다.\n\n※ 연동 후 자동 동기화되지 않으며, 사용자가 원할 때만 데이터가 저장/복원됩니다.\n\n※ 백업된 데이터는 사용자 본인의 구글 드라이브에만 안전하게 보관되며, 개발자에게는 공유되지 않습니다.",
        confirmText = "로그인",
        onConfirm = onConfirm,
        dismissText = "취소",
        onDismiss = onDismiss
    )
}

/// 로그아웃 확인 다이얼로그
@Composable
fun SignOutDialog(googleAccountEmail: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    GlassAlertDialog(
        onDismissRequest = onDismiss,
        title = "구글 계정 연동 해제",
        description = "현재 구글 계정(${googleAccountEmail}) 연동을 해제하시겠습니까?\n\n※ 연동을 해제하더라도 기기의 현재 데이터나, 이미 구글 드라이브에 백업된 파일은 삭제되지 않습니다.",
        confirmText = "해제",
        onConfirm = onConfirm,
        dismissText = "취소",
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
        title = "업데이트 알림",
        description = "새로운 버전이 출시되었습니다.\n지금 업데이트하시겠습니까?",
        confirmText = "업데이트",
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
        dismissText = "다음에",
        onDismiss = onDismiss,
        isDestructive = false
    )
}
