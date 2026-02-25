package com.toyprojects.card_pilot.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.Transaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth

data class BenefitUsageUiState(
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class BenefitUsageViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    // TODO: BenefitUsageScreen 진입 시 argument 로 Benefit를 받고, 해당 Benefit의 id를 여기서 사용
    private val _benefitId: Long = 1L

    private val _selectedYearMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<BenefitUsageUiState> = _selectedYearMonth.flatMapLatest { yearMonth ->
        transactionRepository.getTransactionsForBenefitByMonth(_benefitId, yearMonth)
            .map { transactions ->
                BenefitUsageUiState(
                    selectedYearMonth = yearMonth,
                    transactions = transactions,
                    isLoading = false
                )
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BenefitUsageUiState(isLoading = true)
    )

    fun selectMonth(yearMonth: YearMonth) {
        _selectedYearMonth.value = yearMonth
    }
}
