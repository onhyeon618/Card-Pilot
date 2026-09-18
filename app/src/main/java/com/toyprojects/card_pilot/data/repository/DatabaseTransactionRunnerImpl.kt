package com.toyprojects.card_pilot.data.repository

import androidx.room.withTransaction
import com.toyprojects.card_pilot.data.local.AppDatabase
import com.toyprojects.card_pilot.domain.repository.DatabaseTransactionRunner

class DatabaseTransactionRunnerImpl(
    private val database: AppDatabase
) : DatabaseTransactionRunner {
    override suspend fun <T> invoke(block: suspend () -> T): T {
        return database.withTransaction {
            block()
        }
    }
}
