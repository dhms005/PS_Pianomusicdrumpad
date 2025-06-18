package com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderViews

import android.content.Context
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderViews.WheelPicker

abstract class WheelAdapter {

    abstract fun getValue(context: Context, position: Int): String

    abstract fun getPosition(vale: String): Int

    abstract fun getTextWithMaximumLength(): String

    open fun getSize(): Int = -1

    open fun getMinValidIndex() : Int? {
        return null
    }

    open fun getMaxValidIndex() : Int? {
        return null
    }

    var picker: WheelPicker? = null

}