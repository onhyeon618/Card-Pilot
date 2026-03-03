package com.toyprojects.card_pilot.ui.feature.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.CardSimpleInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CardListUiState(
    val cards: List<CardSimpleInfo> = emptyList(),
    val isLoading: Boolean = false
)

class CardListViewModel(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _cards = MutableStateFlow<List<CardSimpleInfo>>(emptyList())
    private val _isLoading = MutableStateFlow(true)

    val uiState: StateFlow<CardListUiState> = combine(_cards, _isLoading) { cards, isLoading ->
        CardListUiState(
            cards = cards,
            isLoading = isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CardListUiState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            cardRepository.getAllCards().collect { cards ->
                _cards.value = cards.sortedBy { it.displayOrder }
                _isLoading.value = false
            }
        }
    }

    private var saveJob: Job? = null

    fun moveCard(fromIndex: Int, toIndex: Int) {
        val currentList = _cards.value.toMutableList()
        if (fromIndex in currentList.indices && toIndex in currentList.indices) {
            val item = currentList.removeAt(fromIndex)
            currentList.add(toIndex, item)
            _cards.value = currentList

            saveJob?.cancel()
            saveJob = viewModelScope.launch {
                delay(500)
                saveCardOrder()
            }
        }
    }

    private fun saveCardOrder() {
        viewModelScope.launch {
            val currentList = _cards.value
            val updatedList = currentList.mapIndexed { index, card ->
                card.copy(displayOrder = index)
            }
            _cards.value = updatedList
            cardRepository.updateCardOrders(updatedList)
        }
    }
}
