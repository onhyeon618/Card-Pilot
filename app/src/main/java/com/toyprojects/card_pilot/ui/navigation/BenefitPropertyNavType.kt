package com.toyprojects.card_pilot.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.toyprojects.card_pilot.model.BenefitProperty
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val BenefitPropertyType = object : NavType<BenefitProperty?>(
    isNullableAllowed = true
) {
    override fun get(bundle: Bundle, key: String): BenefitProperty? {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, BenefitProperty::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): BenefitProperty? {
        if (value == "null") return null
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: BenefitProperty?) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: BenefitProperty?): String {
        return value?.let { Uri.encode(Json.encodeToString(value)) } ?: "null"
    }
}
