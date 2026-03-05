package com.toyprojects.card_pilot.ui.feature.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.CardSimpleInfo
import com.toyprojects.card_pilot.model.Transaction
import com.toyprojects.card_pilot.ui.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class TransactionFormData(
    val amount: String = "",
    val date: String = LocalDate.now().format(TransactionFormData.DATE_FORMATTER),
    val time: String = LocalTime.now().format(TransactionFormData.TIME_FORMATTER),
    val merchant: String = "",
    val selectedCard: CardSimpleInfo? = null,
    val selectedBenefit: BenefitProperty? = null
) {
    companion object {
        const val DATE_FORMAT_PATTERN = "yyyy.MM.dd"
        const val TIME_FORMAT_PATTERN = "HH:mm"
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)
        val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN)
    }
}

data class EditTransactionUiState(
    val formData: TransactionFormData = TransactionFormData(),
    val cards: List<CardSimpleInfo> = emptyList(),
    val benefits: List<BenefitProperty> = emptyList(),
    val isEditMode: Boolean = false,
    val isSaving: Boolean = false,
    val isModified: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = !isSaving && formData.amount.isNotBlank() && formData.merchant.isNotBlank() && formData.selectedCard != null && formData.selectedBenefit != null
}

sealed interface EditTransactionEvent {
    object SaveSuccess : EditTransactionEvent
    data class SaveError(val message: String) : EditTransactionEvent
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
            val benefits = card?.let { benefitRepository.getBenefitPropertiesOfCardSync(it.id) } ?: emptyList()

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
                        date = transaction.dateTime.toLocalDate().format(
                            TransactionFormData.DATE_FORMATTER
                        ),
                        time = transaction.dateTime.toLocalTime().format(
                            TransactionFormData.TIME_FORMATTER
                        ),
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
            val benefits = benefitRepository.getBenefitPropertiesOfCardSync(cardId)
            _uiState.update { it.copy(benefits = benefits) }
        }
    }

    fun updateBenefit(benefit: BenefitProperty) {
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
        val date = LocalDate.parse(form.date, TransactionFormData.DATE_FORMATTER)
        val time = LocalTime.parse(form.time, TransactionFormData.TIME_FORMATTER)
        val benefitProperty = form.selectedBenefit ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            try {
                // 혜택 적용 가능 금액 계산
                val yearMonth = java.time.YearMonth.from(date)
                val transactions =
                    transactionRepository.getTransactionsForBenefitByMonth(benefitProperty.id, yearMonth).first()
                val (monthAppliedSum, todayAppliedSum) = calculateSums(transactions, date)

                // 1. 이번달 잔여 혜택
                var limit = maxOf(0L, benefitProperty.capAmount - monthAppliedSum)

                // 2. 일일 제한이 있는 경우, 일별 잔여 혜택과 비교
                if (benefitProperty.dailyLimit != null) {
                    limit = minOf(limit, maxOf(0L, benefitProperty.dailyLimit - todayAppliedSum))
                }

                // 3. 1회 제한이 있는 경우, 1회 최대 혜택과 비교
                if (benefitProperty.oneTimeLimit != null) {
                    limit = minOf(limit, benefitProperty.oneTimeLimit)
                }

                // 4. 실제 지출 금액과 한도 금액 비교
                val appliedAmount = minOf(amount, limit)

                val dateTime = LocalDateTime.of(date, time)
                val transaction = Transaction(
                    id = routeArgs.transactionId ?: 0L,
                    merchant = form.merchant,
                    dateTime = dateTime,
                    amount = amount,
                    appliedAmount = appliedAmount
                )

                if (routeArgs.transactionId != null) {
                    transactionRepository.updateTransaction(transaction, benefitProperty.id)
                } else {
                    transactionRepository.insertTransaction(transaction, benefitProperty.id)
                }

                _eventFlow.emit(EditTransactionEvent.SaveSuccess)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(EditTransactionEvent.SaveError("일시적인 오류가 발생했습니다."))
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun calculateSums(transactions: List<Transaction>, targetDate: LocalDate): Pair<Long, Long> {
        var monthAppliedSum = 0L
        var todayAppliedSum = 0L

        for (t in transactions) {
            // 지출 내역을 수정 중인 경우, 현재 내역의 적용 금액은 계산에서 제외
            if (routeArgs.transactionId != null && t.id == routeArgs.transactionId) {
                continue
            }

            monthAppliedSum += t.appliedAmount
            if (t.dateTime.toLocalDate() == targetDate) {
                todayAppliedSum += t.appliedAmount
            }
        }

        return Pair(monthAppliedSum, todayAppliedSum)
    }
}

