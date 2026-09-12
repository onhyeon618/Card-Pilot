package com.toyprojects.card_pilot.ui.feature.settings

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.backup.ExportBackupUseCase
import com.toyprojects.card_pilot.domain.backup.GoogleAuthClient
import com.toyprojects.card_pilot.domain.backup.GoogleDriveClient
import com.toyprojects.card_pilot.domain.backup.MergeBackupUseCase
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.domain.usecase.ClearAllDataUseCase
import com.toyprojects.card_pilot.model.ThemeType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val googleAuthClient: GoogleAuthClient,
    private val googleDriveClient: GoogleDriveClient,
    private val exportBackupUseCase: ExportBackupUseCase,
    private val mergeBackupUseCase: MergeBackupUseCase
) : ViewModel() {

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class LaunchGoogleSignIn(val intent: Intent) : UiEvent()
    }

    var isUpdateAvailable by mutableStateOf(false)
        private set

    var googleAccountEmail by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var loadingMessage by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            isUpdateAvailable = settingsRepository.checkForUpdate()
            silentSignIn()
        }
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private suspend fun silentSignIn() {
        val account = googleAuthClient.silentSignIn() ?: googleAuthClient.getSignedInAccount()
        googleAccountEmail = account?.email
    }

    fun requestGoogleSignIn() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.LaunchGoogleSignIn(googleAuthClient.getSignInIntent()))
        }
    }

    fun handleSignInResult(intent: Intent?) {
        val account = googleAuthClient.getSignedInAccountFromIntent(intent)
        if (account != null) {
            googleAccountEmail = account.email
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowSnackbar("구글 계정이 연결되었습니다."))
            }
        } else {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowSnackbar("구글 로그인에 실패했거나 취소되었습니다."))
            }
        }
    }

    fun backupToGoogleDrive() {
        val account = googleAuthClient.getSignedInAccount()
        if (account == null) {
            viewModelScope.launch { _uiEvent.emit(UiEvent.ShowSnackbar("로그인이 필요합니다.")) }
            return
        }

        viewModelScope.launch {
            isLoading = true
            loadingMessage = "기기에서 데이터를 추출하는 중..."
            try {
                val jsonContent = exportBackupUseCase()
                googleDriveClient.uploadBackup(account, jsonContent) { progressMsg ->
                    loadingMessage = progressMsg
                }
                loadingMessage = "백업 완료!"
                _uiEvent.emit(UiEvent.ShowSnackbar("데이터 백업이 완료되었습니다."))
            } catch (e: Exception) {
                val errorMsg = if (e.message?.contains("quota") == true) "드라이브 용량이 부족합니다." else "네트워크 연결을 확인해주세요."
                _uiEvent.emit(UiEvent.ShowSnackbar("백업 실패: $errorMsg"))
            } finally {
                isLoading = false
                loadingMessage = null
            }
        }
    }

    fun restoreFromGoogleDrive() {
        val account = googleAuthClient.getSignedInAccount()
        if (account == null) {
            viewModelScope.launch { _uiEvent.emit(UiEvent.ShowSnackbar("로그인이 필요합니다.")) }
            return
        }

        viewModelScope.launch {
            isLoading = true
            loadingMessage = "구글 드라이브와 통신을 준비하는 중..."
            try {
                val jsonContent = googleDriveClient.downloadBackup(account) { progressMsg ->
                    loadingMessage = progressMsg
                }
                if (jsonContent != null) {
                    loadingMessage = "기기 데이터와 안전하게 병합하는 중..."
                    mergeBackupUseCase(jsonContent)
                    loadingMessage = "복원 완료!"
                    _uiEvent.emit(UiEvent.ShowSnackbar("데이터 복원이 성공적으로 완료되었습니다."))
                } else {
                    _uiEvent.emit(UiEvent.ShowSnackbar("백업된 파일이 존재하지 않습니다."))
                }
            } catch (e: IllegalArgumentException) {
                _uiEvent.emit(UiEvent.ShowSnackbar(e.message ?: "지원하지 않는 백업 파일 포맷입니다."))
            } catch (_: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("복원 실패: 네트워크를 확인해주세요."))
            } finally {
                isLoading = false
                loadingMessage = null
            }
        }
    }

    fun signOutFromGoogle() {
        viewModelScope.launch {
            googleAuthClient.signOut()
            googleAccountEmail = null
            _uiEvent.emit(UiEvent.ShowSnackbar("구글 계정 연동이 해제되었습니다."))
        }
    }

    val currentTheme: StateFlow<ThemeType> = settingsRepository.themeType
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeType.PURPLE
        )

    fun updateTheme(themeType: ThemeType) {
        viewModelScope.launch {
            settingsRepository.setTheme(themeType)
        }
    }

    val notiReceiveEnabled: StateFlow<Boolean> = settingsRepository.notiReceiveEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val keepSelectedCard: StateFlow<Boolean> = settingsRepository.keepSelectedCard
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun setKeepSelectedCard(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.setKeepSelectedCard(value)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                clearAllDataUseCase()
                _uiEvent.emit(UiEvent.ShowSnackbar("데이터가 초기화되었습니다."))
            } catch (_: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("데이터 초기화에 실패했습니다."))
            }
        }
    }
}
