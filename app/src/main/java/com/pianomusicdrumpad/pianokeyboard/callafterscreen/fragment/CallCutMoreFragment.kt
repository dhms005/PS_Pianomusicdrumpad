package com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment

import android.app.SearchManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.activity.CallCutPopupActivity
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.getSharedPreferencesData
import com.pianomusicdrumpad.pianokeyboard.databinding.CfsFragmentCallCutMoreBinding


class CallCutMoreFragment : Fragment() {
    lateinit var binding: CfsFragmentCallCutMoreBinding
    private var phoneNumber: String? = ""
    lateinit var sharedPreferences: SharedPreferences
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
        binding = CfsFragmentCallCutMoreBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    fun init() {

        sharedPreferences = getSharedPreferencesData(callCutPopupActivity!!)

        phoneNumber = CommonUtils.getString(
            sharedPreferences = sharedPreferences,
            key = CommonUtils.sharedPreferencesNumber, defaultValue = ""
        )



        binding.llEditContact.setOnClickListener {
            CommonUtils.openContact(callCutPopupActivity!!)
        }

        binding.llMessage.setOnClickListener {
            CommonUtils.sendMessage(callCutPopupActivity!!, "")
        }

        binding.llWhatsApp.setOnClickListener {
            CommonUtils.openWhatApp(callCutPopupActivity!!)
        }

        binding.llWeb.setOnClickListener {
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, phoneNumber!!)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}