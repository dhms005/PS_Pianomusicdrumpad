package com.pianomusicdrumpad.pianokeyboard.callafterscreen.model

import android.telecom.PhoneAccountHandle

data class SimModel(
    val simId: Int,
    val phoneAccountHandle: PhoneAccountHandle,
    val simOperatorName: String
)

data class SIMAccount(
    val id: Int,
    val handle: PhoneAccountHandle,
    val label: String,
    val phoneNumber: String
)