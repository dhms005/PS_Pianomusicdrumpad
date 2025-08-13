package com.pianomusicdrumpad.pianokeyboard.ads

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils

object GetDataApi {
    var publicIP: String = ""
    var regionName: String = ""
    var city: String = ""
    var vpnInUse: Boolean = false

    fun getRemoteConfig(context: Activity, loadData: MainInterfaceV2.LoadData) {
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)


        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(
                context
            ) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.e("TAG", "Config params updated: $updated")
                    //                            Toast.makeText(context, "Fetch and activate succeeded",
                    //                                    Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                        context, "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                setData(context, loadData)
            }
    }

    private fun setData(context: Activity, loadData: MainInterfaceV2.LoadData) {
        //        Log.e("chekLogData", loadData.toString() + "");

//        String mediation = FirebaseRemoteConfig.getInstance().getString("mediation");
//        String google_fullad = FirebaseRemoteConfig.getInstance().getString("google_fullad");

//        String google_fullad_2 = FirebaseRemoteConfig.getInstance().getString("google_fullad_2");
//        String google_banner = FirebaseRemoteConfig.getInstance().getString("google_banner");
//        String google_native = FirebaseRemoteConfig.getInstance().getString("google_native");
//        String google_native_2 = FirebaseRemoteConfig.getInstance().getString("google_native_2");
//        String google_native_banner = FirebaseRemoteConfig.getInstance().getString("google_native_banner");
//        String rewared_ad = FirebaseRemoteConfig.getInstance().getString("google_reward_ad");
//        String fb_dialog = FirebaseRemoteConfig.getInstance().getString("fb_dialog");
//        String fb_full_ad = FirebaseRemoteConfig.getInstance().getString("fb_full_ad");
//        String fb_banner = FirebaseRemoteConfig.getInstance().getString("fb_banner");
//        String fb_full_native = FirebaseRemoteConfig.getInstance().getString("fb_full_native");
//        String open_ad_id = FirebaseRemoteConfig.getInstance().getString("google_appopen");
//        String google_appopen_2 = FirebaseRemoteConfig.getInstance().getString("google_appopen_2");
//        String google_appopen_3 = FirebaseRemoteConfig.getInstance().getString("google_appopen_3");
//        String google_appOpen_splash = FirebaseRemoteConfig.getInstance().getString("google_appOpen_splash");
//        String in_house = FirebaseRemoteConfig.getInstance().getString("in_house");

//        SharePrefUtils.putString(ConstantAd.MEDIATION, mediation);
//        SharePrefUtils.putString(ConstantAd.OPEN_AD_OPEN_SPLASH, google_appOpen_splash);
//        SharePrefUtils.putString(ConstantAd.GOOGLE_FULL_AD, google_fullad);
//        SharePrefUtils.putString(ConstantAd.GOOGLE_FULL_AD2, google_fullad_2);
//        SharePrefUtils.putString(ConstantAd.GOOGLE_BANNER, google_banner);
//        SharePrefUtils.putString(ConstantAd.GOOGLE_NATIVE, google_native);
//        SharePrefUtils.putString(ConstantAd.GOOGLE_NATIVE2, google_native_2);
//        SharePrefUtils.putString(ConstantAd.GOOGLE_NATIVE_BANNER, google_native_banner);
//        SharePrefUtils.putString(ConstantAd.GOOGLE_REWAREDED, rewared_ad);

//        Log.e("onAdFailedToLoad", "GOOGLE_BANNER " + FirebaseRemoteConfig.getInstance().getString("google_banner"));


//        SharePrefUtils.putString(ConstantAd.AD_DIALOGUE, FirebaseRemoteConfig.getInstance().getString("ad_dialogue"));


        SharePrefUtils.putString(
            ConstantAd.ACCOUNT,
            FirebaseRemoteConfig.getInstance().getString("ac_name")
        )

        //        SharePrefUtils.putString(ConstantAd.AD_EXIT_NATIVE, FirebaseRemoteConfig.getInstance().getString("exit_native_ad"));

//        SharePrefUtils.putString(ConstantAd.AD_NAV_BAR, FirebaseRemoteConfig.getInstance().getString("nav_bar"));
//        SharePrefUtils.putString(ConstantAd.VPN_DETECT, FirebaseRemoteConfig.getInstance().getString("vpn_detect"));
        SharePrefUtils.putInt(
            ConstantAd.AD_BACK_TOTAL_COUNT,
            FirebaseRemoteConfig.getInstance().getString("back_ad_count").toInt()
        )
        SharePrefUtils.putInt(
            ConstantAd.AD_COUNT,
            FirebaseRemoteConfig.getInstance().getString("forward_ad_count").toInt()
        )

        SharePrefUtils.putString(
            ConstantAd.AUTO_NOTI_ENABLE,
            FirebaseRemoteConfig.getInstance().getString("auto_notification_enable")
        )

        SharePrefUtils.putString(
            ConstantAd.EMAIL,
            FirebaseRemoteConfig.getInstance().getString("email")
        )

        SharePrefUtils.putString(
            ConstantAd.QUREKA_URL,
            FirebaseRemoteConfig.getInstance().getString("qureka_url")
        )

        SharePrefUtils.putString(
            ConstantAd.CALL_END_ADS_SHOW,
            FirebaseRemoteConfig.getInstance().getString("call_end_ads_show")
        )

        SharePrefUtils.putString(
            ConstantAd.HOME_AD_SHOW,
            FirebaseRemoteConfig.getInstance().getString("home_ad_show")
        )

        SharePrefUtils.putString(
            ConstantAd.PERMISSION_SCREEN_ADS_SHOW,
            FirebaseRemoteConfig.getInstance().getString("permission_screen_ads_show")
        )

        SharePrefUtils.putString(
            ConstantAd.SPLASH_OPEN_ADS_SHOW,
            FirebaseRemoteConfig.getInstance().getString("splash_open_ad_show")
        )

        SharePrefUtils.putString(
            ConstantAd.APP_OPEN_RECENT_ADS_SHOW,
            FirebaseRemoteConfig.getInstance().getString("app_open_recent_ad_show")
        )

        SharePrefUtils.putString(
            ConstantAd.navBarEnableAfterCallScreen,
            FirebaseRemoteConfig.getInstance().getString("navBar_enable_afterCall_screen")
        )
        SharePrefUtils.putString(
            ConstantAd.navBarEnablePermissionScreen,
            FirebaseRemoteConfig.getInstance().getString("navBar_enable_permission_screen")
        )

        SharePrefUtils.putString(
            ConstantAd.navBarEnableLanguageScreen,
            FirebaseRemoteConfig.getInstance().getString("navBar_enable_language_screen")
        )


        //       SharePrefUtils.putString(ConstantAd.START_SCREEN_COUNT, FirebaseRemoteConfig.getInstance().getString("start_screen"));
//         SharePrefUtils.putString(ConstantAd.QUREKA_BTN, FirebaseRemoteConfig.getInstance().getString("qureka_btn"));
        Admob_Full_AD_New.getInstance().loadInterOne(context)


        loadData.onSuccess()
    }

    fun isNetworkAvailable(c: Context): Boolean {
        val connectivityManager = c
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
