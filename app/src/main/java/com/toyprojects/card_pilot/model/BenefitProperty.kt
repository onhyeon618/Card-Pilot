package com.toyprojects.card_pilot.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class BenefitProperty(
    val id: Long = 0,
    val clientId: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val explanation: String? = null,
    val capAmount: Long,
    val dailyLimit: Long?,
    val oneTimeLimit: Long?,
    val rate: Float,
    val displayOrder: Int = 0,
) : Parcelable
