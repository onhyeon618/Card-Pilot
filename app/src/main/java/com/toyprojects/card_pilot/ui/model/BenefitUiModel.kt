package com.toyprojects.card_pilot.ui.model

import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.BenefitDisplayMode

data class BenefitUiModel(
    val id: Long,
    val name: String,
    val explanation: String?,
    val progress: Float,
    val formattedUsedAmount: String,
    val formattedTotalAmount: String,
    val formattedRemainingAmount: String
)

fun Benefit.toUiModel(displayMode: BenefitDisplayMode): BenefitUiModel {
    val displayUsedAmount = when (displayMode) {
        BenefitDisplayMode.PAYMENT -> this.usedPaymentAmount
        BenefitDisplayMode.BENEFIT -> this.usedBenefitAmount
    }
    val displayTotalAmount = when (displayMode) {
        BenefitDisplayMode.PAYMENT -> this.getPaymentCapAmount()
        BenefitDisplayMode.BENEFIT -> this.capAmount
    }

    val progress = calculateProgress(displayUsedAmount, displayTotalAmount)
    val remainingAmount = (displayTotalAmount - displayUsedAmount).coerceAtLeast(0L)

    return BenefitUiModel(
        id = this.id,
        name = this.name,
        explanation = this.explanation,
        progress = progress,
        formattedUsedAmount = "%,d".format(displayUsedAmount),
        formattedTotalAmount = "%,d".format(displayTotalAmount),
        formattedRemainingAmount = "%,d".format(remainingAmount)
    )
}

private fun calculateProgress(usedAmount: Long, totalAmount: Long): Float {
    if (totalAmount > 0L) {
        val currentRatio = usedAmount.toFloat() / totalAmount.toFloat()
        return currentRatio.coerceIn(0f, 1f)
    }
    return 0f
}
