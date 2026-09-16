package com.toyprojects.card_pilot.util

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics

object AppLogger {
    fun d(tag: String, message: String) {
        Log.d(tag, message)
        FirebaseCrashlytics.getInstance().log("$tag: $message")
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
        FirebaseCrashlytics.getInstance().log("$tag: $message")
        throwable?.let {
            FirebaseCrashlytics.getInstance().recordException(it)
        }
    }

    fun setUserId(hashedId: String) {
        FirebaseCrashlytics.getInstance().setUserId(hashedId)
    }

    fun logEvent(eventName: String, vararg params: Pair<String, String>) {
        Firebase.analytics.logEvent(eventName) {
            params.forEach { (key, value) ->
                param(key, value)
            }
        }
    }
}
