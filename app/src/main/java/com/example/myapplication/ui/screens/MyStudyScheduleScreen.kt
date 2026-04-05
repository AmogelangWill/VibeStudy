package com.example.myapplication.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.data.Task
import com.example.myapplication.data.TaskType
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodel.TaskViewModel
import com.example.myapplication.utils.NotificationPermissionManager
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyStudyScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    val tasks by viewModel.tasks.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showConflictDialog by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var pendingTask by remember { mutableStateOf<Task?>(null) }
    val context = LocalContext.current

    // Permission launcher for notifications (Android 13+)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, add the pending task
            pendingTask?.let { task ->
                val success = viewModel.addTask(task, context)
                if (!success) {
                    showConflictDialog = true
                }
            }
            pendingTask = null
        } else {
            // Permission denied, still add task but notify user that notifications won't work
            pendingTask?.let { task ->
                val success = viewModel.addTask(task, context)
                if (!success) {
                    showConflictDialog = true
                }
            }
            pendingTask = null
            showPermissionDialog = true
        }
    }

    Scaffold(
        containerColor = AppBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryButton,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            Text(
                text = "My Study Schedule",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Manage your study tasks and reminders",
                fontSize = 14.sp,
                color = TextColor.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "📅",
                                fontSize = 48.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Text(
                                text = "No Tasks Yet",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Tap + to add your first study task or reminder",
                                fontSize = 14.sp,
                                color = DisabledText,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tasks) { task ->
                        TaskCard(
                            task = task,
                            onDelete = { viewModel.removeTask(task.id, context) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onAddTask = { task ->
                showAddDialog = false

                // Check if we have notification permission
                if (NotificationPermissionManager.hasNotificationPermission(context)) {
                    // Already have permission, add task directly
                    val success = viewModel.addTask(task, context)
                    if (!success) {
                        showConflictDialog = true
                    }
                } else {
                    // Need to request permission
                    pendingTask = task
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        // On older Android versions, just add the task
                        val success = viewModel.addTask(task, context)
                        if (!success) {
                            showConflictDialog = true
                        }
                        pendingTask = null
                    }
                }
            }
        )
    }

    if (showConflictDialog) {
        AlertDialog(
            onDismissRequest = { showConflictDialog = false },
            title = { Text("Task Conflict", fontWeight = FontWeight.Bold) },
            text = { Text("This task conflicts with an existing task. Please choose a different time.") },
            confirmButton = {
                Button(
                    onClick = { showConflictDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryButton),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("OK", color = Color.White)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("⚠️ Notifications Disabled", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Your task has been added, but you won't receive reminders because notification permission was denied.\n\n" +
                    "To enable notifications, go to:\nSettings → Apps → Vibe Study → Notifications"
                )
            },
            confirmButton = {
                Button(
                    onClick = { showPermissionDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryButton),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Got It", color = Color.White)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun TaskCard(
    task: Task,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.subject,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${task.type.displayName} • ${task.topic}",
                    fontSize = 14.sp,
                    color = TextColor.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = task.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) +
                           " at " + task.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fontSize = 13.sp,
                    color = TextColor.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Duration: ${task.duration} min",
                    fontSize = 13.sp,
                    color = TextColor.copy(alpha = 0.6f)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = TextColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (Task) -> Unit
) {
    var subject by remember { mutableStateOf("") }
    var taskType by remember { mutableStateOf(TaskType.STUDY) }
    var time by remember { mutableStateOf(LocalTime.now()) }
    var duration by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var showError by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AppBackground)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Add New Task",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Task Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = taskType.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Type of Task") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        TaskType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    taskType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Date Display
                OutlinedTextField(
                    value = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        TextButton(onClick = {
                            android.app.DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    date = LocalDate.of(year, month + 1, dayOfMonth)
                                },
                                date.year,
                                date.monthValue - 1,
                                date.dayOfMonth
                            ).show()
                        }) {
                            Text("Select", color = PrimaryButton)
                        }
                    }
                )

                // Time Picker
                OutlinedTextField(
                    value = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    onValueChange = {},
                    label = { Text("Time") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        TextButton(onClick = {
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    time = LocalTime.of(hourOfDay, minute)
                                },
                                time.hour,
                                time.minute,
                                true
                            ).show()
                        }) {
                            Text("Select", color = PrimaryButton)
                        }
                    }
                )

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it.filter { char -> char.isDigit() } },
                    label = { Text("Duration (minutes)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Topic") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (showError) {
                    Text(
                        text = "Please fill all fields",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextColor.copy(alpha = 0.7f))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (subject.isNotBlank() && duration.isNotBlank() && topic.isNotBlank()) {
                                val task = Task(
                                    subject = subject,
                                    type = taskType,
                                    time = time,
                                    duration = duration.toIntOrNull() ?: 60,
                                    topic = topic,
                                    date = date
                                )
                                onAddTask(task)
                            } else {
                                showError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryButton),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add Task", color = Color.White)
                    }
                }
            }
        }
    }
}

