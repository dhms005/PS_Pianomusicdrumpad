package com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert
    fun insertReminderInDB(reminder: ReminderData)

    @Query("SELECT * FROM ReminderData")
    fun getReminders(): Flow<List<ReminderData>>

    @Query("DELETE FROM ReminderData WHERE id = :id")
    fun deleteReminderById(id: Long)

    @Query("DELETE FROM ReminderData WHERE timeMillies = :id")
    fun deleteReminderByTime(id: Long)
}