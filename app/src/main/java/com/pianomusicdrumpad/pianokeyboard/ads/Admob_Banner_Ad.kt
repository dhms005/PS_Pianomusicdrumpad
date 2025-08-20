package com.pianomusicdrumpad.pianokeyboard.ads

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.WindowInsets
import android.widget.FrameLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import androidx.core.graphics.toColorInt

class Admob_Banner_Ad {
    var adView: AdView? = null

    fun loadBanner(
        context: Activity,
        adContainer: FrameLayout,
        adNativeBannerSimmer: Int,
        bannerId: String
    ) {

        val adViewShimmer = context.layoutInflater.inflate(
            adNativeBannerSimmer,
            adContainer,
            false
        ) as ShimmerFrameLayout

        adContainer.addView(adViewShimmer)
        adViewShimmer.startShimmer()

        adView = AdView(context)
        adView?.adUnitId = bannerId


//        adView.setAdSize(adSize)
        adView?.setAdSize(getAdSize(context))

        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)

        adView?.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                adViewShimmer.stopShimmer()
                adContainer.removeAllViews()
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
//                adContainerView.removeAllViews()
                //                adContainerView.setVisibility(View.GONE);
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                // Replace ad container with new ad view.
                adViewShimmer.stopShimmer()
                adContainer.removeAllViews()

                // Create a wrapper FrameLayout to add border
                val borderWrapper = FrameLayout(context).apply {
                    setPadding(2, 2, 2, 2) // Padding inside the border
                    background = GradientDrawable().apply {
                        setStroke(3, "#95BCBBBB".toColorInt()) // Border thickness & color
                        cornerRadius = 0f // Optional rounded corners
                        setColor(Color.WHITE) // Background color inside border
                    }
                    addView(adView)
                }


                adContainer.addView(borderWrapper)
                // [END create_ad_view]
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

    }


    fun loadBigBanner(
        context: Activity,
        adContainer: FrameLayout,
        adNativeBannerSimmer: Int,
        bannerId: String
    ) {

        val adViewShimmer = context.layoutInflater.inflate(
            adNativeBannerSimmer,
            adContainer,
            false
        ) as ShimmerFrameLayout

        adContainer.addView(adViewShimmer)
        adViewShimmer.startShimmer()

        adView = AdView(context)
        adView?.adUnitId = bannerId


        val displayMetrics = context.resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels
        val screenWidthDp = (screenWidthPx / (displayMetrics.densityDpi / 160f)).toInt()
        val adSize = AdSize.getLandscapeInlineAdaptiveBannerAdSize(context, screenWidthDp)

        adView?.setAdSize(adSize)
        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)

        adView?.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                adViewShimmer.stopShimmer()
                adContainer.removeAllViews()
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
//                adContainerView.removeAllViews()
                //                adContainerView.setVisibility(View.GONE);
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                // Replace ad container with new ad view.
                adViewShimmer.stopShimmer()
                adContainer.removeAllViews()

                // Create a wrapper FrameLayout to add border
                val borderWrapper = FrameLayout(context).apply {
                    setPadding(2, 2, 2, 2) // Padding inside the border
                    background = GradientDrawable().apply {
                        setStroke(3, "#95BCBBBB".toColorInt()) // Border thickness & color
                        cornerRadius = 0f // Optional rounded corners
                        setColor(Color.WHITE) // Background color inside border
                    }
                    addView(adView)
                }


                adContainer.addView(borderWrapper)
                // [END create_ad_view]
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

    }

    // Get the ad size with screen width.
//    private fun getAdSize(context: Activity): AdSize {
//        val displayMetrics = context.resources.displayMetrics
//        var adWidthPixels = displayMetrics.widthPixels
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val windowMetrics = context.windowManager.currentWindowMetrics
//            adWidthPixels = windowMetrics.bounds.width()
//        }
//        val density = displayMetrics.density
//        val adWidth = (adWidthPixels / density).toInt()
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
//    }

    //    @SuppressLint("ObsoleteSdkInt")
    private fun getAdSize(context: Activity): AdSize {
        val windowManager = context.windowManager
        val displayMetrics = context.resources.displayMetrics
        var adWidthPixels: Int

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(
                    WindowInsets.Type.navigationBars()
                            or WindowInsets.Type.displayCutout()
                )
            val bounds = windowMetrics.bounds
            adWidthPixels = bounds.width() - insets.left - insets.right
        } else {
            // Fallback for < API 30
            adWidthPixels = displayMetrics.widthPixels
        }

        val density = displayMetrics.density
        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
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
