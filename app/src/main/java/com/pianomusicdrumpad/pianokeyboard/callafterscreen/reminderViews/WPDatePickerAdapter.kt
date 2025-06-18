package com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderViews


import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WPDatePickerAdapter : WheelAdapter() {

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    override fun getValue(context: Context, position: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, position) // Increment days based on position
        return dateFormat.format(calendar.time)
    }

    override fun getPosition(value: String): Int {
        return try {
            val inputDate = dateFormat.parse(value) ?: return 0
            val today = Calendar.getInstance()
            val target = Calendar.getInstance().apply { time = inputDate }
            ((target.timeInMillis - today.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            0
        }
    }

    override fun getTextWithMaximumLength(): String {
        return "00-00-0000"
    }
}