package com.pianomusicdrumpad.pianokeyboard.Utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by India on 6/22/2016.
 */
class SharedPrefrencesApp(private val mContext: Context) {
    private val settings: SharedPreferences
    private val editor: SharedPreferences.Editor
    var url: String = "url"
    var title: String = "title"
    var LANGUAGE: String = "english"
    var Activity: String = "activity"
    var CHECK_FIRST_TIME_LAN: String = "check_first_time_lan"


    var Check_ID: String = "check_ad"

    init {
        settings = mContext.getSharedPreferences(MYPREFS, 0)
        editor = settings.edit()
    }

    // Set the Access Token and Other Important Keys in SharedPrefernces
    fun setPreferences(Key: String?, Value: String?) {
        editor.putString(Key, Value)
        editor.commit()
    }

    // get the Access Token and Other Important Keys in SharedPrefernces
    fun getPreferences(Key: String?, Value: String?): String? {
        return settings.getString(Key, Value)
    }

    // Set the Access Token and Other Important Keys in SharedPrefernces
    fun setBooleanPreferences(Key: String?, Status: Boolean) {
        editor.putBoolean(Key, Status)
        editor.commit()
    }

    // get the Access Token and Other Important Keys in SharedPrefernces
    fun getBooleanPreferences(Key: String?, Status: Boolean): Boolean {
        return settings.getBoolean(Key, Status)
    }

    companion object {
        var Privacy: String = "ABC"
        private const val MYPREFS = "Piano Music Drum"
    }
}
