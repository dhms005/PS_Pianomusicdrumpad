package com.pianomusicdrumpad.pianokeyboard.Utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SharePrefUtils {
    private var mSharePref: SharedPreferences? = null
    @JvmStatic
    fun init(context: Context?) {
        mSharePref = PreferenceManager.getDefaultSharedPreferences(context)
    }
    @JvmStatic
    fun clearAll() {
        mSharePref!!.edit().clear().apply()
        mSharePref!!.edit().commit()
    }
    @JvmStatic
    fun putString(key: String?, value: String?) {
        val editor = mSharePref!!.edit()
        editor.putString(key, value)
        editor.commit()
    }
    @JvmStatic
    fun getString(key: String?, defaultValues: String?): String {
        return mSharePref?.getString(key, defaultValues)!!
    }
    @JvmStatic
    fun putInt(key: String?, value: Int) {
        val editor = mSharePref!!.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun getInt(key: String?, defaultValues: Int): Int {
        return mSharePref?.getInt(key, defaultValues)!!
    }

    fun putLong(key: String?, value: Long) {
        val editor = mSharePref!!.edit()
        editor.putLong(key, value)
        editor.commit()
    }
    @JvmStatic
    fun getLong(key: String?, defaultValues: Long): Long {
        return mSharePref!!.getLong(key, defaultValues)
    }
    @JvmStatic
    fun putBoolean(key: String?, value: Boolean) {
        val editor = mSharePref!!.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }
    @JvmStatic
    fun getBoolean(key: String?, defaultValues: Boolean): Boolean {
        return mSharePref!!.getBoolean(key, defaultValues)
    }
}
