package com.toyprojects.card_pilot.ui.shared

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds

/// 키보드가 노출된 상태에서 바텀시트 호출 시 키보드 닫힘 애니메이션을 대기한 뒤 액션을 실행하는 런처
class KeyboardDismissLauncher internal constructor(
    private val scope: CoroutineScope,
    private val keyboardController: SoftwareKeyboardController?,
    private val focusManager: FocusManager,
    private val imeInsets: WindowInsets,
    private val density: Density
) {
    private var pendingJob: Job? = null

    fun launch(action: () -> Unit) {
        focusManager.clearFocus()
        keyboardController?.hide()

        val isKeyboardVisible = imeInsets.getBottom(density) > 0
        if (isKeyboardVisible) {
            pendingJob?.cancel()
            pendingJob = scope.launch {
                withTimeoutOrNull(500.milliseconds) {
                    snapshotFlow { imeInsets.getBottom(density) }.first { it == 0 }
                }
                action()
            }
        } else {
            action()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun rememberKeyboardDismissLauncher(): KeyboardDismissLauncher {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime
    val scope = rememberCoroutineScope()

    return remember(keyboardController, focusManager, density, imeInsets, scope) {
        KeyboardDismissLauncher(
            scope = scope,
            keyboardController = keyboardController,
            focusManager = focusManager,
            imeInsets = imeInsets,
            density = density
        )
    }
}
