package com.pianomusicdrumpad.pianokeyboard.language

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.ump.FormError
import com.google.firebase.analytics.FirebaseAnalytics
import com.pianomusicdrumpad.pianokeyboard.Activities.AgreeScreenActivity
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.PulsatorLayout
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility
import com.pianomusicdrumpad.pianokeyboard.ads.GoogleMobileAdsConsentManager
import com.pianomusicdrumpad.pianokeyboard.ads.MainInterfaceV2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class LanguageActivitySplash : AppCompatActivity() {
    private var languageRecyclerView: RecyclerView? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var languageAdapter: LanguageAdapter? = null
    private val languageName = arrayOf(
        "English",
        "Chinese",
        "Spanish",
        "French",
        "Hindi",
        "Indonesian",
        "Russian",
        "German",
        "Portuguese"
    )
    private val languageImg = intArrayOf(
        R.drawable.lan_usa,
        R.drawable.lan_china,
        R.drawable.lan_spain,
        R.drawable.lan_france,
        R.drawable.lan_india,
        R.drawable.lan_indonesia,
        R.drawable.lan_russia,
        R.drawable.lan_germany,
        R.drawable.lan_portugal
    )
    private val lanCode = arrayOf("en", "zh", "es", "fr", "hi", "in", "ru", "de", "pt")
    private var selectedLan = "en"

    //    var resources: Resources? = null
//    private val mContext: Context? = null
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    private var nativeAd: NativeAd? = null
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("shownLang", "onCreate: Language shown")
//        if ((SharePrefUtils.getString(ConstantAd.AD_NAV_BAR, "1") == "0")) {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        }

        if (SharePrefUtils.getString(ConstantAd.navBarEnableLanguageScreen, "0") == "1") {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_cm_language)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        findViewById<View>(R.id.back).visibility = View.GONE
        (findViewById<View>(R.id.back) as ImageView).setOnClickListener { onBackPressed() }
        SharePrefUtils.putString(
            ConstantAd.LANGUAGE_CODE_FOR_SELECTION,
            SharePrefUtils.getString(ConstantAd.LANGUAGE_CODE, "en")
        )
        languageRecyclerView = findViewById(R.id.rec_language)
        languageRecyclerView?.layoutManager = LinearLayoutManager(this)
        languageAdapter = LanguageAdapter(
            this,
            languageName,
            languageImg,
            object : LanguageAdapter.OnItemClickListener {
                override fun onItemClick(item: Int) {
                    selectedLan = lanCode[item]
                    SharePrefUtils.putString(ConstantAd.LANGUAGE_CODE_FOR_SELECTION, selectedLan)
                    languageAdapter!!.notifyDataSetChanged()
                    Log.e(
                        "getLAnUpdate", (selectedLan + "--" +
                                SharePrefUtils.getString(
                                    ConstantAd.LANGUAGE_CODE_FOR_SELECTION,
                                    "en"
                                ))
                    )
                }
            })
        languageRecyclerView?.adapter = languageAdapter
        findViewById<View>(R.id.donenext).setOnClickListener { doneSelected() }
        findViewById<View>(R.id.tx_done).setOnClickListener { doneSelected() }
        val pulsator = findViewById<View>(R.id.pulsator) as PulsatorLayout
        pulsator.start()
        googleMobileAdsConsentManager =
            GoogleMobileAdsConsentManager.getInstance(applicationContext)
        googleMobileAdsConsentManager.gatherConsent(
            this
        ) { consentError: FormError? ->
            if (consentError != null) {
                // Consent not obtained in current session.
                Log.w(
                    "TAG",
                    String.format(
                        "%s: %s",
                        consentError.errorCode,
                        consentError.message
                    )
                )
            }
            if (googleMobileAdsConsentManager.canRequestAds()) {
                initializeMobileAdsSdk()
            }
            if (googleMobileAdsConsentManager.isPrivacyOptionsRequired) {
                // Regenerate the options menu to include a privacy setting.
                invalidateOptionsMenu()
            }
        }

        // This sample attempts to load ads using consent obtained in the previous session.
        if (googleMobileAdsConsentManager.canRequestAds()) {
            initializeMobileAdsSdk()
        }

//        loadInter()
    }

    override fun onResume() {
        super.onResume()
        Utility.setLocale(this, SharePrefUtils.getString(ConstantAd.LANGUAGE_CODE, "en"))
        SharePrefUtils.putString(ConstantAd.AD_CHECK_RESUME, "1")
        ConstantAd.SPLASH_OPEN = true
    }

    override fun onPause() {
        super.onPause()
        SharePrefUtils.putString(ConstantAd.AD_CHECK_RESUME, "0")
    }

    override fun onBackPressed() {
//        SharePrefUtils.putString(ConstantAd.LANGUAGE_CODE, "en")
//        SharePrefUtils.putString(ConstantAd.OPEN_APP_FIRST_TIME, "1")
//        Utility.setLocale(
//            this@LanguageActivitySplash,
//            SharePrefUtils.getString(ConstantAd.LANGUAGE_CODE, "en")
//        )
//        if (MySharedPre.getBooleanCALLENDSHOW(this@LanguageActivitySplash, MySharedPre.CALL_END_SHOW)) {
//            if (!permissionCheck()) {
//                val i = Intent(this@LanguageActivitySplash, PermissionScreenOne::class.java)
//                i.putExtra("activityFrom", "permissionPhone")
//                i.putExtra("intentfrom", "on back language")
//                startActivity(i)
//                finish()
//            } else {
//                val i = Intent(this@LanguageActivitySplash, HomePagerActivity::class.java)
//                startActivity(i)
//                finish()
//            }
//        } else {
//            val i = Intent(this@LanguageActivitySplash, HomePagerActivity::class.java)
//            startActivity(i)
//            finish()
//        }
        finish()
    }

    private fun doneSelected() {
        SharePrefUtils.putString(ConstantAd.LANGUAGE_CODE, selectedLan)
        SharePrefUtils.putString(ConstantAd.OPEN_APP_FIRST_TIME, "1")
        Utility.setLocale(
            this@LanguageActivitySplash,
            SharePrefUtils.getString(ConstantAd.LANGUAGE_CODE, "en")
        )
//        startActivity(Intent(this@LanguageActivitySplash, HomePagerActivity::class.java))
//        putBoolean(ConstantAd.FEATURE_SCREEN, true)
        SharePrefUtils.putString(ConstantAd.LANGUAGE_CODE, selectedLan)
        SharePrefUtils.putString(ConstantAd.OPEN_APP_FIRST_TIME, "1")
        Utility.setLocale(
            this@LanguageActivitySplash,
            SharePrefUtils.getString(ConstantAd.LANGUAGE_CODE, "en")
        )
//        startActivity(Intent(this@LanguageActivitySplash, HomePagerActivity::class.java))
//        putBoolean(ConstantAd.FEATURE_SCREEN, true)
        val i = Intent(this@LanguageActivitySplash, AgreeScreenActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        // Set your test devices.
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder() //                        .setTestDeviceIds(Arrays.asList(TEST_DEVICE_HASHED_ID))
                .build()
        )

        CoroutineScope(Dispatchers.IO).launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@LanguageActivitySplash) {}
            runOnUiThread {
                // Load an ad on the main thread.
                if (!SharePrefUtils.getBoolean(ConstantAd.IS_PURCHASE, false)) {
//                    refreshAd()
                    loadBanner()
                }
            }
        }

