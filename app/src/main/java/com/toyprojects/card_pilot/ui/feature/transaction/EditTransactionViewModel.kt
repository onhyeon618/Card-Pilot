package com.toyprojects.card_pilot.ui.feature.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.BenefitSimpleInfo
import com.toyprojects.card_pilot.model.CardSimpleInfo
import com.toyprojects.card_pilot.model.Transaction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class EditTransactionUiState(
    val amount: String = "",
    val date: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
    val time: String = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
    val merchant: String = "",
    val selectedCard: CardSimpleInfo? = null,
    val selectedBenefit: BenefitSimpleInfo? = null,
    val cards: List<CardSimpleInfo> = emptyList(),
    val benefits: List<BenefitSimpleInfo> = emptyList(),
    val isSaving: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = !isSaving && amount.isNotBlank() && merchant.isNotBlank() && selectedCard != null && selectedBenefit != null
}

sealed interface EditTransactionEvent {
    object SaveSuccess : EditTransactionEvent
}

class EditTransactionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val cardRepository: CardRepository,
    private val benefitRepository: BenefitRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    // TODO: 화면 진입 시 넘겨받은 초기값이 있으면 세팅

    private val _uiState = MutableStateFlow(EditTransactionUiState())
    val uiState: StateFlow<EditTransactionUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<EditTransactionEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        fetchCards()
    }

    private fun fetchCards() {
        viewModelScope.launch {
            cardRepository.getAllCards().collect { cards ->
                _uiState.update { it.copy(cards = cards) }
            }
        }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun updateDate(date: String) {
        _uiState.update { it.copy(date = date) }
    }

    fun updateTime(time: String) {
        _uiState.update { it.copy(time = time) }
    }

    fun updateMerchant(merchant: String) {
        _uiState.update { it.copy(merchant = merchant) }
    }

    fun updateCard(card: CardSimpleInfo) {
        if (_uiState.value.selectedCard?.id == card.id) return

        _uiState.update {
            it.copy(
                selectedCard = card,
                selectedBenefit = null,
                benefits = emptyList()
            )
        }
        fetchBenefitsForCard(card.id)
    }

    private fun fetchBenefitsForCard(cardId: Long) {
        viewModelScope.launch {
            val benefits = benefitRepository.getSimpleBenefitsOfCardSync(cardId)
            _uiState.update { it.copy(benefits = benefits) }
        }
    }

    fun updateBenefit(benefit: BenefitSimpleInfo) {
        if (_uiState.value.selectedBenefit?.id == benefit.id) return

        _uiState.update {
            it.copy(
                selectedBenefit = benefit
            )
        }
    }

    fun saveTransaction() {
        val currentState = _uiState.value
        val amount = currentState.amount.replace(",", "").toLongOrNull() ?: 0L
        val date = LocalDate.parse(currentState.date, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        val time = LocalTime.parse(currentState.time, DateTimeFormatter.ofPattern("HH:mm"))
        val benefitId = currentState.selectedBenefit?.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            transactionRepository.insertTransaction(
                transaction = Transaction(
                    merchant = currentState.merchant,
                    dateTime = LocalDateTime.of(date, time),
                    amount = amount
                ),
                benefitId = benefitId
            )
            // Transaction save logic goes here
            _uiState.update { it.copy(isSaving = false) }
            _eventFlow.emit(EditTransactionEvent.SaveSuccess)
        }
    }
}
