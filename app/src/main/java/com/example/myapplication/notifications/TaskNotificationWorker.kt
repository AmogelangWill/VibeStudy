package com.example.myapplication.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class TaskNotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val taskTitle = inputData.getString("taskTitle") ?: "Study Task"
        val taskDescription = inputData.getString("taskDescription") ?: ""
        val isReminder = inputData.getBoolean("isReminder", false)

        showNotification(taskTitle, taskDescription, isReminder)

        return Result.success()
    }

    private fun showNotification(title: String, description: String, isReminder: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Study Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                this.description = "Notifications for study tasks and reminders"
                enableVibration(true)
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create intent to open app when notification is tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notificationTitle = if (isReminder) "⏰ Reminder: $title" else "📚 Time to Study: $title"
        val notificationText = if (isReminder) {
            "Starting in 10 minutes${if (description.isNotEmpty()) " - $description" else ""}"
        } else {
            "It's time!${if (description.isNotEmpty()) " - $description" else ""}"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_schedule)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()

        notificationManager.notify(NOTIFICATION_ID++, notification)
    }

    companion object {
        private const val CHANNEL_ID = "task_notifications"
        private var NOTIFICATION_ID = 1000
    }
}

