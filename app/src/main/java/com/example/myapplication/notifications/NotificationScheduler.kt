package com.example.myapplication.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myapplication.data.Task
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun scheduleTaskNotifications(context: Context, task: Task) {
        val workManager = WorkManager.getInstance(context)

        val taskDateTime = LocalDateTime.of(task.date, task.time)
        val now = LocalDateTime.now()

        // Don't schedule if task is in the past
        if (taskDateTime.isBefore(now)) {
            return
        }

        // Calculate delay for 10-minute reminder
        val reminderTime = taskDateTime.minusMinutes(10)
        if (reminderTime.isAfter(now)) {
            val reminderDelay = ChronoUnit.MILLIS.between(now, reminderTime)
            scheduleNotification(
                context = context,
                taskId = "${task.id}_reminder",
                taskTitle = task.subject,
                taskDescription = task.topic,
                delay = reminderDelay,
                isReminder = true
            )
        }

        // Calculate delay for exact time notification
        val exactDelay = ChronoUnit.MILLIS.between(now, taskDateTime)
        scheduleNotification(
            context = context,
            taskId = "${task.id}_exact",
            taskTitle = task.subject,
            taskDescription = task.topic,
            delay = exactDelay,
            isReminder = false
        )
    }

    private fun scheduleNotification(
        context: Context,
        taskId: String,
        taskTitle: String,
        taskDescription: String,
        delay: Long,
        isReminder: Boolean
    ) {
        val inputData = Data.Builder()
            .putString("taskTitle", taskTitle)
            .putString("taskDescription", taskDescription)
            .putBoolean("isReminder", isReminder)
            .build()

        val notificationWork = OneTimeWorkRequestBuilder<TaskNotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(taskId)
            .build()

        WorkManager.getInstance(context).enqueue(notificationWork)
    }

    fun cancelTaskNotifications(context: Context, taskId: Int) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag("${taskId}_reminder")
        workManager.cancelAllWorkByTag("${taskId}_exact")
    }
}

