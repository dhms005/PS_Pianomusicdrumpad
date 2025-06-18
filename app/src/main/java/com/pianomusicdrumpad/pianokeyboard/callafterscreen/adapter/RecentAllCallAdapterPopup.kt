package com.pianomusicdrumpad.pianokeyboard.callafterscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.Interface.RecentCallClick
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.model.ItemRecent
import com.pianomusicdrumpad.pianokeyboard.R

class RecentAllCallAdapterPopup(
    private val context: Context,
    private val itemList: List<ItemRecent>,
    private val clickListener: RecentCallClick
) : RecyclerView.Adapter<RecentAllCallAdapterPopup.ViewHolder>() {

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.cfs_item_recent_calls_popup, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        if (item.totalCount > 1) {

            if (item.name != "") {
                "${item.name} (${item.totalCount})".also { holder.nameTextView.text = it }
            } else {
                "${item.number} (${item.totalCount})".also { holder.nameTextView.text = it }
            }
        } else {
            if (item.name != "") {
                holder.nameTextView.text = item.name
            } else {
                holder.nameTextView.text = item.number
            }

        }

        holder.timeTextView.text = CommonUtils.formatTimestamp(item.time)
        holder.simIdTextView.text = item.simId

        when (item.simId) {
            CommonUtils.sim1Text -> {
                holder.simTypeImageView.setImageResource(R.drawable.cfs_ic_sim_1)
            }

            CommonUtils.sim2Text -> {
                holder.simTypeImageView.setImageResource(R.drawable.cfs_ic_sim_2)
            }

            else -> {
                holder.simTypeImageView.setImageResource(R.drawable.cfs_ic_sim_1)
            }
        }

        when (item.type) {
            3 -> {
                holder.callTypeImageView.setImageResource(R.drawable.cfs_ic_miss)
            }

            1 -> {
                holder.callTypeImageView.setImageResource(R.drawable.cfs_ic_incoming)
            }

            else -> {
                holder.callTypeImageView.setImageResource(R.drawable.cfs_ic_outgoing)
            }
        }

        holder.nameTextView.setTextColor(
            context.getColor(
                if (item.type == 3) R.color.light_red else R.color.label_color_light_primary
            )
        )

        holder.itemView.setOnClickListener { clickListener.onRecentCallClick(position) }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvName)
        val timeTextView: TextView = view.findViewById(R.id.tvTime)
        val simIdTextView: TextView = view.findViewById(R.id.tvSim)
        val callTypeImageView: ImageView = view.findViewById(R.id.ivCallType)
        val simTypeImageView: ImageView = view.findViewById(R.id.ivSim)
        val dividerView: View = view.findViewById(R.id.view)
    }
}
