package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Task
import com.example.myapplication.data.TaskRepository
import com.example.myapplication.notifications.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TaskRepository(application.applicationContext)

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        // Load tasks from persistent storage on initialization
        viewModelScope.launch {
            repository.tasksFlow.collect { loadedTasks ->
                _tasks.value = loadedTasks.sortedWith(
                    compareBy({ it.date }, { it.time })
                )
            }
        }
    }

    fun addTask(task: Task, context: android.content.Context): Boolean {
        // Check if there are conflicting tasks
        val hasConflict = _tasks.value.any { existingTask ->
            existingTask.date == task.date && tasksOverlap(existingTask, task)
        }

        if (hasConflict) {
            return false // Task conflicts with existing task
        }

        viewModelScope.launch {
            val updatedTasks = (_tasks.value + task).sortedWith(
                compareBy({ it.date }, { it.time })
            )
            _tasks.value = updatedTasks

            // Save to persistent storage (synchronous)
            repository.saveTasks(updatedTasks)

            // Schedule notifications for the task (10 min before + exact time)
            NotificationScheduler.scheduleTaskNotifications(context, task)
        }
        return true
    }

    fun removeTask(taskId: String, context: android.content.Context) {
        viewModelScope.launch {
            val updatedTasks = _tasks.value.filter { it.id != taskId }
            _tasks.value = updatedTasks

            // Save to persistent storage (synchronous)
            repository.saveTasks(updatedTasks)

            // Cancel scheduled notifications for this task
            NotificationScheduler.cancelTaskNotifications(context, taskId.toIntOrNull() ?: 0)
        }
    }

    private fun tasksOverlap(task1: Task, task2: Task): Boolean {
        val task1End = task1.time.plusMinutes(task1.duration.toLong())
        val task2End = task2.time.plusMinutes(task2.duration.toLong())

        return (task1.time.isBefore(task2End) && task2.time.isBefore(task1End))
    }
}

