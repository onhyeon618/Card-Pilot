package com.toyprojects.card_pilot.ui.feature.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val card: String = "카드 선택",
    val benefit: String = "혜택 선택",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

class EditTransactionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    // TODO: 화면 진입 시 넘겨받은 초기값이 있으면 세팅

    private val _uiState = MutableStateFlow(EditTransactionUiState())
    val uiState: StateFlow<EditTransactionUiState> = _uiState.asStateFlow()

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

    fun updateCard(card: String) {
        _uiState.update { it.copy(card = card) }
    }

    fun updateBenefit(benefit: String) {
        _uiState.update { it.copy(benefit = benefit) }
    }

    fun saveTransaction() {
        val currentState = _uiState.value
        val amount = currentState.amount.replace(",", "").toLongOrNull() ?: 0L
        val date = LocalDate.parse(currentState.date, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        val time = LocalTime.parse(currentState.time, DateTimeFormatter.ofPattern("HH:mm"))

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            transactionRepository.insertTransaction(
                transaction = Transaction(
                    merchant = currentState.merchant,
                    dateTime = LocalDateTime.of(date, time),
                    amount = amount
                ),
                benefitId = 1L // TODO: 실제 혜택 아이디 전달
            )
            // Transaction save logic goes here
            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }
}
