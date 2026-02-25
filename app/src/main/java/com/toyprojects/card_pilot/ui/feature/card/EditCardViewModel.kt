package com.toyprojects.card_pilot.ui.feature.card

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.CardInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditCardUiState(
    val cardName: String = "",
    val cardImage: String = "",
    val benefits: List<Benefit> = emptyList(),
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

class EditCardViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val cardRepository: CardRepository
) : ViewModel() {

    // TODO: 화면 진입 시 넘겨받은 초기값이 있으면 세팅
    private val _cardId: Long = 0L

    // TODO: benefits 관련 로직...

    private val _uiState = MutableStateFlow(EditCardUiState())
    val uiState: StateFlow<EditCardUiState> = _uiState.asStateFlow()

    fun updateCardName(name: String) {
        _uiState.update { it.copy(cardName = name) }
    }

    fun saveCard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val currentState = _uiState.value
            val cardInfo = CardInfo(
                id = _cardId,
                name = currentState.cardName,
                image = currentState.cardImage,
                usageAmount = 0L,
                benefits = currentState.benefits,
                displayOrder = cardRepository.getMaxDisplayOrder() + 1
            )

            if (_cardId > 0) {
                cardRepository.updateCard(cardInfo)
            } else {
                cardRepository.insertCard(cardInfo)
            }

            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }
}
