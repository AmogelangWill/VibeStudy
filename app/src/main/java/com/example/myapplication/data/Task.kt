package com.example.myapplication.data

import java.time.LocalDate
import java.time.LocalTime

data class Task(
    val id: String = java.util.UUID.randomUUID().toString(),
    val subject: String,
    val type: TaskType,
    val time: LocalTime,
    val duration: Int, // Duration in minutes
    val topic: String,
    val date: LocalDate
)

enum class TaskType(val displayName: String) {
    STUDY("Study"),
    HOMEWORK("Home work")
}

