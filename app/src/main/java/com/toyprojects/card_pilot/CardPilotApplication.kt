package com.toyprojects.card_pilot

import android.app.Application
import com.toyprojects.card_pilot.data.AppContainer
import com.toyprojects.card_pilot.data.DefaultAppContainer

class CardPilotApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
