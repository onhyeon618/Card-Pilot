package com.toyprojects.card_pilot.ui.feature.settings.model

import androidx.compose.ui.graphics.ImageBitmap

data class CardCompanyApp(
    val displayName: String,
    val packageName: String,
    val icon: ImageBitmap? = null
) {
    companion object {
        val KNOWN_PACKAGES = listOf(
            CardCompanyApp("KB국민카드", "com.kbcard.cxh.appcard"),
            CardCompanyApp("현대카드", "com.hyundaicard.appcard"),
            CardCompanyApp("삼성카드", "kr.co.samsungcard.mpocket"),
            CardCompanyApp("신한카드", "com.shcard.smartpay"),
            CardCompanyApp("롯데카드", "com.lcacApp"),
            CardCompanyApp("하나카드", "com.hanaskcard.paycla"),
            CardCompanyApp("우리카드", "com.wooricard.smartapp"),
            CardCompanyApp("NH농협카드", "nh.smart.nhallonepay"),
        )
    }
}
