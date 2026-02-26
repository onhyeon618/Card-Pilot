package com.toyprojects.card_pilot.ui.feature.benefit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.ui.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BenefitFormData(
    val name: String = "",
    val explanation: String = "",
    val capAmount: String = "",
    val dailyLimit: String = "",
    val oneTimeLimit: String = "",
    val rate: String = ""
)

data class EditBenefitUiState(
    val formData: BenefitFormData = BenefitFormData(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isModified: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
) {
    val isFormValid: Boolean
        get() = formData.name.isNotBlank() && formData.capAmount.isNotBlank() && formData.rate.isNotBlank()
}

class EditBenefitViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val benefitRepository: BenefitRepository,
) : ViewModel() {

    private val _benefitId: Long? = savedStateHandle.toRoute<Screen.EditBenefit>().benefitId

    private var initialSnapshot: BenefitFormData = BenefitFormData()

    private val _uiState = MutableStateFlow(EditBenefitUiState())
    val uiState: StateFlow<EditBenefitUiState> = _uiState.asStateFlow()

    init {
        if (_benefitId != null) {
            loadBenefitData(_benefitId)
        }
    }

    private fun loadBenefitData(benefitId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val benefit = benefitRepository.getBenefitPropertyById(benefitId)

                if (benefit == null) {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                } else {
                    _uiState.update { state ->
                        val initialFormData = BenefitFormData(
                            name = benefit.name,
                            explanation = benefit.explanation ?: "",
                            capAmount = benefit.capAmount.toString(),
                            dailyLimit = benefit.dailyLimit.toString(),
                            oneTimeLimit = benefit.oneTimeLimit.toString(),
                            rate = benefit.rate.toString()
                        )
                        initialSnapshot = initialFormData
                        state.copy(
                            formData = initialFormData,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }

    private fun updateFormData(transform: (BenefitFormData) -> BenefitFormData) {
        _uiState.update { currentState ->
            val nextFormData = transform(currentState.formData)
            currentState.copy(
                formData = nextFormData,
                isModified = nextFormData != initialSnapshot
            )
        }
    }

    fun updateName(name: String) {
        updateFormData { it.copy(name = name) }
    }

    fun updateExplanation(explanation: String) {
        updateFormData { it.copy(explanation = explanation) }
    }

    fun updateAmount(amount: String) {
        if (amount.all { it.isDigit() }) {
            updateFormData { it.copy(capAmount = amount) }
        }
    }

    fun updateDailyLimit(limit: String) {
        if (limit.all { it.isDigit() }) {
            updateFormData { it.copy(dailyLimit = limit) }
        }
    }

    fun updateOneTimeLimit(limit: String) {
        if (limit.all { it.isDigit() }) {
            updateFormData { it.copy(oneTimeLimit = limit) }
        }
    }

    fun updateRate(rate: String) {
        if (rate.count { it == '.' } <= 1 && rate.all { it.isDigit() || it == '.' }) {
            updateFormData { it.copy(rate = rate) }
        }
    }

    fun saveBenefit() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            // TODO: 직전 화면 (EditCardScreen)에 Benefit 전달

            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }
}
