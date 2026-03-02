package com.toyprojects.card_pilot.ui.feature.card

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.ui.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardFormData(
    val cardName: String = "",
    val cardImage: String = "",
    val benefits: List<BenefitProperty> = emptyList()
) : Parcelable

data class EditCardUiState(
    val formData: CardFormData = CardFormData(),
    val isEdit: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isModified: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
) {
    val isFormValid: Boolean
        get() = formData.cardName.isNotBlank() && formData.benefits.isNotEmpty()
}

class EditCardViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val cardRepository: CardRepository,
    private val benefitRepository: BenefitRepository,
) : ViewModel() {

    private val _cardId: Long? = savedStateHandle.toRoute<Screen.EditCard>().cardId

    private val FORM_DATA_KEY = "card_form_data"
    private val INITIAL_SNAPSHOT_KEY = "initial_snapshot_data"

    private var initialSnapshot: CardFormData
        get() = savedStateHandle[INITIAL_SNAPSHOT_KEY] ?: CardFormData()
        set(value) { savedStateHandle[INITIAL_SNAPSHOT_KEY] = value }

    private val restoredFormData: CardFormData? = savedStateHandle[FORM_DATA_KEY]

    private val _uiState = MutableStateFlow(
        EditCardUiState(
            formData = restoredFormData ?: CardFormData(),
            isEdit = _cardId != null,
            isModified = restoredFormData != null && restoredFormData != initialSnapshot
        )
    )
    val uiState: StateFlow<EditCardUiState> = _uiState.asStateFlow()

    init {
        if (_cardId != null && !savedStateHandle.contains(FORM_DATA_KEY)) {
            loadCardData(_cardId)
        }
    }

    private fun loadCardData(cardId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val card = cardRepository.getCardById(cardId)

            if (card == null) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
                return@launch
            }

            val benefits = benefitRepository.getBenefitPropertiesOfCardSync(cardId)

            val initialFormData = CardFormData(
                cardName = card.name,
                cardImage = card.image,
                benefits = benefits
            )
            initialSnapshot = initialFormData
            savedStateHandle[FORM_DATA_KEY] = initialFormData

            _uiState.update { state ->
                state.copy(
                    formData = initialFormData,
                    isLoading = false
                )
            }
        }
    }

    private fun updateFormData(transform: (CardFormData) -> CardFormData) {
        _uiState.update { currentState ->
            val nextFormData = transform(currentState.formData)
            savedStateHandle[FORM_DATA_KEY] = nextFormData
            currentState.copy(
                formData = nextFormData,
                isModified = nextFormData != initialSnapshot
            )
        }
    }

    fun updateCardName(name: String) {
        updateFormData { it.copy(cardName = name) }
    }

    fun updateBenefit(updatedBenefit: BenefitProperty, benefitIndex: Int) {
        updateFormData { currentFormData ->
            val existingBenefits = currentFormData.benefits.toMutableList()

            if (benefitIndex != -1 && benefitIndex in existingBenefits.indices) {
                existingBenefits[benefitIndex] = updatedBenefit
            } else {
                existingBenefits.add(updatedBenefit)
            }

            currentFormData.copy(benefits = existingBenefits)
        }
    }

    fun saveCard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val currentState = _uiState.value

            val displayOrder = if (_cardId != null) {
                cardRepository.getCardById(_cardId)?.displayOrder ?: 0
            } else {
                cardRepository.getMaxDisplayOrder() + 1
            }

            val cardInfo = CardInfo(
                id = _cardId ?: 0L,
                name = currentState.formData.cardName,
                image = currentState.formData.cardImage,
                usageAmount = 0L,
                benefits = currentState.formData.benefits.mapIndexed { index, property ->
                    // TODO: BenefitProperty로 교체 필요
                    Benefit(
                        id = property.id,
                        name = property.name,
                        explanation = property.explanation,
                        capAmount = property.capAmount,
                        usedAmount = 0L,
                        displayOrder = index + 1
                    )
                },
                displayOrder = displayOrder
            )

            if (_cardId != null) {
                cardRepository.updateCard(cardInfo)
            } else {
                cardRepository.insertCard(cardInfo)
            }

            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }
}
