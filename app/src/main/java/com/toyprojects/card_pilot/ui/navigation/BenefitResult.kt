package com.toyprojects.card_pilot.ui.navigation

import android.os.Parcelable
import com.toyprojects.card_pilot.model.BenefitProperty
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class BenefitResult(
    val property: BenefitProperty,
    val index: Int
) : Parcelable
