package com.toyprojects.card_pilot.ui.feature.card

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.ui.Screen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface EditCardEvent {
    data class ShowSnackbar(val message: String) : EditCardEvent
}

data class CardFormData(
    val cardName: String = "",
    val cardImage: String = "",
    val benefits: List<BenefitProperty> = emptyList()
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

    private val _eventChannel = Channel<EditCardEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

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

            val benefits = benefitRepository.getBenefitPropertiesOfCardSync(cardId)

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

            // 혜택 이름은 Unique 해야 함
            val benefitNames = currentState.formData.benefits.map { it.name }
            if (benefitNames.size != benefitNames.distinct().size) {
                _uiState.update { it.copy(isSaving = false) }
                _eventChannel.send(EditCardEvent.ShowSnackbar("혜택 이름은 중복될 수 없습니다."))
                return@launch
            }

            val displayOrder = if (_cardId != null) {
                cardRepository.getCardById(_cardId)?.displayOrder ?: 0
            } else {
                cardRepository.getMaxDisplayOrder() + 1
            }

            val cardInfo = CardInfo(
                id = _cardId ?: 0L,
                name = currentState.formData.cardName,
                image = currentState.formData.cardImage,
                displayOrder = displayOrder
            )

            val benefits = currentState.formData.benefits.mapIndexed { index, property ->
                BenefitProperty(
                    id = property.id,
                    name = property.name,
                    explanation = property.explanation,
                    capAmount = property.capAmount,
                    dailyLimit = property.dailyLimit,
                    oneTimeLimit = property.oneTimeLimit,
                    rate = property.rate,
                    displayOrder = index + 1
                )
            }

            if (_cardId != null) {
                cardRepository.updateCard(cardInfo, benefits)
            } else {
                cardRepository.insertCard(cardInfo, benefits)
            }

            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }
}