//        Thread(
//            {
//
//                // Initialize the Google Mobile Ads SDK on a background thread.
//                MobileAds.initialize(
//                    this,
//                    OnInitializationCompleteListener { initializationStatus: InitializationStatus? -> })
//
//                // Load an ad on the main thread.
//                runOnUiThread(Runnable { refreshAd() })
//            })
//            .start()
    }

    private fun loadBanner() {
        MainInterfaceV2.loadBanner(
            this,
            findViewById(R.id.Admob_Native_Frame_two),
            adNativeBannerSimmer = R.layout.ad_native_adptive_banner_simmer,
            bannerId = ConstantAd.AD_LANG_BANNER
        )
    }


    private fun refreshAd() {
        val frameLayout = findViewById<FrameLayout>(R.id.Admob_Native_Frame_two)
        val adViewShimmer = layoutInflater.inflate(
            R.layout.ad_native_media_simmer,
            frameLayout,
            false
        ) as ShimmerFrameLayout
        frameLayout.removeAllViews()
        frameLayout.addView(adViewShimmer)
        adViewShimmer.startShimmer()
        //
        val builder = AdLoader.Builder(this, ConstantAd.AD_LANG_BANNER_NATIVE)

        // OnLoadedListener implementation.
        builder.forNativeAd { nativeAd: NativeAd ->
            var isDestroyed: Boolean = false
            isDestroyed = isDestroyed()
            if (isDestroyed || isFinishing || isChangingConfigurations) {
                nativeAd.destroy()
                return@forNativeAd
            }
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            if (this@LanguageActivitySplash.nativeAd != null) {
                this@LanguageActivitySplash.nativeAd!!.destroy()
            }
            this@LanguageActivitySplash.nativeAd = nativeAd
            adViewShimmer.stopShimmer()

//                    FrameLayout frameLayout = findViewById(R.id.Admob_Native_Frame_two);
            val adView: NativeAdView = layoutInflater.inflate(
                R.layout.ad_native_media,
                frameLayout,
                false
            ) as NativeAdView
            populateNativeAdView(nativeAd, adView)
            frameLayout.removeAllViews()
            frameLayout.addView(adView)
        }
        val adLoader = builder
            .withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        frameLayout.removeAllViews()
                    }
                })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {

        // Set the media view.
        adView.mediaView = adView.findViewById<View>(R.id.ad_media) as MediaView

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        (adView.headlineView as TextView?)!!.text = nativeAd.headline
        adView.mediaView!!.mediaContent = nativeAd.mediaContent
        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView?)!!.text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as TextView?)!!.text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView!!.visibility = View.GONE
        } else {
            (adView.iconView as ImageView?)!!.setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
    }

    override fun onDestroy() {
        if (nativeAd != null) {
            nativeAd!!.destroy()
        }
        super.onDestroy()
    }

