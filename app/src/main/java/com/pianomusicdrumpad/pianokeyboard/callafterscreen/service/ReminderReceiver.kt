package com.pianomusicdrumpad.pianokeyboard.callafterscreen.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.window.SplashScreen
import androidx.core.app.NotificationCompat
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderDatabase
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class ReminderReceiver : BroadcastReceiver() {

    lateinit var repo: ReminderRepo

    override fun onReceive(context: Context, intent: Intent?) {
        val reminderContent = intent?.getStringExtra("reminder_content") ?: "Reminder!"
        val reminderId = intent?.getLongExtra("reminder_time", -1) ?: -1L

        if (reminderId == -1L) return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Reminders", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Reminder Notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val splashIntent = Intent(context, SplashScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            splashIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val localizedContext =
            changeLanguage(context, SharePrefUtils.getString(ConstantAd.LANGUAGE_CODE, "en"))
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.cfs_reminder_callend_image)
            .setContentTitle(localizedContext!!.getString(R.string.reminder_alert))
            .setContentText(reminderContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        val database = ReminderDatabase.getDatabase(context)
        val reminderDao = database.reminderDao()
        repo = ReminderRepo(reminderDao)
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("receiver", "onReceive: time millies $reminderId", )
           removeReminderFromDB(reminderId)
        }
    }

    private fun removeReminderFromDB(it: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.deleteReminderByTime(it)
        }
    }

    private fun changeLanguage(context: Context, languageCode: String): Context {
        val newLocale = Locale(languageCode)
        Locale.setDefault(newLocale)
        val config = Configuration()
        config.setLocale(newLocale)
        return context.createConfigurationContext(config)
    }
}