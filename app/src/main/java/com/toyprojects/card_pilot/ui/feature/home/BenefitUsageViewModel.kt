package com.toyprojects.card_pilot.ui.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.Transaction
import com.toyprojects.card_pilot.ui.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth

data class BenefitUsageUiState(
    val cardId: Long = -1L,
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val benefit: Benefit? = null,
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class BenefitUsageViewModel(
    savedStateHandle: SavedStateHandle,
    private val benefitRepository: BenefitRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val routeArgs = savedStateHandle.toRoute<Screen.BenefitUsage>()
    private val _cardId: Long = routeArgs.cardId
    private val _benefitId: Long = routeArgs.benefitId

    private val _selectedYearMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<BenefitUsageUiState> = _selectedYearMonth.flatMapLatest { yearMonth ->
        val benefitFlow = benefitRepository.getBenefitWithUsage(_benefitId, yearMonth)
        val transactionsFlow = transactionRepository.getTransactionsForBenefitByMonth(_benefitId, yearMonth)

        benefitFlow.flatMapLatest { benefit ->
            transactionsFlow.map { transactions ->
                BenefitUsageUiState(
                    cardId = _cardId,
                    selectedYearMonth = yearMonth,
                    benefit = benefit,
                    transactions = transactions,
                    isLoading = false
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BenefitUsageUiState(isLoading = true)
    )

    fun selectMonth(yearMonth: YearMonth) {
        _selectedYearMonth.value = yearMonth
    }

    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transactionId)
        }
    }
}
