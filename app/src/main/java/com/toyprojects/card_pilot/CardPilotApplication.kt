package com.toyprojects.card_pilot

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.toyprojects.card_pilot.di.AppContainer
import com.toyprojects.card_pilot.di.DefaultAppContainer

class CardPilotApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        MobileAds.initialize(this) {
            container.nativeAdManager.loadAd()
        }
    }
}
