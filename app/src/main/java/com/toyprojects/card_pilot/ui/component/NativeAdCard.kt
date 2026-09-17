package com.toyprojects.card_pilot.ui.component

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.ui.theme.CardPilotColorPalette
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

@Composable
fun NativeAdCard(
    nativeAd: NativeAd?,
    modifier: Modifier = Modifier
) {
    val cornerRadius = 12.dp
    val colors = CardPilotColors

    if (nativeAd != null) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(100.dp)
                .background(colors.surfaceCard, RoundedCornerShape(cornerRadius))
                .border(1.dp, colors.outline, RoundedCornerShape(cornerRadius))
                .clip(RoundedCornerShape(cornerRadius)),
            factory = { ctx ->
                val adView = LayoutInflater.from(ctx).inflate(R.layout.native_ad_layout, null, false) as NativeAdView

                adView.iconView = adView.findViewById(R.id.ad_app_icon)
                adView.headlineView = adView.findViewById(R.id.ad_headline)
                adView.bodyView = adView.findViewById(R.id.ad_body)
                adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)

                adView
            },
            update = { adView ->
                // 1. 광고 객체가 변경되었을 때만 데이터 바인딩 및 SDK에 뷰 등록
                if (adView.tag != nativeAd) {
                    bindAdData(adView, nativeAd)
                }

                // 2. 테마색 반영 (항상 최신 상태 유지)
                applyThemeColors(
                    adView = adView,
                    colors = colors
                )
            }
        )
    } else {
        // 펄스 애니메이션 스켈레톤 UI
        val infiniteTransition = rememberInfiniteTransition()
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.8f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(100.dp)
                .background(colors.surfaceCard.copy(alpha = alpha), RoundedCornerShape(cornerRadius))
                .border(1.dp, colors.outline.copy(alpha = alpha), RoundedCornerShape(cornerRadius))
                .clip(RoundedCornerShape(cornerRadius))
        )
    }
}

private fun bindAdData(adView: NativeAdView, ad: NativeAd) {
    val iconView = adView.iconView as? ImageView
    val headlineView = adView.headlineView as? TextView
    val bodyView = adView.bodyView as? TextView
    val ctaView = adView.callToActionView as? TextView

    if (ad.icon != null) {
        iconView?.setImageDrawable(ad.icon?.drawable)
        iconView?.visibility = android.view.View.VISIBLE
    } else {
        iconView?.visibility = android.view.View.GONE
    }

    headlineView?.text = ad.headline

    if (ad.body != null) {
        bodyView?.text = ad.body
        bodyView?.visibility = android.view.View.VISIBLE
    } else {
        bodyView?.visibility = android.view.View.GONE
    }

    if (ad.callToAction != null) {
        ctaView?.text = ad.callToAction
        ctaView?.visibility = android.view.View.VISIBLE
    } else {
        ctaView?.visibility = android.view.View.GONE
    }

    adView.setNativeAd(ad)
    adView.tag = ad
}

private fun applyThemeColors(
    adView: NativeAdView,
    colors: CardPilotColorPalette
) {
    val headlineView = adView.headlineView as? TextView
    val bodyView = adView.bodyView as? TextView
    val ctaView = adView.callToActionView as? TextView

    headlineView?.setTextColor(colors.textPrimary.toArgb())
    bodyView?.setTextColor(colors.textSecondary.toArgb())

    ctaView?.setTextColor(colors.white.toArgb())
    ctaView?.backgroundTintList = android.content.res.ColorStateList.valueOf(colors.cta.toArgb())
}
