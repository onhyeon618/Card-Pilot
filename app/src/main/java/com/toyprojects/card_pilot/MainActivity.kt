package com.toyprojects.card_pilot

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.toyprojects.card_pilot.ui.CardPilotApp

class MainActivity : ComponentActivity() {

    private var currentIntentState by mutableStateOf<Intent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentIntentState = intent
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            CardPilotApp(
                intent = currentIntentState,
                onIntentConsumed = { currentIntentState = null }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIntentState = intent
    }

    override fun onResume() {
        super.onResume()
        // 광고 유효성 검사 및 백그라운드 선탑재 (로딩 UI 노출 최소화)
        (application as CardPilotApplication).container.nativeAdManager.loadAd(forceRefresh = false)
    }
}
