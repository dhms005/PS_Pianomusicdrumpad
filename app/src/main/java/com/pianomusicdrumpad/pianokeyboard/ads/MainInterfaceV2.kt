package com.pianomusicdrumpad.pianokeyboard.ads

import android.app.Activity
import android.content.Context
import android.widget.FrameLayout
import android.widget.Toast
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import org.json.JSONObject

object MainInterfaceV2 {
    fun initMain(activity: Activity, loadData: LoadData) {
        if (GetDataApi.isNetworkAvailable(activity)) {
//            GetDataApi.loadDataTxt(activity, false, loadData);
            GetDataApi.getRemoteConfig(activity, loadData)
        } else {
//            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show()
        }
        //        loadData.onSuccess();
    }

    fun showInter(activity: Activity, interAdListener: InterAdListener) {
        if (!SharePrefUtils.getBoolean(
                ConstantAd.IS_PURCHASE, false
            )
        ) {
            Admob_Full_AD_New.getInstance().showInter(activity, interAdListener)
        } else {
            interAdListener.onAdClosed()
        }
    }

    fun ShowBackInter(activity: Activity, backInterAdListener: BackInterAdListener) {
        if (!SharePrefUtils.getBoolean(
                ConstantAd.IS_PURCHASE, false
            )
        ) {
            Admob_Full_AD_New.getInstance().showInterBack(activity, backInterAdListener)
        } else {
            backInterAdListener.onAdClosed()
        }
    }

    fun loadCacheBanner(context: Context) {
        if (!SharePrefUtils.getBoolean(
                ConstantAd.IS_PURCHASE, false
            )
        ) {
            Admob_Banner_Ad.getInstance().loadBanner(context)
        } else {
        }
    }

    fun showCacheBanner(adContainer: FrameLayout) {
        if (!SharePrefUtils.getBoolean(
                ConstantAd.IS_PURCHASE, false
            )
        ) {
            Admob_Banner_Ad.getInstance().showBanner(adContainer)
        } else {
        }
    }

    interface LoadData {
        fun reLoad(reloadTxt: String?)

        fun onSuccess()

        fun onExtraData(extData: JSONObject?)
    }

    interface OpenAdListener {
        fun onAdClosed()
    }

    interface InterAdListener {
        fun onAdClosed()
    }

    interface BackInterAdListener {
        fun onAdClosed()
    }
}