package com.toyprojects.card_pilot.ui.component

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.toyprojects.card_pilot.BuildConfig
import com.toyprojects.card_pilot.util.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicBoolean

class NativeAdManager(private val context: Context) {
    private val _nativeAd = MutableStateFlow<NativeAd?>(null)
    val nativeAd = _nativeAd.asStateFlow()

    private val _isAdLoadFailed = MutableStateFlow(false)
    val isAdLoadFailed = _isAdLoadFailed.asStateFlow()

    private val isLoading = AtomicBoolean(false)
    private var lastLoadTime = 0L
    private val adExpiryMs = java.util.concurrent.TimeUnit.HOURS.toMillis(1)

    fun loadAd(
        adUnitId: String = BuildConfig.NATIVE_AD_UNIT_ID,
        forceRefresh: Boolean = false
    ) {
        // 만료된 광고 파기
        clearAdIfExpired()

        if (_nativeAd.value != null && !forceRefresh) return
        if (!isLoading.compareAndSet(false, true)) return

        _isAdLoadFailed.value = false

        // 새 광고 요청
        requestNewAd(adUnitId)
    }

    private fun clearAdIfExpired() {
        val isExpired = System.currentTimeMillis() - lastLoadTime >= adExpiryMs
        if (isExpired && _nativeAd.value != null) {
            _nativeAd.value?.destroy()
            _nativeAd.value = null
        }
    }

    private fun requestNewAd(adUnitId: String) {
        val adLoader = AdLoader.Builder(context.applicationContext, adUnitId)
            .forNativeAd { ad ->
                _nativeAd.value?.destroy()
                _nativeAd.value = ad
                lastLoadTime = System.currentTimeMillis()
                isLoading.set(false)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    AppLogger.logEvent(
                        "ad_load_failed",
                        "error_code" to adError.code.toString(),
                        "error_domain" to adError.domain
                    )
                    _isAdLoadFailed.value = true
                    isLoading.set(false)
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }
}
