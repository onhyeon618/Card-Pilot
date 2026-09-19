package com.toyprojects.card_pilot.model

import kotlin.math.roundToLong

data class Benefit(
    val id: Long = 0,
    val name: String,
    val explanation: String? = null,
    val capAmount: Long,
    val usedBenefitAmount: Long, // 사용한 한도
    val usedPaymentAmount: Long, // 실제 결제 금액
    val rate: Float = 0f,
    val displayOrder: Int
) {
    fun getPaymentCapAmount(): Long {
        val isValidRate = !rate.isNaN() && !rate.isInfinite() && rate > 0f && rate <= 100f
        return if (isValidRate) {
            (capAmount * 100.0 / rate.toDouble()).roundToLong()
        } else {
            capAmount
        }
    }
}
