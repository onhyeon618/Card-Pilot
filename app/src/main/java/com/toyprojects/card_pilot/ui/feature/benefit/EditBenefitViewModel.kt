package com.toyprojects.card_pilot.ui.feature.benefit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.ui.Screen
import com.toyprojects.card_pilot.ui.navigation.BenefitPropertyType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

sealed interface EditBenefitEvent {
    data class SaveSuccess(val benefit: BenefitProperty, val benefitIndex: Int) : EditBenefitEvent
}

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
    val isSaving: Boolean = false
) {
    val isFormValid: Boolean
        get() = formData.name.isNotBlank() && formData.capAmount.isNotBlank() && formData.rate.isNotBlank()
}

class EditBenefitViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val screen = savedStateHandle.toRoute<Screen.EditBenefit>(
        typeMap = mapOf(typeOf<BenefitProperty?>() to BenefitPropertyType)
    )
    private val benefitProperty: BenefitProperty? = screen.benefitProperty
    private val benefitIndex: Int = screen.index

    private var initialSnapshot: BenefitFormData = BenefitFormData()
    private var benefitId: Long = 0L

    private val _uiState = MutableStateFlow(EditBenefitUiState())
    val uiState: StateFlow<EditBenefitUiState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<EditBenefitEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    init {
        if (benefitProperty != null) {
            benefitId = benefitProperty.id
            loadBenefitData(benefitProperty)
        }
    }

    private fun loadBenefitData(benefit: BenefitProperty) {
        _uiState.update { state ->
            val initialFormData = BenefitFormData(
                name = benefit.name,
                explanation = benefit.explanation ?: "",
                capAmount = benefit.capAmount.toString(),
                dailyLimit = benefit.dailyLimit?.toString() ?: "",
                oneTimeLimit = benefit.oneTimeLimit?.toString() ?: "",
                rate = benefit.rate.toString()
            )
            initialSnapshot = initialFormData
            state.copy(
                formData = initialFormData,
                isLoading = false
            )
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
            val currentForm = _uiState.value.formData

            val resultProperty = BenefitProperty(
                id = benefitId,
                name = currentForm.name.trim(),
                explanation = currentForm.explanation.trim().takeIf { it.isNotBlank() },
                capAmount = currentForm.capAmount.toLongOrNull() ?: 0L,
                dailyLimit = currentForm.dailyLimit.toLongOrNull(),
                oneTimeLimit = currentForm.oneTimeLimit.toLongOrNull(),
                rate = currentForm.rate.toFloatOrNull() ?: 0f
            )

            _uiState.update { it.copy(isSaving = false) }
            _eventChannel.send(EditBenefitEvent.SaveSuccess(resultProperty, benefitIndex))
        }
    }
}
