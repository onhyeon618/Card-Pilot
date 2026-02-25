package com.toyprojects.card_pilot.ui.feature.benefit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditBenefitUiState(
    val name: String = "",
    val amount: String = "",
    val dailyLimit: String = "",
    val oneTimeLimit: String = "",
    val rate: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

class EditBenefitViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // TODO: 화면 진입 시 넘겨받은 초기값이 있으면 세팅

    private val _uiState = MutableStateFlow(EditBenefitUiState())
    val uiState: StateFlow<EditBenefitUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun updateDailyLimit(limit: String) {
        _uiState.update { it.copy(dailyLimit = limit) }
    }

    fun updateOneTimeLimit(limit: String) {
        _uiState.update { it.copy(oneTimeLimit = limit) }
    }

    fun updateRate(rate: String) {
        _uiState.update { it.copy(rate = rate) }
    }

    fun saveBenefit() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            // TODO: 직전 화면 (EditCardScreen)에 Benefit 전달 ... 또는 DB에 선추가 후 카드 미저장 시 제거?

            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }
}
