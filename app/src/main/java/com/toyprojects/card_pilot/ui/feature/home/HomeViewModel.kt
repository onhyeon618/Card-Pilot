package com.toyprojects.card_pilot.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.model.CardSimpleInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth

data class HomeUiState(
    val cardList: List<CardSimpleInfo> = emptyList(),
    val selectedCardId: Long? = null,
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val cardInfo: CardInfo? = null,
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _selectedCardId = MutableStateFlow<Long?>(null)
    private val _selectedYearMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<HomeUiState> = combine(
        cardRepository.getAllCards(),
        _selectedCardId,
        _selectedYearMonth
    ) { cards, cardId, yearMonth ->
        val selectedId = cardId ?: cards.firstOrNull()?.id
        HomeUiState(
            cardList = cards,
            selectedCardId = selectedId,
            selectedYearMonth = yearMonth,
            isLoading = false
        )
    }.flatMapLatest { state ->
        if (state.selectedCardId != null) {
            cardRepository.getCardWithTotalAmount(state.selectedCardId, state.selectedYearMonth)
                .combine(flowOf(state)) { cardInfo, currentState ->
                    currentState.copy(cardInfo = cardInfo)
                }
        } else {
            flowOf(state)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun selectCard(cardId: Long) {
        _selectedCardId.value = cardId
    }

    fun selectMonth(yearMonth: YearMonth) {
        _selectedYearMonth.value = yearMonth
    }
}
