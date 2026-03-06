package com.toyprojects.card_pilot.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.model.CardSimpleInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
    private val cardRepository: CardRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _selectedCardId = MutableStateFlow<Long?>(null)
    private val _selectedYearMonth = MutableStateFlow(YearMonth.now())
    private val _isInitialized = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            val keepSelected = settingsRepository.keepSelectedCard.first()
            if (keepSelected) {
                val lastId = settingsRepository.lastViewedCardId.first()
                if (lastId != null) {
                    _selectedCardId.value = lastId
                }
            }
            _isInitialized.value = true
        }

        // 설정이 켜지는 시점에 현재 카드를 저장
        viewModelScope.launch {
            settingsRepository.keepSelectedCard
                .drop(1)
                .collect { isEnabled ->
                    if (isEnabled) {
                        val currentId = uiState.value.selectedCardId
                        if (currentId != null) {
                            settingsRepository.setLastViewedCardId(currentId)
                        }
                    }
                }
        }
    }

    val uiState: StateFlow<HomeUiState> = combine(
        cardRepository.getAllCards(),
        _selectedCardId,
        _selectedYearMonth,
        _isInitialized
    ) { cards, cardId, yearMonth, isInitialized ->
        if (!isInitialized) {
            return@combine HomeUiState(isLoading = true)
        }
        val selectedId = if (cards.any { it.id == cardId }) cardId else cards.firstOrNull()?.id
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
        viewModelScope.launch {
            val shouldPersist = settingsRepository.keepSelectedCard.first()
            if (shouldPersist) {
                settingsRepository.setLastViewedCardId(cardId)
            }
        }
    }

    fun selectMonth(yearMonth: YearMonth) {
        _selectedYearMonth.value = yearMonth
    }
}
