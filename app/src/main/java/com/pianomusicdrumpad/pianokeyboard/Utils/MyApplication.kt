package com.pianomusicdrumpad.pianokeyboard.Utils

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.google.android.gms.ads.initialization.InitializationStatus
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.init
import com.pianomusicdrumpad.pianokeyboard.ads.GoogleMobileAdsConsentManager
import java.util.Date


class MyApplication : Application(), ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private var appOpenAdManager: AppOpenAdManager? = null
    private var currentActivity: Activity? = null

    override fun onCreate() {

        super<Application>.onCreate()
        mInstance = this
        appContext = this
//        super.onCreate()

        init(applicationContext)

        this.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        appOpenAdManager = AppOpenAdManager()

        MobileAds.initialize(
            this
        ) { initializationStatus: InitializationStatus? ->
            if (!SharePrefUtils.getBoolean(
                    ConstantAd.IS_PURCHASE, false
                )
            ) {
                appOpenAdManager!!.loadAd(this, false)
            }
        }
    }


    override fun onActivityStarted(activity: Activity) {
        if (!appOpenAdManager!!.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
        Log.e("Foreground", "************* onMoveToForeground")
        if (ConstantAd.SPLASH_OPEN) {
            if (!SharePrefUtils.getBoolean(
                    ConstantAd.IS_PURCHASE, false
                )
            ) {
                appOpenAdManager!!.showAdIfAvailable(currentActivity!!)
            }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
        Log.e("Foreground", "************* onMoveToForeground")
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    }

    fun loadAd(activity: Activity) {
        if (!SharePrefUtils.getBoolean(
                ConstantAd.IS_PURCHASE, false
            )
        ) {
            appOpenAdManager!!.loadAd(activity, false)
        }
    }

    fun showAdIfAvailable(
        activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener
    ) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager!!.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete (i.e.
     * dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    /**
     * Inner class that loads and shows app open ads.
     */
    private inner class AppOpenAdManager
    /**
     * Constructor.
     */
    {
        //        private static final String AD_UNIT_ID = Constant_ad.AD_SPLASH_OPEN;
        private val googleMobileAdsConsentManager: GoogleMobileAdsConsentManager =
            GoogleMobileAdsConsentManager.getInstance(
                applicationContext
            )
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd: Boolean = false

        private var isLoadedAd = false

        /**
         * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
         */
        private var loadTime: Long = 0

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        fun loadAd(context: Context, isFirstTime: Boolean) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable) {
                return
            }

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
//                if (isFirstTime) ConstantAd.AD_SPLASH_OPEN else ConstantAd.AD_APP_OPEN,
                ConstantAd.AD_APP_OPEN,
                request,
                object : AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = (Date()).time
                        isLoadedAd = true
                        Log.d(Companion.LOG_TAG, "onAdLoaded.")
                        //                            Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
                        Log.d(Companion.LOG_TAG, "onAdFailedToLoad: " + loadAdError.message)
                        //                            Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                    }
                })
        }

        /**
         * Check if ad was loaded more than n hours ago.
         */
        fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference = (Date()).time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return (dateDifference < (numMilliSecondsPerHour * numHours))
        }

        val isAdAvailable: Boolean
            /**
             * Check if ad exists and can be shown.
             */
            get() =// Ad references in the app open beta will time out after four hours, but this time limit
            // may change in future beta versions. For details, see:
                // https://support.google.com/admob/answer/9341964?hl=en
                appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity                 the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener = object : OnShowAdCompleteListener {
                override fun onShowAdComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }
            }
        ) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(Companion.LOG_TAG, "The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable) {
                Log.d(Companion.LOG_TAG, "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                if (googleMobileAdsConsentManager.canRequestAds()) {
                    currentActivity?.let { loadAd(it, false) }
                }
                return
            }

            Log.d(Companion.LOG_TAG, "Will show ad.")

            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                /** Called when full screen content is dismissed.  */
                override fun onAdDismissedFullScreenContent() {
                    // Set the reference to null so isAdAvailable() returns false.
                    appOpenAd = null
                    isShowingAd = false

                    Log.d(
                        Companion.LOG_TAG,
                        "onAdDismissedFullScreenContent."
                    )

                    //                            Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();
                    onShowAdCompleteListener.onShowAdComplete()
                    if (googleMobileAdsConsentManager.canRequestAds()) {
                        loadAd(activity, false)
                    }
                }

                /** Called when fullscreen content failed to show.  */
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false

                    Log.d(
                        Companion.LOG_TAG,
                        "onAdFailedToShowFullScreenContent: " + adError.message
                    )

                    //                            Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
                    //                                    .show();
                    onShowAdCompleteListener.onShowAdComplete()
                    if (googleMobileAdsConsentManager.canRequestAds()) {
                        loadAd(activity, false)
                    }
                }

                /** Called when fullscreen content is shown.  */
                override fun onAdShowedFullScreenContent() {
                    Log.d(Companion.LOG_TAG, "onAdShowedFullScreenContent.")
                    //                            Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();
                }
            }

            isShowingAd = true
            appOpenAd!!.show(activity)
        }
    }


    companion object {
        private const val TAG = "MyApplication"

        var appContext: Context? = null
            private set
        private var mInstance: MyApplication? = null
        private const val LOG_TAG = "AppOpenAdManager"

        @get:Synchronized
        val instance: MyApplication
            get() {
                if (mInstance == null) {
                    mInstance = MyApplication()
                }
                return mInstance!!
            }
    }

}
