package com.toyprojects.card_pilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.toyprojects.card_pilot.ui.MainScreen
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardPilotTheme {
                MainScreen()
            }
        }
    }
}