package com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.Interface.OnSimSelection
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.Interface.RecentCallClick
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.ViewModel.RecentCallViewModel
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.activity.CallCutPopupActivity
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.adapter.RecentAllCallAdapterPopup
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.getAvailablesimList
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.getSharedPreferencesData
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.placeCall
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.dialog.SimSelectionDialog
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.model.SimModel
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.databinding.CfsFragmentCallCutRecentBinding

class CallCutRecentFragment : Fragment(), OnSimSelection {

    lateinit var binding: CfsFragmentCallCutRecentBinding
    lateinit var viewModelRecentAllCall: RecentCallViewModel

    var availablesimList: ArrayList<SimModel> = ArrayList()
    var callCutPopupActivity: CallCutPopupActivity? = null
    var simDialog: SimSelectionDialog? = null
    private var callNumber: String? = ""
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callCutPopupActivity = activity as CallCutPopupActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = CfsFragmentCallCutRecentBinding.inflate(inflater, container, false)
        availablesimList = getAvailablesimList(callCutPopupActivity!!)
        simDialog = SimSelectionDialog(callCutPopupActivity!!, availablesimList, this)
        sharedPreferences = getSharedPreferencesData(requireContext())
        return binding.root
    }

    fun init() {
        loadRecentCallsUsingViewModel()
    }

    private fun loadRecentCallsUsingViewModel() {
        if (isAdded) {
            viewModelRecentAllCall = ViewModelProvider(this)[RecentCallViewModel::class.java]

            if (::viewModelRecentAllCall.isInitialized) {
                viewModelRecentAllCall.getRecentCallsLast3Days(requireContext())
                viewModelRecentAllCall.recentContactGroups.observe(viewLifecycleOwner) { recentCalls ->
                    recentCalls.let {
                        if (!recentCalls.isNullOrEmpty()) {

                            Log.e("Recent Call", "" + recentCalls.get(0).type)

                            val recentCallAdapter =
                                RecentAllCallAdapterPopup(callCutPopupActivity!!,
                                    recentCalls,
                                    object : RecentCallClick {

                                        override fun onRecentCallClick(pos: Int) {

                                            callNumber = recentCalls[pos].number
                                            initClick()
//                                        startActivity(
//                                            Intent(
//                                                callCutPopupActivity,
//                                                MainActivity::class.java
//                                            )
//                                        )
                                        }
                                    })

                            binding.rcvAllRecentCall.adapter = recentCallAdapter
                            binding.pbProgress.visibility = View.GONE
                        } else {
                            binding.pbProgress.visibility = View.GONE
                            binding.rcvAllRecentCall.visibility = View.GONE
                            binding.llNoData.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            init()
        }, 500)
    }

    override fun onSimSelected(simId: Int) {
        if (simId == 0) {
            simDialog!!.dismiss()
            placeCall(
                requireContext(), callNumber, 0
            )
        } else if (simId == 1) {
            simDialog!!.dismiss()
            placeCall(
                requireContext(), callNumber, 1
            )
        }
    }

    private fun initClick() {

        if (callNumber!!.isNotEmpty()) {

            if (availablesimList.isNotEmpty()) {
                when (availablesimList.size) {
                    2 -> {
                        val simPref =
                            CommonUtils.getString(
                                sharedPreferences,
                                "SimPrefer",
                                "ask"
                            )

                        when (simPref) {
                            "sim1" -> {
                                placeCall(
                                    requireContext(), callNumber, 0
                                )
                            }

                            "sim2" -> {
                                placeCall(
                                    requireContext(), callNumber, 1
                                )
                            }

                            else -> {
                                simDialog!!.show()
                            }
                        }

                    }

                    1 -> {
                        placeCall(
                            requireContext(), callNumber, 0
                        )
                    }

                    else -> {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.no_sim_card_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.no_sim_card_found),
                    Toast.LENGTH_SHORT
                ).show()
            }


        }

    }
}