//    private fun loadInter() {
//
////        adLoadDialog!!.show()
//
//        val adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(
//            this,
//            ConstantAd.AD_LANGUAGE_INTER,
//            adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
////                    adError?.toString()?.let { Log.d(TAG, it) }
//                    mInterstitialAd = null
////                    adLoadDialog!!.dismiss()
////                    openSetting()
//                }
//
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
////                    Log.d(TAG, "Ad was loaded.")
//                    mInterstitialAd = interstitialAd
////                    adLoadDialog!!.dismiss()
////                    showInterstitial()
//                }
//
//
//            })
//    }
//
//    // Show the ad if it's ready. Otherwise restart the game.
//    private fun showInterstitial() {
//        if (mInterstitialAd != null) {
//            mInterstitialAd?.fullScreenContentCallback =
//                object : FullScreenContentCallback() {
//                    override fun onAdDismissedFullScreenContent() {
////                        Log.d(TAG, "Ad was dismissed.")
//                        // Don't forget to set the ad reference to null so you
//                        // don't show the ad a second time.
//                        mInterstitialAd = null
//                        doneSelected()
//                        loadInter()
//                    }
//
//                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
////                        Log.d(TAG, "Ad failed to show.")
//                        // Don't forget to set the ad reference to null so you
//                        // don't show the ad a second time.
//                        mInterstitialAd = null
//                        doneSelected()
//                    }
//
//                    override fun onAdShowedFullScreenContent() {
////                        Log.d(TAG, "Ad showed fullscreen content.")
//                        // Called when ad is dismissed.
//                    }
//
//                }
//
//            // Set paid event listener
//            mInterstitialAd?.onPaidEventListener = OnPaidEventListener { adValue ->
//                // TODO: Send the impression-level ad revenue information to your preferred
//                // analytics server directly within this callback.
//
//                // Extract the impression-level ad revenue data.
//                val valueMicros = adValue.valueMicros
//                val currencyCode = adValue.currencyCode
//                val precision = adValue.precisionType
//
//                // Get the ad unit ID.
//                val adUnitId = mInterstitialAd?.adUnitId
//
//                trackEvent(mFirebaseAnalytics!!, "$valueMicros $currencyCode $precision $adUnitId")
//
//            }
//
//            mInterstitialAd?.show(this)
//        } else {
//
//            doneSelected()
////            loadInter()
//
//        }
//    }
}