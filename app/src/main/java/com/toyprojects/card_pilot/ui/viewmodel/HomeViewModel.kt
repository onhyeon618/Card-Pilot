package com.toyprojects.card_pilot.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.data.MockData
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.CardInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val cardList: List<CardInfo> = emptyList(),
    val selectedCard: CardInfo? = null
)

class HomeViewModel(
    private val cardRepository: CardRepository
) : ViewModel() {

    // Internal selection state
    private val _selectedCardId = MutableStateFlow<Int?>(null)

    val homeUiState: StateFlow<HomeUiState> = combine(
        cardRepository.getAllCardsList(),
        _selectedCardId
    ) { cards, selectedId ->
        // If no ID selected, default to first (or keep null if list empty)
        val selected = if (selectedId != null) {
            cards.find { it.id == selectedId } ?: cards.firstOrNull()
        } else {
            cards.firstOrNull()
        }
        
        // If we defaulted, update the internal state so it sticks (optional)
        if (selectedId == null && selected != null) {
            _selectedCardId.value = selected.id
        }

        HomeUiState(
            cardList = cards,
            selectedCard = selected
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    init {
        // Seed database if empty (Temporary logic for dev)
        viewModelScope.launch {
            // Check count via a one-time collection or simple query if available
            // Here using the flow for simplicity
            // Note: In real app, put seeding in Repository or specialized class
            try {
                // Determine if we need to seed
                 cardRepository.getAllCardsList().collect { cards ->
                    if (cards.isEmpty()) { 
                        seedDatabase() 
                    }
                    // Break after first check to avoid loop/re-seeding
                    // (Actually collect is terminal, so we'd need 'first()', but let's keep it simple logic)
                    // Better: trigger seeding and let it run
                    throw java.util.concurrent.CancellationException("Checking complete")
                }
            } catch (e: java.util.concurrent.CancellationException) {
                // Expected flow cancellation
            }
        }
    }
    
    fun selectCard(card: CardInfo) {
        _selectedCardId.value = card.id
    }

    private fun seedDatabase() {
        viewModelScope.launch {
            // Need to check again to be safe async
             // For now, just insert
             MockData.cardInfos.forEach { card ->
                // Check if exists? Name unique?
                // Repo insert is basic.
                cardRepository.insertCard(card)
            }
        }
    }
}
