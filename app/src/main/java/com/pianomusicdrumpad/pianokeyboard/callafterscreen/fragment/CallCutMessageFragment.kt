package com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.Interface.QuickResponseClick
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.activity.CallCutPopupActivity
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.adapter.QuickResponseViewAdapterPopup
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.getSharedPreferencesData
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.getStringList
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.saveStringList
import com.pianomusicdrumpad.pianokeyboard.databinding.CfsFragmentCallCutMessageBinding

class CallCutMessageFragment : Fragment() {
    lateinit var binding: CfsFragmentCallCutMessageBinding
    val arrayList = ArrayList<String>()
    lateinit var sharedPreferences: SharedPreferences
    private var phoneNumber: String? = ""

    var callCutPopupActivity: CallCutPopupActivity? = null

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callCutPopupActivity = activity as CallCutPopupActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CfsFragmentCallCutMessageBinding.inflate(inflater, container, false)

        init()
        initClick()

        return binding.root
    }


    fun init() {
        sharedPreferences = getSharedPreferencesData(callCutPopupActivity!!)

        phoneNumber = CommonUtils.getString(
            sharedPreferences = sharedPreferences,
            key = CommonUtils.sharedPreferencesNumber, defaultValue = ""
        )

        // Save a list of responses
        val responses = listOf("Hello!", "I'm busy, call later.", "On my way!")
        saveStringList(sharedPreferences, "QuickResponsePref", responses)

        val responseList = getStringList(sharedPreferences, "QuickResponsePref")

        if (responseList.isNotEmpty()) {
            arrayList.addAll(responseList)
        }

        val quickResponseViewAdapterPopup = QuickResponseViewAdapterPopup(
            callCutPopupActivity!!,
            arrayList,
            object : QuickResponseClick {
                override fun onMessageClick(pos: Int) {
                    CommonUtils.sendMessageWithText(
                        callCutPopupActivity!!,
                        phoneNumber!!,
                        arrayList[pos]
                    )
                }

            })
        binding.rcvQuickResponse.adapter = quickResponseViewAdapterPopup

    }

    private fun initClick() {
        binding.ivSend.setOnClickListener {
            if (binding.etCustomMessage.text.toString().isNotEmpty()) {
                CommonUtils.sendMessageWithText(
                    callCutPopupActivity!!,
                    phoneNumber!!,
                    binding.etCustomMessage.text.toString()
                )
            } else {
                Toast.makeText(
                    callCutPopupActivity,
                    requireActivity().resources.getString(R.string.please_enter_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}