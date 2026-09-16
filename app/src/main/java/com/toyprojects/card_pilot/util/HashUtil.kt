package com.toyprojects.card_pilot.util

import java.security.MessageDigest

object HashUtil {
    fun sha256(input: String): String? {
        return try {
            val bytes = input.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            digest.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            null
        }
    }
}
