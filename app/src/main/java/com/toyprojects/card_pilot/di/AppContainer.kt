package com.toyprojects.card_pilot.di

import android.content.Context
import com.toyprojects.card_pilot.data.local.AppDatabase
import com.toyprojects.card_pilot.data.repository.CardRepositoryImpl
import com.toyprojects.card_pilot.data.repository.TransactionRepositoryImpl
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository

/**
 * AppContainer provides manual Dependency Injection.
 */
interface AppContainer {
    val cardRepository: CardRepository
    val transactionRepository: TransactionRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database by lazy { AppDatabase.getDatabase(context) }

    override val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(database.cardDao(), database.benefitDao(), database)
    }

    override val transactionRepository: TransactionRepository by lazy {
        TransactionRepositoryImpl(database.transactionDao())
    }
}
