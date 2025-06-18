package com.pianomusicdrumpad.pianokeyboard.callafterscreen.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment.ReminderFragment
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderData

class RemindersAdapter(private val fragment: ReminderFragment) :
    RecyclerView.Adapter<RemindersAdapter.ReminderViewHolder>() {

    private val remindersList = mutableListOf<ReminderData>()

    inner class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvReminderContent: TextView = view.findViewById(R.id.tvReminderContentText)
        private val tvReminderDate: TextView = view.findViewById(R.id.tvReminderDateText)
        private val tvReminderTime: TextView = view.findViewById(R.id.tvReminderTimeText)
        private val ivDeleteReminder: ImageView = view.findViewById(R.id.ivDeleteReminderIcon)

        fun bind(item: ReminderData) {
            tvReminderContent.text = item.content
            tvReminderDate.text = item.reminderData
            tvReminderTime.text = item.reminderTime
            Log.e("remindersList", "onBindViewHolder: Adapter reminder ${item.content}")

            ivDeleteReminder.setOnClickListener {
                fragment.deleteReminder(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cfs_item_reminder_layout, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(remindersList[position])
    }

    override fun getItemCount(): Int = remindersList.size

    fun updateList(newList: List<ReminderData>) {
        remindersList.clear()
        remindersList.addAll(newList)
        notifyDataSetChanged()
    }
}
