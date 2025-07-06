package com.pianomusicdrumpad.pianokeyboard.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd

class AppOpenAdManager(private val context: Context) : LifecycleObserver {

    private val TAG = "AppOpenAdManager"

    // Test App Open ad unit ID
    private val AD_UNIT_ID = ConstantAd.AD_SPLASH_OPEN


    init {
        // Observe app foreground events
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /** Request an ad with high priority */
    fun loadAndShowAd(activity: Activity, baseCallback: BaseCallback) {
        val request = AdRequest.Builder().build()

        AppOpenAd.load(
            context,
            AD_UNIT_ID,
            request,
//            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d(TAG, "Ad was loaded.")

                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {

                            Log.d(TAG, "onAdDismissedFullScreenContent.")

                            baseCallback.completed()
                            ConstantAd.SPLASH_OPEN = true
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {

                            Log.e(TAG, "onAdFailedToShowFullScreenContent: ${adError.message}")

                            baseCallback.completed()
                            ConstantAd.SPLASH_OPEN = true
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when full screen content is shown
                            Log.d(TAG, "onAdShowedFullScreenContent.")
                            ConstantAd.SPLASH_OPEN = false
                        }
                    }

                    ad.show(activity)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(TAG, "Ad failed to load: ${loadAdError.message}")
                    ConstantAd.SPLASH_OPEN = true
                    baseCallback.completed()
                }
            }
        )
    }


    /** Show app open ad when the app moves to foreground */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        // Load an ad if none is available
//        if (!isAdAvailable()) {
//            loadAd()
//        }
    }
} 