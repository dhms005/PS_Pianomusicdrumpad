package com.pianomusicdrumpad.pianokeyboard.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

object AdManager {
    var preloadedBannerAdView: AdView? = null

    fun preloadBannerAd(context: Context, adUnitId: String, adSize: AdSize) {
        if (preloadedBannerAdView != null) return // Already loaded

        val adView = AdView(context)
        adView.adUnitId = adUnitId
        adView.setAdSize(adSize)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                preloadedBannerAdView = adView
                Log.d("AdManager", "Banner ad preloaded successfully.")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("AdManager", "Banner ad failed to preload: ${adError.message}")
            }
        }
    }

    fun getPreloadedAdView(): AdView? = preloadedBannerAdView
}
