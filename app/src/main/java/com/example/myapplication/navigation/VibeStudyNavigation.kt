package com.example.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.data.ExamMonth
import com.example.myapplication.data.Grade
import com.example.myapplication.data.Subject
import com.example.myapplication.ui.components.DrawerContent
import com.example.myapplication.ui.components.VibeStudyAppBar
import com.example.myapplication.ui.screens.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibeStudyNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                onDismiss = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                VibeStudyAppBar(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(navController = navController)
                }

                composable(Screen.About.route) {
                    AboutScreen()
                }

                composable(
                    route = Screen.SubjectList.route,
                    arguments = listOf(navArgument("grade") { type = NavType.StringType })
                ) { backStackEntry ->
                    val gradeString = backStackEntry.arguments?.getString("grade")
                    val grade = Grade.valueOf(gradeString ?: "GRADE_10")
                    SubjectListScreen(grade = grade, navController = navController)
                }

                composable(
                    route = Screen.YearList.route,
                    arguments = listOf(
                        navArgument("grade") { type = NavType.StringType },
                        navArgument("subject") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val gradeString = backStackEntry.arguments?.getString("grade")
                    val subjectString = backStackEntry.arguments?.getString("subject")
                    val grade = Grade.valueOf(gradeString ?: "GRADE_10")
                    val subject = Subject.valueOf(subjectString ?: "MATHEMATICS")
                    YearListScreen(grade = grade, subject = subject, navController = navController)
                }

                composable(
                    route = Screen.MonthList.route,
                    arguments = listOf(
                        navArgument("grade") { type = NavType.StringType },
                        navArgument("subject") { type = NavType.StringType },
                        navArgument("year") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val gradeString = backStackEntry.arguments?.getString("grade")
                    val subjectString = backStackEntry.arguments?.getString("subject")
                    val year = backStackEntry.arguments?.getInt("year") ?: 2025
                    val grade = Grade.valueOf(gradeString ?: "GRADE_10")
                    val subject = Subject.valueOf(subjectString ?: "MATHEMATICS")
                    MonthListScreen(
                        grade = grade,
                        subject = subject,
                        year = year,
                        navController = navController
                    )
                }

                composable(
                    route = Screen.PaperList.route,
                    arguments = listOf(
                        navArgument("grade") { type = NavType.StringType },
                        navArgument("subject") { type = NavType.StringType },
                        navArgument("year") { type = NavType.IntType },
                        navArgument("month") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val gradeString = backStackEntry.arguments?.getString("grade")
                    val subjectString = backStackEntry.arguments?.getString("subject")
                    val year = backStackEntry.arguments?.getInt("year") ?: 2025
                    val monthString = backStackEntry.arguments?.getString("month")
                    val grade = Grade.valueOf(gradeString ?: "GRADE_10")
                    val subject = Subject.valueOf(subjectString ?: "MATHEMATICS")
                    val month = ExamMonth.valueOf(monthString ?: "MARCH")
                    PaperListScreen(
                        grade = grade,
                        subject = subject,
                        year = year,
                        month = month,
                        navController = navController
                    )
                }

                composable(Screen.MyStudySchedule.route) {
                    MyStudyScheduleScreen()
                }

                composable(Screen.Schedule.route) {
                    MyStudyScheduleScreen()
                }

                composable(Screen.LibrarySubjects.route) {
                    LibrarySubjectsScreen(navController = navController)
                }

                composable(
                    route = Screen.LibraryTopics.route,
                    arguments = listOf(navArgument("subject") { type = NavType.StringType })
                ) { backStackEntry ->
                    val subjectString = backStackEntry.arguments?.getString("subject")
                    val subject = Subject.valueOf(subjectString ?: "MATHEMATICS")
                    LibraryTopicsScreen(subject = subject, navController = navController)
                }

                composable(
                    route = Screen.PaperView.route,
                    arguments = listOf(navArgument("manifest") { type = NavType.StringType })
                ) { backStackEntry ->
                    val manifest = backStackEntry.arguments?.getString("manifest")
                    PaperViewScreen(manifestAsset = manifest ?: "math_p1_nov_2024_manifest.json")
                }
            }
        }
    }
}

