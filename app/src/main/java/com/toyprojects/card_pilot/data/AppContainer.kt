package com.toyprojects.card_pilot.data

import android.content.Context
import com.toyprojects.card_pilot.data.database.CardPilotDatabase
import com.toyprojects.card_pilot.data.repository.CardRepositoryImpl
import com.toyprojects.card_pilot.domain.repository.CardRepository

interface AppContainer {
    val cardRepository: CardRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    
    private val database: CardPilotDatabase by lazy {
        CardPilotDatabase.getDatabase(context)
    }

    override val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(
            cardDao = database.cardDao(),
            benefitDao = database.benefitDao(),
            transactionDao = database.transactionDao()
        )
    }
}
