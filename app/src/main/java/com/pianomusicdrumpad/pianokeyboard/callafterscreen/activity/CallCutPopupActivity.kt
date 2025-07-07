package com.pianomusicdrumpad.pianokeyboard.callafterscreen.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.ViewModel.RecentCallViewModel
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.adapter.MyFragmentStatePagerAdapter
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.getCurrentTime
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.getSharedPreferencesData
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.logCallAfterScreenEvent
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.openDialer
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.MenuActivity
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import com.pianomusicdrumpad.pianokeyboard.databinding.ActivityCallCutPopupBinding

class CallCutPopupActivity : AppCompatActivity() {
    lateinit var binding: ActivityCallCutPopupBinding
    private var callNumber: String? = ""

    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModelRecentAllCall: RecentCallViewModel
    private lateinit var adContainerView: FrameLayout
    private var adView: AdView? = null
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics


    companion object {
        @SuppressLint("StaticFieldLeak")
        var callCutPopupActivity: Activity? = null
        var REQUEST_CODE_PERMISSIONS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCallCutPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callCutPopupActivity = this
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        if (intent.extras != null) {
            callNumber = intent.getStringExtra("callNumber")
        }
//        checkPermission()
        afterPermissionUpdate()

        adContainerView = binding.AdmobNativeFrameTwo

//        MainInterfaceV2.loadCacheBanner(this@CallCutPopupActivity)
//        MainInterfaceV2.showCacheBanner(adContainerView)
//        Admob_Full_AD_New.getInstance().loadInterOne(context)
//        adContainerView.removeAllViews()
//        adContainerView.addView(MainInterfaceV2.loadCacheBanner(this@CallCutPopupActivity))


        val callEndAdsShow =
            SharePrefUtils.getString(ConstantAd.CALL_END_ADS_SHOW, "1")

        if (callEndAdsShow == "1") {
            loadBanner()
        }

        viewModelRecentAllCall = ViewModelProvider(this)[RecentCallViewModel::class.java]

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                destroyActivity()
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }

    fun init() {
        sharedPreferences = getSharedPreferencesData(this)

//        mFirebaseAnalytics?.let { logCallAfterScreenEvent(it) }
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        logCallAfterScreenEvent(mFirebaseAnalytics)
//        CommonUtils.putString(
//            sharedPreferences = sharedPreferences,
//            key = CommonUtils.sharedPreferencesNumber,
//            value = callNumber!!
//        )


        if (SharePrefUtils.getBoolean(ConstantAd.FIRST_AFTER_CALL, true)) {
            CommonUtils.trackEvent(mFirebaseAnalytics, "first_aftercall")
            SharePrefUtils.putBoolean(ConstantAd.FIRST_AFTER_CALL, false)
        }
        CommonUtils.trackEvent(mFirebaseAnalytics, "aftercall_created")

        callNumber = CommonUtils.getString(
            sharedPreferences = sharedPreferences,
            key = CommonUtils.sharedPreferencesNumber, defaultValue = ""
        )


        binding.tvstatus.isSelected = true

//        binding.tvDuration.text = formatDuration(recentCalls.dur)
        binding.tvDuration.text = getCurrentTime()

//        binding.ivProfile.visibility = View.GONE
        binding.ivDefaultProfile.visibility = View.VISIBLE


        val adapter = MyFragmentStatePagerAdapter(this@CallCutPopupActivity)
        binding.viewPager.adapter = adapter
//
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()
        binding.tabLayout.getTabAt(0)!!.setIcon(R.drawable.cfs_ic_tab_image)
//        binding.tabLayout.getTabAt(1)!!.setIcon(R.drawable.cfs_ic_tab_4)
        binding.tabLayout.getTabAt(1)!!.setIcon(R.drawable.cfs_ic_tab_sms)
        binding.tabLayout.getTabAt(2)!!.setIcon(R.drawable.cfs_reminder_callend_image)
        binding.tabLayout.getTabAt(3)!!.setIcon(R.drawable.cfs_ic_tab_3)

    }

    private fun afterPermissionUpdate() {

        init()
        initClick()
    }

    private fun initClick() {
        binding.ivCall.setOnClickListener {
            openDialer(this)
        }
        binding.ivLogo.setOnClickListener {
            openApp()
        }
    }

    private fun openApp() {

        val i = Intent(this@CallCutPopupActivity, MenuActivity::class.java)
        startActivity(i)
    }

    private fun destroyActivity() {
        finish()
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
        val window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        // Check if the app was reopened from recent apps
        val intent = intent
        if (intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY != 0) {
            navigateToHome()
        }
    }

    override fun onPause() {
        super.onPause()
        adView?.pause()
    }

    override fun onDestroy() {
        ConstantAd.SPLASH_OPEN = true
        adView?.destroy()
        super.onDestroy()
    }

    private fun navigateToHome() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(homeIntent)
        finish()
    }

    private fun loadBanner() {
        // [START create_ad_view]
        // Create a new ad view.

        CommonUtils.trackEvent(mFirebaseAnalytics, "ac_ad_shown")

        adContainerView = binding.AdmobNativeFrameTwo

        val adViewShimmer = layoutInflater.inflate(
            R.layout.ad_native_rectangle_adaptive_banner_simmer, adContainerView, false
        ) as ShimmerFrameLayout
        adContainerView.removeAllViews()
        adContainerView.addView(adViewShimmer)
        adViewShimmer.startShimmer()
        adView = callCutPopupActivity?.let { AdView(it) }
        adView?.adUnitId = ConstantAd.CALL_END_SCREEN_BANNER

        val displayMetrics = resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels
        val screenWidthDp = (screenWidthPx / (displayMetrics.densityDpi / 160f)).toInt()
        val adSize = AdSize.getLandscapeInlineAdaptiveBannerAdSize(this, screenWidthDp)

//        adView.setAdSize(adSize)

        adView?.setAdSize(adSize)

//        adContainerView.addView(adView)

        // Start loading the ad in the background.
        val adRequest = AdRequest.Builder().build()
//        adView?.loadAd(adRequest)


        // [START load_ad]
        // Start loading the ad in the background.
//        AdRequest adRequest = new AdRequest.Builder().build();
        adView?.loadAd(adRequest)
        // [END load_ad]
        adView?.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                CommonUtils.trackEvent(mFirebaseAnalytics, "ad_clicked")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                adContainerView.removeAllViews()
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
//                adContainerView.removeAllViews()
                //                adContainerView.setVisibility(View.GONE);
                CommonUtils.trackEvent(mFirebaseAnalytics, "av_ad_impression")
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                // Replace ad container with new ad view.
                adContainerView.removeAllViews()
                adContainerView.addView(adView)
                // [END create_ad_view]
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }
    }

}
