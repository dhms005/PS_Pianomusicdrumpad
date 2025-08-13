package com.pianomusicdrumpad.pianokeyboard.callafterscreen.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.google.android.gms.ads.AdSize
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.ads.AdManager
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.activity.CallCutPopupActivity
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.getSharedPreferencesData
import java.util.logging.Handler

class CallReceiver : BroadcastReceiver() {

    companion object {
        private var wasCallActive = false // Track if the call was active
        private var incomingNumber: String? = "" // Track if the call was active
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                    Log.d("CallReceiver", "Incoming Call: $incomingNumber")
                    wasCallActive = true
//                    MainInterfaceV2.loadCacheBanner(context)
                }

                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
//                    Log.d("CallReceiver", "Call Answered")
                    incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                    Log.d("CallReceiver", "Call Answered: $incomingNumber")
//                    MainInterfaceV2.loadCacheBanner(context)
                    wasCallActive = true // Call is active now
                }

                TelephonyManager.EXTRA_STATE_IDLE -> {

                    Log.d("CallReceiver", "Call Disconnected")

                    // Open activity ONLY if a call was active before disconnecting
                    if (wasCallActive) {
                        wasCallActive = false // Reset state


                        var sharedPreferences = getSharedPreferencesData(context)
                        if (incomingNumber != null) {
                            CommonUtils.putString(
                                sharedPreferences = sharedPreferences,
                                key = CommonUtils.sharedPreferencesNumber,
                                value = incomingNumber!!
                            )
                        }

                        if (Settings.canDrawOverlays(context)) {


                            val defaultSharedPreferences =
                                PreferenceManager.getDefaultSharedPreferences(
                                    context
                                )
                            if (defaultSharedPreferences.getBoolean(
                                    "prefAfterCallScreen", true
                                )
                            ) {

                                val displayMetrics = context.resources.displayMetrics
                                val screenWidthDp =
                                    (displayMetrics.widthPixels / displayMetrics.density).toInt()
                                val adSize = AdSize.getLandscapeInlineAdaptiveBannerAdSize(
                                    context,
                                    screenWidthDp
                                )

                                // Preload ad
                                AdManager.preloadBannerAd(
                                    context,
                                    ConstantAd.CALL_END_SCREEN_BANNER,
                                    adSize
                                )

                                android.os.Handler(Looper.getMainLooper()).postDelayed({
                                    // Code to run after 3 seconds
                                    Log.d("Timer", "3 seconds passed!")

                                    ConstantAd.SPLASH_OPEN = false
                                    val newIntent =
                                        Intent(context, CallCutPopupActivity::class.java).apply {
//                                            flags =
//                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
////                                    putExtra("callNumber", incomingNumber)

                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                                            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
//                                            Log.e("@@@", "" + incomingNumber)
//                                    putExtra("callNumber", "123")
                                        }
                                    context.startActivity(newIntent)

                                }, 100) // 3000 milliseconds = 3 seconds


                            }

                        }

                    }
                }
            }
        }
    }
}
