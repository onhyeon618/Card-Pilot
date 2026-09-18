package com.toyprojects.card_pilot.domain.repository

interface DatabaseTransactionRunner {
    suspend operator fun <T> invoke(block: suspend () -> T): T
}
