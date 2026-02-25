package com.toyprojects.card_pilot.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.toyprojects.card_pilot.CardPilotApplication
import com.toyprojects.card_pilot.ui.feature.benefit.EditBenefitViewModel
import com.toyprojects.card_pilot.ui.feature.card.CardListViewModel
import com.toyprojects.card_pilot.ui.feature.card.EditCardViewModel
import com.toyprojects.card_pilot.ui.feature.home.BenefitUsageViewModel
import com.toyprojects.card_pilot.ui.feature.home.HomeViewModel
import com.toyprojects.card_pilot.ui.feature.transaction.EditTransactionViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                cardPilotApplication().container.cardRepository
            )
        }
        initializer {
            BenefitUsageViewModel(
                cardPilotApplication().container.transactionRepository
            )
        }
        initializer {
            CardListViewModel(
                cardPilotApplication().container.cardRepository
            )
        }
        initializer {
            EditCardViewModel(
                createSavedStateHandle(),
                cardPilotApplication().container.cardRepository
            )
        }
        initializer {
            EditBenefitViewModel(
                createSavedStateHandle()
            )
        }
        initializer {
            EditTransactionViewModel(
                createSavedStateHandle(),
                cardPilotApplication().container.transactionRepository
            )
        }
    }
}

fun CreationExtras.cardPilotApplication(): CardPilotApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as CardPilotApplication)
