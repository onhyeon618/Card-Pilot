package com.toyprojects.card_pilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.toyprojects.card_pilot.ui.CardPilotApp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                CardPilotColors.GradientBlue.toArgb(),
                CardPilotColors.GradientBlue.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            CardPilotApp()
        }
    }
}
