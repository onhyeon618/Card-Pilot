package com.toyprojects.card_pilot.ui.feature.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.CardSimpleInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CardListUiState(
    val cards: List<CardSimpleInfo> = emptyList(),
    val isLoading: Boolean = false
)

class CardListViewModel(
    private val cardRepository: CardRepository
) : ViewModel() {

    val uiState: StateFlow<CardListUiState> = cardRepository.getAllCards()
        .map { cards ->
            CardListUiState(
                cards = cards,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CardListUiState(isLoading = true)
        )
}
