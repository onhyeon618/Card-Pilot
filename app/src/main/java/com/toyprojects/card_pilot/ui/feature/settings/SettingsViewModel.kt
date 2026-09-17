package com.toyprojects.card_pilot.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.auth.AuthUseCases
import com.toyprojects.card_pilot.domain.backup.BackupException
import com.toyprojects.card_pilot.domain.backup.BackupProgressState
import com.toyprojects.card_pilot.domain.backup.BackupUseCases
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.domain.usecase.ClearAllDataUseCase
import com.toyprojects.card_pilot.model.ThemeType
import com.toyprojects.card_pilot.ui.component.NativeAdManager
import com.toyprojects.card_pilot.util.AppLogger
import com.toyprojects.card_pilot.util.HashUtil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SettingsViewModel(
    private val nativeAdManager: NativeAdManager,
    private val settingsRepository: SettingsRepository,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val authUseCases: AuthUseCases,
    private val backupUseCases: BackupUseCases
) : ViewModel() {

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object RequestGoogleSignIn : UiEvent()
        object RestartApp : UiEvent()
    }

    val nativeAd = nativeAdManager.nativeAd
    val isAdLoadFailed = nativeAdManager.isAdLoadFailed

    fun loadAd() {
        nativeAdManager.loadAd(forceRefresh = false)
    }

    private val _isUpdateAvailable = MutableStateFlow(false)
    val isUpdateAvailable: StateFlow<Boolean> = _isUpdateAvailable.asStateFlow()

    private val _googleAccountEmail = MutableStateFlow<String?>(null)
    val googleAccountEmail: StateFlow<String?> = _googleAccountEmail.asStateFlow()

    data class SettingsUiState(
        val isLoading: Boolean = false,
        val loadingMessage: String? = null
    )

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _isUpdateAvailable.value = settingsRepository.checkForUpdate()
            silentSignIn()
        }
        viewModelScope.launch {
            googleAccountEmail.collect { email ->
                if (!email.isNullOrEmpty()) {
                    val hashedEmail = HashUtil.sha256(email)
                    if (hashedEmail != null) {
                        AppLogger.setUserId(hashedEmail)
                    } else {
                        AppLogger.setUserId("hash_error")
                    }
                } else {
                    AppLogger.setUserId("anonymous")
                }
            }
        }
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private suspend fun silentSignIn() {
        authUseCases.silentSignInUseCase()
        _googleAccountEmail.value = authUseCases.signedInUserEmailUseCase()
    }

    fun requestGoogleSignIn() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.RequestGoogleSignIn)
        }
    }

    fun onSignInResult(result: SignInResult) {
        when (result) {
            is SignInResult.Success -> {
                _googleAccountEmail.value = result.email
                viewModelScope.launch {
                    _uiEvent.emit(UiEvent.ShowSnackbar("구글 계정이 연결되었습니다."))
                }
            }

            SignInResult.Cancelled -> {
                // 사용자가 의도적으로 취소한 경우 무동작
            }

            SignInResult.Error -> {
                viewModelScope.launch {
                    _uiEvent.emit(UiEvent.ShowSnackbar("네트워크 오류로 로그인에 실패했습니다."))
                }
            }
        }
    }

    private fun BackupProgressState.toProgressMessage(): String {
        return when (this) {
            BackupProgressState.EXTRACTING_DATA -> "기기에서 데이터를 추출하는 중..."
            BackupProgressState.PREPARING_NETWORK -> "구글 드라이브와 통신을 준비하는 중..."
            BackupProgressState.CHECKING_EXISTING -> "기존 백업 파일을 확인하는 중..."
            BackupProgressState.UPLOADING_OVERWRITE -> "안전하게 기존 데이터를 덮어쓰는 중..."
            BackupProgressState.UPLOADING_NEW -> "새로운 백업 파일을 업로드하여 생성하는 중..."
            BackupProgressState.DOWNLOADING -> "백업된 데이터를 기기로 다운로드하는 중..."
            BackupProgressState.RESTORING_DATA -> "데이터를 기기에 복원하는 중..."
            BackupProgressState.DONE -> "완료!"
        }
    }

    private fun BackupException.toUserMessage(): String {
        return when (this) {
            is BackupException.QuotaExceeded -> "구글 드라이브 용량이 부족합니다."
            is BackupException.NetworkUnavailable -> "인터넷 연결을 확인해주세요."
            is BackupException.AuthRequired -> "권한이 만료되었습니다. 다시 로그인해주세요."
            is BackupException.NotFound -> "백업된 파일이 존재하지 않습니다."
            is BackupException.InvalidBackupFile -> "백업 파일이 손상되었거나 유효하지 않습니다."
            is BackupException.Unknown -> "알 수 없는 오류가 발생했습니다."
        }
    }

    fun backupToGoogleDrive() {
        if (_googleAccountEmail.value == null) {
            viewModelScope.launch { _uiEvent.emit(UiEvent.ShowSnackbar("로그인이 필요합니다.")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loadingMessage = "기기에서 데이터를 추출하는 중...") }
            try {
                backupUseCases.cloudBackupUseCase().collect { state ->
                    _uiState.update { it.copy(loadingMessage = state.toProgressMessage()) }
                }
                _uiEvent.emit(UiEvent.ShowSnackbar("데이터 백업이 완료되었습니다."))
            } catch (e: BackupException) {
                if (e is BackupException.Unknown || e is BackupException.InvalidBackupFile) {
                    AppLogger.e(TAG, "backupToGoogleDrive failed (BackupException)", e)
                } else {
                    AppLogger.d(TAG, "backupToGoogleDrive failed: ${e.javaClass.simpleName}")
                }
                if (e is BackupException.AuthRequired) signOutFromGoogle()
                _uiEvent.emit(UiEvent.ShowSnackbar("백업 실패: ${e.toUserMessage()}"))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                AppLogger.e(TAG, "backupToGoogleDrive failed (Exception)", e)
                _uiEvent.emit(UiEvent.ShowSnackbar("백업 실패: 알 수 없는 오류가 발생했습니다."))
            } finally {
                _uiState.update { it.copy(isLoading = false, loadingMessage = null) }
            }
        }
    }

    fun restoreFromGoogleDrive() {
        if (_googleAccountEmail.value == null) {
            viewModelScope.launch { _uiEvent.emit(UiEvent.ShowSnackbar("로그인이 필요합니다.")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loadingMessage = "구글 드라이브와 통신을 준비하는 중...") }
            try {
                backupUseCases.cloudRestoreUseCase().collect { state ->
                    _uiState.update { it.copy(loadingMessage = state.toProgressMessage()) }
                }
                _uiEvent.emit(UiEvent.ShowSnackbar("복원이 완료되었습니다. 변경사항을 적용하기 위해 앱을 재시작합니다."))
                kotlinx.coroutines.delay(1500.milliseconds)
                _uiEvent.emit(UiEvent.RestartApp)
            } catch (e: BackupException) {
                if (e is BackupException.Unknown || e is BackupException.InvalidBackupFile) {
                    AppLogger.e(TAG, "restoreFromGoogleDrive failed (BackupException)", e)
                } else {
                    AppLogger.d(TAG, "restoreFromGoogleDrive failed: ${e.javaClass.simpleName}")
                }
                if (e is BackupException.AuthRequired) signOutFromGoogle()
                _uiEvent.emit(UiEvent.ShowSnackbar("복원 실패: ${e.toUserMessage()}"))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                AppLogger.e(TAG, "restoreFromGoogleDrive failed (Exception)", e)
                _uiEvent.emit(UiEvent.ShowSnackbar("복원 실패: 알 수 없는 오류가 발생했습니다."))
            } finally {
                _uiState.update { it.copy(isLoading = false, loadingMessage = null) }
            }
        }
    }

    fun signOutFromGoogle() {
        viewModelScope.launch {
            authUseCases.cloudSignOutUseCase()
            _googleAccountEmail.value = null
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
            } catch (e: Exception) {
                AppLogger.e(TAG, "clearAllData failed", e)
                _uiEvent.emit(UiEvent.ShowSnackbar("데이터 초기화에 실패했습니다."))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // 화면 이탈 시(뷰모델 파기 시) 다음 진입을 위해 미리 새 광고를 로드
        nativeAdManager.loadAd(forceRefresh = true)
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}
