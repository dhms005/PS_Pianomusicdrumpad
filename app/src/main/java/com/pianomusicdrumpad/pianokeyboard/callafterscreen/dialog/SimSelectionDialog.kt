package com.pianomusicdrumpad.pianokeyboard.callafterscreen.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.Interface.OnSimSelection
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.model.SimModel

class SimSelectionDialog(
    context: Context,
    private val availableSimList: List<SimModel>,
    private val onSimSelection: OnSimSelection
//) : Dialog(context, R.style.DialogTheme) {
) : Dialog(context) {

    init {
        // Inflate the dialog layout.
        val view = LayoutInflater.from(context).inflate(R.layout.cfs_dialog_sim_selection, null)

        // Retrieve the required views. If any are missing, a clear exception is thrown.
        val simLayout1 = view.findViewById<LinearLayout>(R.id.llSim1)
            ?: throw NullPointerException(
                "Missing required view with ID: ${
                    context.resources.getResourceName(
                        R.id.llSim1
                    )
                }"
            )
        val simLayout2 = view.findViewById<LinearLayout>(R.id.llSim2)
            ?: throw NullPointerException(
                "Missing required view with ID: ${
                    context.resources.getResourceName(
                        R.id.llSim2
                    )
                }"
            )
        val simText1 = view.findViewById<TextView>(R.id.tvSimName1)
            ?: throw NullPointerException(
                "Missing required view with ID: ${
                    context.resources.getResourceName(
                        R.id.tvSimName1
                    )
                }"
            )
        val simText2 = view.findViewById<TextView>(R.id.tvSimName2)
            ?: throw NullPointerException(
                "Missing required view with ID: ${
                    context.resources.getResourceName(
                        R.id.tvSimName2
                    )
                }"
            )

        // Set up the dialog.
        setContentView(view as RelativeLayout)
        setCancelable(true)

        // Update the UI with SIM information if available.
        if (availableSimList.size > 1) {
            simText1.text = availableSimList[0].simOperatorName
            simText2.text = availableSimList[1].simOperatorName
        }

        // Set click listeners to notify which SIM was selected.
        simLayout1.setOnClickListener { onSimSelection.onSimSelected(0) }
        simLayout2.setOnClickListener { onSimSelection.onSimSelected(1) }
    }
}
