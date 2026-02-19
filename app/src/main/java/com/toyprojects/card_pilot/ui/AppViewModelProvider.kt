package com.toyprojects.card_pilot.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.toyprojects.card_pilot.CardPilotApplication
import com.toyprojects.card_pilot.ui.viewmodel.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                cardPilotApplication().container.cardRepository
            )
        }
    }
}

fun CreationExtras.cardPilotApplication(): CardPilotApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CardPilotApplication)
