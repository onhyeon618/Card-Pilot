package com.toyprojects.card_pilot.ui.feature.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.BenefitSimpleInfo
import com.toyprojects.card_pilot.model.CardSimpleInfo
import com.toyprojects.card_pilot.model.Transaction
import com.toyprojects.card_pilot.ui.Screen
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

data class TransactionFormData(
    val amount: String = "",
    val date: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
    val time: String = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
    val merchant: String = "",
    val selectedCard: CardSimpleInfo? = null,
    val selectedBenefit: BenefitSimpleInfo? = null
)

data class EditTransactionUiState(
    val formData: TransactionFormData = TransactionFormData(),
    val cards: List<CardSimpleInfo> = emptyList(),
    val benefits: List<BenefitSimpleInfo> = emptyList(),
    val isEditMode: Boolean = false,
    val isSaving: Boolean = false,
    val isModified: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = !isSaving && formData.amount.isNotBlank() && formData.merchant.isNotBlank() && formData.selectedCard != null && formData.selectedBenefit != null
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

    private val routeArgs = savedStateHandle.toRoute<Screen.EditTransaction>()

    private var initialSnapshot: TransactionFormData = TransactionFormData()

    private val _uiState = MutableStateFlow(EditTransactionUiState())
    val uiState: StateFlow<EditTransactionUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<EditTransactionEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        fetchCards()
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch {
            val card = cardRepository.getCardById(routeArgs.initialCardId)
            val benefits = card?.let { benefitRepository.getSimpleBenefitsOfCardSync(it.id) } ?: emptyList()

            var initialFormData = TransactionFormData(
                selectedCard = card,
                selectedBenefit = benefits.find { b -> b.id == routeArgs.initialBenefitId }
            )

            if (routeArgs.transactionId != null) {
                // 기존 지출 내역 수정 시
                val transaction = transactionRepository.getTransactionById(routeArgs.transactionId)
                if (transaction != null) {
                    initialFormData = initialFormData.copy(
                        amount = transaction.amount.toString(),
                        date = transaction.dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                        time = transaction.dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        merchant = transaction.merchant
                    )
                }
            }

            initialSnapshot = initialFormData

            _uiState.update {
                it.copy(
                    formData = initialFormData,
                    benefits = benefits,
                    isEditMode = routeArgs.transactionId != null
                )
            }
        }
    }

    private fun fetchCards() {
        viewModelScope.launch {
            cardRepository.getAllCards().collect { cards ->
                _uiState.update { it.copy(cards = cards) }
            }
        }
    }

    private fun updateFormData(transform: (TransactionFormData) -> TransactionFormData) {
        _uiState.update { currentState ->
            val nextFormData = transform(currentState.formData)
            currentState.copy(
                formData = nextFormData,
                isModified = nextFormData != initialSnapshot
            )
        }
    }

    fun updateAmount(amount: String) {
        updateFormData { it.copy(amount = amount) }
    }

    fun updateDate(date: String) {
        updateFormData { it.copy(date = date) }
    }

    fun updateTime(time: String) {
        updateFormData { it.copy(time = time) }
    }

    fun updateMerchant(merchant: String) {
        updateFormData { it.copy(merchant = merchant) }
    }

    fun updateCard(card: CardSimpleInfo) {
        if (_uiState.value.formData.selectedCard?.id == card.id) return

        updateFormData {
            it.copy(
                selectedCard = card,
                selectedBenefit = null
            )
        }

        _uiState.update { it.copy(benefits = emptyList()) }
        fetchBenefitsForCard(card.id)
    }

    private fun fetchBenefitsForCard(cardId: Long) {
        viewModelScope.launch {
            val benefits = benefitRepository.getSimpleBenefitsOfCardSync(cardId)
            _uiState.update { it.copy(benefits = benefits) }
        }
    }

    fun updateBenefit(benefit: BenefitSimpleInfo) {
        if (_uiState.value.formData.selectedBenefit?.id == benefit.id) return

        updateFormData {
            it.copy(
                selectedBenefit = benefit
            )
        }
    }

    fun saveTransaction() {
        val currentState = _uiState.value
        val form = currentState.formData
        val amount = form.amount.replace(",", "").toLongOrNull() ?: 0L
        val date = LocalDate.parse(form.date, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        val time = LocalTime.parse(form.time, DateTimeFormatter.ofPattern("HH:mm"))
        val benefitId = form.selectedBenefit?.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            transactionRepository.insertTransaction(
                transaction = Transaction(
                    merchant = form.merchant,
                    dateTime = LocalDateTime.of(date, time),
                    amount = amount
                ),
                benefitId = benefitId
            )

            _uiState.update { it.copy(isSaving = false) }
            _eventFlow.emit(EditTransactionEvent.SaveSuccess)
        }
    }
}
