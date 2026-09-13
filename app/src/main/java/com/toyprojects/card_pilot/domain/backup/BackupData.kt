package com.toyprojects.card_pilot.domain.backup

import com.toyprojects.card_pilot.data.local.AppDatabase
import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
    val version: Int = AppDatabase.SCHEMA_VERSION,
    val cards: List<CardBackup> = emptyList(),
    val settings: SettingsBackup? = null
)

@Serializable
data class CardBackup(
    val id: Long,
    val name: String,
    val image: String,
    val displayOrder: Int,
    val benefits: List<BenefitBackup> = emptyList()
)

@Serializable
data class BenefitBackup(
    val id: Long,
    val name: String,
    val explanation: String?,
    val capAmount: Long,
    val dailyLimit: Long?,
    val oneTimeLimit: Long?,
    val rate: Float,
    val displayOrder: Int,
    val transactions: List<TransactionBackup> = emptyList()
)

@Serializable
data class TransactionBackup(
    val id: Long,
    val merchant: String,
    val dateTime: String, // Serialize as ISO string
    val amount: Long,
    val appliedAmount: Long
)

@Serializable
data class SettingsBackup(
    val theme: String? = null,
    val notiReceiveEnabled: Boolean = false,
    val keepSelectedCard: Boolean = false
)
