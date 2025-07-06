package com.pianomusicdrumpad.pianokeyboard.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd

class Admob_Banner_Ad {
    var adView: AdView? = null

    fun loadBanner(context: Context) {
        adView = AdView(context)
        adView?.adUnitId = ConstantAd.CALL_END_SCREEN_BANNER

        val displayMetrics = context.resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels
        val screenWidthDp = (screenWidthPx / (displayMetrics.densityDpi / 160f)).toInt()
        val adSize = AdSize.getLandscapeInlineAdaptiveBannerAdSize(context, screenWidthDp)

//        adView.setAdSize(adSize)

        adView?.setAdSize(adSize)

        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }


    fun showBanner(adContainer: FrameLayout) {
        adContainer.removeAllViews()
        adContainer.addView(adView)
    }

    companion object {

        private var mInstance: Admob_Banner_Ad? = null

        fun getInstance(): Admob_Banner_Ad {
            if (mInstance == null) {
                mInstance = Admob_Banner_Ad()
            }
            return mInstance!!
        }


//        val instance: Admob_Full_AD_New
//            get() {
//                if (mInstance == null) {
//                    mInstance = Admob_Full_AD_New()
//                }
//                return mInstance!!
//            }
    }
}
