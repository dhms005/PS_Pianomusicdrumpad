package com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class OverlayPermissionResult(
    val requestCode: Int,
    val resultCode: Int,
    val data: Intent?
)

class SharedViewModel : ViewModel() {
    val overlayPermissionResult = MutableLiveData<OverlayPermissionResult>()

    fun setResult(requestCode: Int, resultCode: Int, data: Intent?) {
        overlayPermissionResult.value = OverlayPermissionResult(requestCode, resultCode, data)
    }
}
