package com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderData
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderRepo
import kotlinx.coroutines.flow.Flow


class ReminderViewModel (application: Application, private val repo: ReminderRepo) : AndroidViewModel(application) {

    fun insertReminder(reminder: ReminderData){
        repo.insertReminder(reminder)
    }

    fun getReminders() : Flow<List<ReminderData>> {
        return repo.getReminders()
    }

    fun deleteReminderById(id: Long){
        repo.deleteReminderById(id)
    }
}