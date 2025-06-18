package com.pianomusicdrumpad.pianokeyboard.callafterscreen.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object MySharedPre {

    private const val MYPREFTABLE = "com.iptv.player.iptvchannel"
    const val CALLENDSCREEN = "callEndScreen"
    const val ISAUTOSTART = "auto_start"
    const val ISONETIME = "is_one_time"
    const val LANGUAGECODE = "code_locale"
    const val CALLENDPREMISSIONONETIME = "show_call_end_permission_one_time"
    const val CALLENDPREMISSIONONECOUNT = "show_call_end_permission_count"
    const val CALLENDPERMISSIONSHOWTIME = "call_end_show_permission_time"
    const val PERMISSION_COUNTVALUE_SCREEN_ONCE_PERDAY = "permission_countvalue_screen_once_per_day"

    const val SHOWCALLSCREENONMISSEDCALL = "show_call_end_screen_after_missed_call"
    const val SHOWCALLSCREENONINCOMINGCALL = "show_call_end_screen_after_completed_call"
    const val SHOWCALLSCREENONOUTGOINGCALL = "show_call_end_screen_after_declined_call"

    const val  CALL_END_ADS_SHOW = "call_end_ads_show"
    const val  CALL_END_BANNER_AD_ID = "call_end_banner_id"
    const val  CALL_END_NATIVE_AD_ID = "call_end_native_id"
    const val  CALL_END_HIDE_BOTTOM_NAVIGATION = "call_end_hide_bottom_navigation"
    const val  CALL_END_SHOW = "call_end_show"
    const val  HIDE_BOTTOM_NAVIGATION_HOME_SCREEN = "hide_bottom_navigation_home_screen"
    const val  CALL_END_PERMISSION_COUNT = "call_end_permission_count"
    const val  CALL_END_PERMISSION_REOPEN = "call_end_permission_reopen"
    const val  PERMISSION_AD_SHOW = "permission_ad_show"
    const val  PERMISSION_HIDE_BOTTOM_NAVIGATION = "permission_hide_bottom_navigation"
    const val  PERMISSION_BANNER_AD_ID = "permission_banner_ad_ic"

    private fun getPrefEdit(context: Context): SharedPreferences {
        return context.getSharedPreferences(MYPREFTABLE, MODE_PRIVATE)
    }

    @JvmStatic
    fun prefPutBooleanCallEnd(context: Context, key: String, value: Boolean) {
        getPrefEdit(context).edit().putBoolean(key, value).apply()
    }

    @JvmStatic
    fun prefGetBooleanCallEnd(context: Context, key: String): Boolean {
        return getPrefEdit(context).getBoolean(key, true)
    }

    @JvmStatic
    fun prefPutBooleanAutoStart(context: Context, key: String, value: Boolean) {
        getPrefEdit(context).edit().putBoolean(key, value).apply()
    }

    @JvmStatic
    fun prefGetBooleanAutoStart(context: Context, key: String): Boolean {
        return getPrefEdit(context).getBoolean(key, false)
    }

    fun prefPutBooleanIsOneTime(context: Context, key: String, value: Boolean) {
        getPrefEdit(context).edit().putBoolean(key, value).apply()
    }

    fun prefGetBooleanIsOneTime(context: Context, key: String): Boolean {
        return getPrefEdit(context).getBoolean(key, false)
    }


    fun prefPutBoolean(context: Context, key: String, value: Boolean) {
        getPrefEdit(context).edit().putBoolean(key, value).apply()
    }

    fun prefGetBoolean(context: Context, key: String): Boolean {
        return getPrefEdit(context).getBoolean(key, false)
    }

    fun prefPutStringLanguage(context: Context, key: String, value: String) {
        getPrefEdit(context).edit().putString(key, value).apply()
    }

    fun prefGetStringLanguage(context: Context, value: String): String? {
        return getPrefEdit(context).getString(value, "")
    }

    fun prefPutBooleanOneTimePermissionScreen(context: Context, key: String, value: Boolean) {
        return getPrefEdit(context).edit().putBoolean(key, value).apply()
    }

    fun prefGetBooleanOneTimePermissionScreen(context: Context, key: String): Boolean {
        return getPrefEdit(context).getBoolean(key, false)
    }

    fun prefPutPermissionCountInt(context: Context, key: String, value: Int) {
        getPrefEdit(context).edit().putInt(key, value).apply()
    }

    fun prefGetPermissionCountInt(context: Context, key: String): Int {
        return getPrefEdit(context).getInt(key, 0) // Default value is 0
    }

    fun prefPutPermissionTimeLong(context: Context, key: String, value: Long){
        getPrefEdit(context).edit().putLong(key, value).apply()
    }

    fun prefGetPermissionTimeLong(context: Context, key: String): Long {
        return getPrefEdit(context).getLong(key, 0L)
    }

    fun prefPutPermissionOncePerDayInt(context: Context, key: String, value: Int) {
        getPrefEdit(context).edit().putInt(key, value).apply()
    }

    fun prefGetPermissionOncePerDayInt(context: Context, key: String): Int {
        return getPrefEdit(context).getInt(key, 1) // Default value is 0
    }


    fun putBoolean(context: Context, key: String, value: Boolean) {
        getPrefEdit(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String): Boolean {
        return getPrefEdit(context).getBoolean(key, false)
    }

    fun putBooleanCALLENDSHOW(context: Context, key: String, value: Boolean) {
        getPrefEdit(context).edit().putBoolean(key, value).apply()
    }

    fun getBooleanCALLENDSHOW(context: Context, key: String): Boolean {
        return getPrefEdit(context).getBoolean(key, true)
    }

    fun putInt(context: Context, key: String, value: Int) {
        getPrefEdit(context).edit().putInt(key, value).apply()
    }

    fun getInt(context: Context, key: String): Int {
        return getPrefEdit(context).getInt(key, 0) // Default value is 0
    }


    fun putString(context: Context, key: String, value: String) {
        getPrefEdit(context).edit().putString(key, value).apply()
    }

    fun getString(context: Context, value: String): String? {
        return getPrefEdit(context).getString(value, "")
    }

}