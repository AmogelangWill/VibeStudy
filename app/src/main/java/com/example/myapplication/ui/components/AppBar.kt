package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.Grade
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.PrimaryButton
import com.example.myapplication.ui.theme.TextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibeStudyAppBar(
    onMenuClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppBackground,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hamburger Menu Icon
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.size(28.dp),
                    tint = TextColor
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logo in the middle - 180dp LARGE, fits exactly in 180dp Row with NO extra space
            Image(
                painter = painterResource(id = R.drawable.logo_header),
                contentDescription = "Vibe Study Logo",
                contentScale = androidx.compose.ui.layout.ContentScale.Inside,
                modifier = Modifier.fillMaxHeight()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Transparent spacer to balance hamburger icon
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    onDismiss: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color(0xFFF8F9FA)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header with logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryButton.copy(alpha = 0.1f), shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "VibeStudy Logo",
                        modifier = Modifier.height(100.dp)

                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vibe Study",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Home Card (Mint Green)
            DrawerCard(
                title = "Home",
                backgroundColor = Color(0xFFE8F5E9),
                icon = Icons.Default.Home,
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // GRADES Section Header
            Text(
                text = "GRADES",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextColor.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Grade 10 Card (Soft Peach)
            DrawerCard(
                title = "Grade 10",
                backgroundColor = Color(0xFFFFF3E0),
                icon = Icons.Default.School,
                onClick = {
                    navController.navigate(Screen.SubjectList.createRoute(Grade.GRADE_10))
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Grade 11 Card (Soft Blue)
            DrawerCard(
                title = "Grade 11",
                backgroundColor = Color(0xFFE3F2FD),
                icon = Icons.Default.School,
                onClick = {
                    navController.navigate(Screen.SubjectList.createRoute(Grade.GRADE_11))
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Grade 12 Card (Soft Purple)
            DrawerCard(
                title = "Grade 12",
                backgroundColor = Color(0xFFF3E5F5),
                icon = Icons.Default.School,
                onClick = {
                    navController.navigate(Screen.SubjectList.createRoute(Grade.GRADE_12))
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // About Card (Soft Pink)
            DrawerCard(
                title = "About",
                backgroundColor = Color(0xFFFCE4EC),
                icon = Icons.Default.Info,
                onClick = {
                    navController.navigate(Screen.About.route)
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Version footer
            Text(
                text = "Version 1.0.0",
                fontSize = 12.sp,
                color = TextColor.copy(alpha = 0.4f),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun DrawerCard(
    title: String,
    backgroundColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon in white box
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = TextColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextColor
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "›",
                fontSize = 24.sp,
                color = TextColor.copy(alpha = 0.3f)
            )
        }
    }
}

