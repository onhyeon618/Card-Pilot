package com.toyprojects.card_pilot.ui.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.domain.usecase.DeleteTransactionUseCase
import com.toyprojects.card_pilot.model.BenefitDisplayMode
import com.toyprojects.card_pilot.model.Transaction
import com.toyprojects.card_pilot.ui.Screen
import com.toyprojects.card_pilot.ui.model.BenefitUiModel
import com.toyprojects.card_pilot.ui.model.toUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth

data class BenefitUsageUiState(
    val cardId: Long = -1L,
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val benefit: BenefitUiModel? = null,
    val transactions: List<Transaction> = emptyList(),
    val amountDisplayMode: BenefitDisplayMode = BenefitDisplayMode.BENEFIT,
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class BenefitUsageViewModel(
    savedStateHandle: SavedStateHandle,
    private val benefitRepository: BenefitRepository,
    private val transactionRepository: TransactionRepository,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val routeArgs = savedStateHandle.toRoute<Screen.BenefitUsage>()
    private val _cardId: Long = routeArgs.cardId
    private val _benefitId: Long = routeArgs.benefitId

    private val _selectedYearMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<BenefitUsageUiState> = _selectedYearMonth.flatMapLatest { yearMonth ->
        combine(
            benefitRepository.getBenefitWithUsage(_benefitId, yearMonth),
            transactionRepository.getTransactionsForBenefitByMonth(_benefitId, yearMonth),
            settingsRepository.amountDisplayMode
        ) { benefitWithUsedAmount, transactions, amountDisplayMode ->
            BenefitUsageUiState(
                cardId = _cardId,
                selectedYearMonth = yearMonth,
                benefit = benefitWithUsedAmount?.toUiModel(amountDisplayMode),
                transactions = transactions,
                amountDisplayMode = amountDisplayMode,
                isLoading = false
            )
        }
    }.flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BenefitUsageUiState(isLoading = true)
        )

    fun selectMonth(yearMonth: YearMonth) {
        _selectedYearMonth.value = yearMonth
    }

    fun toggleAmountDisplayMode() {
        viewModelScope.launch {
            settingsRepository.toggleAmountDisplayMode()
        }
    }

    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            deleteTransactionUseCase(transactionId, _benefitId)
        }
    }
}
