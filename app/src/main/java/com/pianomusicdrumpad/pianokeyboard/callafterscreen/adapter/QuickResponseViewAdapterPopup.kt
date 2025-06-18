package com.pianomusicdrumpad.pianokeyboard.callafterscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.Interface.QuickResponseClick

class QuickResponseViewAdapterPopup(
    private val context: Context,
    private val responseList: List<String>,
    private val clickListener: QuickResponseClick
) : RecyclerView.Adapter<QuickResponseViewAdapterPopup.ViewHolder>() {

    // ViewHolder class
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val responseText: TextView = view.findViewById(R.id.tvResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.cfs_item_quick_response_view_popup, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.responseText.text = responseList[position]

        holder.itemView.setOnClickListener { clickListener.onMessageClick(position) }
    }

    override fun getItemCount(): Int = responseList.size
}
