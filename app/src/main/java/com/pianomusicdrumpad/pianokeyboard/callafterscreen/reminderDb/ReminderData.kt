package com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReminderData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "Content") val content: String,
    @ColumnInfo(name = "reminderDate") val reminderData: String,
    @ColumnInfo(name = "reminderTime") val reminderTime: String,
    @ColumnInfo(name = "timeMillies") val timeMillies: Long
    )