package com.toyprojects.card_pilot.ui.feature.card

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.ui.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CardFormData(
    val cardName: String = "",
    val cardImage: String = "",
    val benefits: List<Benefit> = emptyList()
)

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

    private var initialSnapshot: CardFormData = CardFormData()

    private val _uiState = MutableStateFlow(EditCardUiState(isEdit = _cardId != null))
    val uiState: StateFlow<EditCardUiState> = _uiState.asStateFlow()

    init {
        if (_cardId != null) {
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

            val benefits = benefitRepository.getBenefitsOfCardSync(cardId)

            val initialFormData = CardFormData(
                cardName = card.name,
                cardImage = card.image,
                benefits = benefits
            )
            initialSnapshot = initialFormData

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
            currentState.copy(
                formData = nextFormData,
                isModified = nextFormData != initialSnapshot
            )
        }
    }

    fun updateCardName(name: String) {
        updateFormData { it.copy(cardName = name) }
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
                benefits = currentState.formData.benefits,
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
