package com.example.myapplication.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONArray
import org.json.JSONObject

class TaskRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("tasks_prefs", Context.MODE_PRIVATE)

    private val _tasksFlow = MutableStateFlow<List<Task>>(loadTasksFromPrefs())
    val tasksFlow: Flow<List<Task>> = _tasksFlow

    private fun loadTasksFromPrefs(): List<Task> {
        val tasksJson = prefs.getString("tasks_list", "[]") ?: "[]"
        return try {
            val jsonArray = JSONArray(tasksJson)
            (0 until jsonArray.length()).map { i ->
                val obj = jsonArray.getJSONObject(i)
                Task(
                    id = obj.getString("id"),
                    subject = obj.getString("subject"),
                    type = TaskType.valueOf(obj.getString("type")),
                    time = java.time.LocalTime.of(obj.getInt("timeHour"), obj.getInt("timeMinute")),
                    duration = obj.getInt("duration"),
                    topic = obj.getString("topic"),
                    date = java.time.LocalDate.of(
                        obj.getInt("dateYear"),
                        obj.getInt("dateMonth"),
                        obj.getInt("dateDayOfMonth")
                    )
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveTasks(tasks: List<Task>) {
        val jsonArray = JSONArray()
        tasks.forEach { task ->
            val obj = JSONObject().apply {
                put("id", task.id)
                put("subject", task.subject)
                put("type", task.type.name)
                put("timeHour", task.time.hour)
                put("timeMinute", task.time.minute)
                put("duration", task.duration)
                put("topic", task.topic)
                put("dateYear", task.date.year)
                put("dateMonth", task.date.monthValue)
                put("dateDayOfMonth", task.date.dayOfMonth)
            }
            jsonArray.put(obj)
        }

        prefs.edit().putString("tasks_list", jsonArray.toString()).apply()
        _tasksFlow.value = tasks
    }
}
