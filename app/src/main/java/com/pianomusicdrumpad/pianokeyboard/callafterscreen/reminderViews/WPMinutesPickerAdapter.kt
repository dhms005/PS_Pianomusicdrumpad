package com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderViews

import android.content.Context
import com.pianomusicdrumpad.pianokeyboard.R
import java.util.Calendar

class WPMinutesPickerAdapter : WheelAdapter() {

    private val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)
    private val defaultMinute = (currentMinute + 10) % 60 // Ensure it stays within 0-59

    override fun getValue(context: Context, position: Int): String {
        return context.getString(R.string.numberPick, String.format("%02d", position))
    }

    override fun getPosition(value: String): Int {
        return value.toIntOrNull()?.coerceIn(0, 59) ?: Calendar.getInstance().get(Calendar.MINUTE) // Restrict to 0-59
    }

    override fun getTextWithMaximumLength(): String {
        return "59"
    }
}