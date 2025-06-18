package com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderViews

import android.content.Context
import com.pianomusicdrumpad.pianokeyboard.R
import java.util.Calendar

class WPHoursPickerAdapter2 : WheelAdapter() {
    private val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) // Get current hour

    override fun getValue(context: Context, position: Int): String {
        return context.getString(R.string.numberPick, String.format("%02d", position))
    }

    override fun getPosition(value: String): Int {
        return value.toIntOrNull() ?: currentHour // Default to current hour
    }

    override fun getTextWithMaximumLength(): String {
        return "23"
    }
}
