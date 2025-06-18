package com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb

import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderDao
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderData
import kotlinx.coroutines.flow.Flow

class ReminderRepo ( private val dao: ReminderDao) {

    fun insertReminder(reminder: ReminderData) {
        dao.insertReminderInDB(reminder)
    }

    fun getReminders() : Flow<List<ReminderData>> {
        return dao.getReminders()
    }

    fun deleteReminderById(id: Long) {
        dao.deleteReminderById(id)
    }

    fun deleteReminderByTime(id: Long) {
        dao.deleteReminderByTime(id)
    }

}