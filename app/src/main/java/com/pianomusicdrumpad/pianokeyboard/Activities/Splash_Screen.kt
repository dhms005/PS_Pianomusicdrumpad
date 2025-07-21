package com.pianomusicdrumpad.pianokeyboard.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pianomusicdrumpad.pianokeyboard.language.LanguageActivitySplash
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.MenuActivity
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility
import com.pianomusicdrumpad.pianokeyboard.ads.AppOpenAdManager
import com.pianomusicdrumpad.pianokeyboard.ads.MainInterfaceV2
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils
import org.json.JSONObject
import java.util.Arrays
import java.util.Random


/**
 * Splash Activity that inflates splash activity xml.
 */
class Splash_Screen : AppCompatActivity() {
    var Start_screen_repeat: Int = 0
    private val vpn_countrycode_list = ArrayList<String>()
    private val secondsRemaining: Long = 0

    // 6-second timeout for ad loading
    private val AD_TIMEOUT_MS: Long = 6000
    private lateinit var appOpenAdManager: AppOpenAdManager

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.POST_NOTIFICATIONS,
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
        )
    } else {
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash__screen)

//        createTimer()
        ConstantAd.SPLASH_OPEN = false
        MainInterfaceV2.initMain(this@Splash_Screen, object : MainInterfaceV2.LoadData {
            override fun reLoad(reloadTxt: String?) {}
            override fun onSuccess() {


                val splashOpenAdsShow =
                    SharePrefUtils.getString(ConstantAd.SPLASH_OPEN_ADS_SHOW, "1")

                if (splashOpenAdsShow == "1") {
                    if (!SharePrefUtils.getBoolean(ConstantAd.IS_PURCHASE, false)) {
                        appOpenAdManager = AppOpenAdManager(this@Splash_Screen)
                        appOpenAdManager.loadAndShowAd(this@Splash_Screen) {
                            nextcall()
                        }
                    } else {
                        nextcall()
                    }
                } else {
                    createTimer()
                }


            }

            override fun onExtraData(extData: JSONObject?) {}
        })


        // Create a timer so the SplashActivity will be displayed for a fixed amount of time.
    }

    fun Internet_dialog() {
        val dialog = Dialog(this@Splash_Screen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.internet)
        val window = dialog.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //        ImageView txtCancel = (ImageView) dialog.findViewById(R.id.no);
        val txtOk = dialog.findViewById<View>(R.id.yes) as TextView
        val action_settings = dialog.findViewById<View>(R.id.action_settings) as TextView
        action_settings.setOnClickListener {
            startActivity(
                Intent(
                    Settings.ACTION_WIRELESS_SETTINGS
                )
            )
        }
        txtOk.setOnClickListener {
            dialog.dismiss()
            check_internet()
        }

        dialog.show()
    }

    private fun nextcall() {
        Utility.temPValue = 0

        //      startService(new Intent(getBaseContext(), ClearService.class));
//
//            Log.e("value", SharePrefUtils.getString(Constant_ad.VPN_BUTTON,"0"));
//            if (SharePrefUtils.getString(Constant_ad.VPN_BUTTON, "0").equals("0")) {
//
//                vpn = false;
//            } else {
//                vpn = true;
//            }
//
//            vpn_cancel_count = 10;
//            url = "https://awebhtpo3u8g5t.ecoweb-network.com";
//            id = "touchvpn";
        if (SharePrefUtils.getString(ConstantAd.AD_MULTIPLE_START, "0") == "0") {
            Start_screen_repeat = 0
            SharePrefUtils.putString(ConstantAd.START_SCREEN_COUNT, "" + Start_screen_repeat)
        } else {
            Start_screen_repeat =
                SharePrefUtils.getString(ConstantAd.START_SCREEN_COUNT, "0").toInt()
        }

        val randomGenerator = Random()
        val vpn_countrycode = SharePrefUtils.getString(ConstantAd.FACEBOOK_DIALOG, "")
        val vpn_countrycode1 =
            vpn_countrycode.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        vpn_countrycode_list.addAll(Arrays.asList(*vpn_countrycode1))


        //            if (vpn_countrycode1.length > 0) {
//                int index = randomGenerator.nextInt(vpn_countrycode_list.size());
//                selectedCountry = vpn_countrycode_list.get(index).toString();
//            } else {
//                selectedCountry = "";
//            }
        /**/
        val AppType = SharePrefUtils.getString(ConstantAd.IN_HOUSE, "0")


        Log.e("@@@", "" + AppType)

        if (AppType == "0") {
            // off
            SharePrefUtils.putString(ConstantAd.IN_HOUSE, "0")
            SharePrefUtils.putString(ConstantAd.QUREKA_BTN, "0")
        } else if (AppType == "1") {
            //on
            SharePrefUtils.putString(ConstantAd.IN_HOUSE, "1")
            SharePrefUtils.putString(ConstantAd.QUREKA_BTN, "0")

            //                    if (qurekaURL.equals("")) {
//                        SharePrefUtils.putString(Constant_ad.QUREKA_BTN, "0");
//                    } else {
//                        SharePrefUtils.putString(Constant_ad.QUREKA_BTN, "0");
//                    }
        } else {
            SharePrefUtils.putString(ConstantAd.IN_HOUSE, "0")
            SharePrefUtils.putString(ConstantAd.QUREKA_BTN, "0")
        }

        SharePrefUtils.putString(ConstantAd.ACCOUNT, "Shine Zone")
        SharePrefUtils.putString(ConstantAd.EMAIL, "  ")

//        startActivity(Intent(this@Splash_Screen, MenuActivity::class.java))


        if (CommonUtils.hasPermissions(permissions, this)) {
            val i = Intent(this@Splash_Screen, MenuActivity::class.java)
            startActivity(i)
            finish()

        } else if (SharePrefUtils.getString(ConstantAd.OPEN_APP_FIRST_TIME, "0") == "0") {
            startActivity(Intent(this@Splash_Screen, LanguageActivitySplash::class.java))
        } else if (!SharePrefUtils.getBoolean(ConstantAd.AGREE_SCREEN, false)) {
            startActivity(Intent(this@Splash_Screen, AgreeScreenActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@Splash_Screen, AgreeScreenActivity::class.java))
            finish()
        }


    }

    override fun onStart() {
        super.onStart()
//        if (Utility.isNetworkAvailable(this@Splash_Screen)) {
//            nextcall()
//        } else {
//            Internet_dialog()
//        }
    }

    private fun check_internet() {
        if (Utility.isNetworkAvailable(this@Splash_Screen)) {
            nextcall()
        } else {
            Internet_dialog()
        }
    }

    private fun createTimer() {


        val countDownTimer: CountDownTimer =
            object : CountDownTimer(COUNTER_TIME_MILLISECONDS, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {

                    //                        counterTextView.setText("App is done loading in: " + secondsRemaining);
                }

                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    nextcall()

                }
            }
        countDownTimer.start()
    }

    companion object {
        private const val LOG_TAG = "SplashActivity"
        var isSplashLoading: Boolean = false

        /**
         * Number of seconds to count down before showing the app open ad. This simulates the time needed
         * to load the app.
         */
        private const val COUNTER_TIME: Long = 5
        private const val COUNTER_TIME_MILLISECONDS: Long = 3000
    }
}