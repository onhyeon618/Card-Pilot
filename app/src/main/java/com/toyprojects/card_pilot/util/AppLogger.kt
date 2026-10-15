package com.toyprojects.card_pilot.util

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.toyprojects.card_pilot.BuildConfig

object AppLogger {
    fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
        try {
            FirebaseCrashlytics.getInstance().log("$tag: $message")
        } catch (_: Exception) {
        }
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable)
        }
        try {
            FirebaseCrashlytics.getInstance().log("$tag: $message")
            if (throwable != null) {
                FirebaseCrashlytics.getInstance().recordException(throwable)
            }
        } catch (_: Exception) {
        }
    }

    fun setUserId(hashedId: String) {
        try {
            FirebaseCrashlytics.getInstance().setUserId(hashedId)
        } catch (_: Exception) {
        }
    }

    fun logEvent(eventName: String, vararg params: Pair<String, String>) {
        try {
            Firebase.analytics.logEvent(eventName) {
                params.forEach { (key, value) ->
                    param(key, value)
                }
            }
        } catch (_: Exception) {
        }
    }
}
