package com.pianomusicdrumpad.pianokeyboard.ads

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils

class Admob_Full_AD_New {
    var interstitialOne: InterstitialAd? = null
    var interAdListener: MainInterfaceV2.InterAdListener? = null
    var backInterAdListener: MainInterfaceV2.BackInterAdListener? = null
    fun loadInterOne(context: Activity) {
//        String admobinter = SharePrefUtils.getString(ConstantAd.GOOGLE_FULL_AD, Custom_Ad_Key.KEY_ADMOB_FULL);
        val admobinter: String = ConstantAd.AD_INTER

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            admobinter,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    interstitialOne = interstitialAd
                    Log.e("@TESTING", "onAdLoaded 1")
                    interstitialOne!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                // Called when fullscreen content is dismissed.
                                interstitialOne = null


                                Log.e("@TESTING", "onAdDismissedFullScreenContent 1")
                                loadInterOne(context)

                                if (interAdListener != null) {
                                    interAdListener!!.onAdClosed()
                                    interAdListener = null
                                }

                                if (backInterAdListener != null) {
                                    backInterAdListener!!.onAdClosed()
                                    backInterAdListener = null
                                }
                                //                                        interAdListener.onAdClosed();
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)

                                interstitialOne = null
                                Log.e("@TESTING", "onAdFailedToShowFullScreenContent 1")
                                loadInterOne(context)
                            }

                            override fun onAdClicked() {
                                super.onAdClicked()
                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    interstitialOne = null
                }
            })
    }

    fun showInter(context: Activity, interAdListener: MainInterfaceV2.InterAdListener?) {
        this.interAdListener = interAdListener

        //        loadingAdDialog(context);
//        Log.e("count","call");
        var click = SharePrefUtils.getInt(ConstantAd.COUNT, 1)
        val totalCount = SharePrefUtils.getInt(ConstantAd.AD_COUNT, 2)

        //        Log.e("count","call " +ConstantAd.IsAdShowing);
//        Log.e("count","call " +ConstantAd.IsAdTimerFinished);
        if (totalCount <= click) {
            //            Log.e("count","call match");

            click = 1
            SharePrefUtils.putInt(ConstantAd.COUNT, click)

            showInterAdDialog(context, interAdListener)
        } else {
            click += 1
            SharePrefUtils.putInt(ConstantAd.COUNT, click)
            if (this.interAdListener != null) {
                this.interAdListener!!.onAdClosed()
                this.interAdListener = null
            }
        }
    }

    fun showInterBack(context: Activity, backInterAdListener: MainInterfaceV2.BackInterAdListener?) {
        this.backInterAdListener = backInterAdListener


        val isBack = SharePrefUtils.getInt(ConstantAd.AD_BACK_TOTAL_COUNT, 1)
        var click = SharePrefUtils.getInt(ConstantAd.AD_BACK_COUNT, 1)

        if (isBack != 0) {
            if (isBack <= click) {
                click = 1
                SharePrefUtils.putInt(ConstantAd.AD_BACK_COUNT, click)
                showBackInterAdDialog(context, backInterAdListener)
            } else {
                click += 1

                SharePrefUtils. putInt(ConstantAd.AD_BACK_COUNT, click)
                if (this.backInterAdListener != null) {
                    this.backInterAdListener!!.onAdClosed()
                    this.backInterAdListener = null
                }
            }
        } else {
            if (this.backInterAdListener != null) {
                this.backInterAdListener!!.onAdClosed()
                this.backInterAdListener = null
            }
        }
    }

    private fun showInterAdDialog(context: Activity, interAdListener: MainInterfaceV2.InterAdListener?) {
        if (interstitialOne != null) {
            interstitialOne!!.show(context)
        }
    }

    private fun showBackInterAdDialog(context: Activity, backInterAdListener: MainInterfaceV2.BackInterAdListener?) {
        if (interstitialOne != null) {
            interstitialOne!!.show(context)
        }
    }

    fun interstitialCallBack() {
        if (interAdListener != null) {
            interAdListener!!.onAdClosed()
            interAdListener = null
        }

        if (backInterAdListener != null) {
            backInterAdListener!!.onAdClosed()
            backInterAdListener = null
        }
    }


    companion object {

        private var mInstance: Admob_Full_AD_New? = null

        fun getInstance(): Admob_Full_AD_New {
            if (mInstance == null) {
                mInstance = Admob_Full_AD_New()
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
