package com.example.jobquest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

// ---------------------------------------------------------------
// 1. Data models
// ---------------------------------------------------------------
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,               // epoch millis
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.GENERAL
)

enum class NotificationType { GENERAL, CONNECTION, JOB, MESSAGE }

// ---------------------------------------------------------------
// 2. Global manager (singleton) – same pattern you used in NetworkScreen
// ---------------------------------------------------------------
object NotificationManager {
    private val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())

    val notifications = mutableStateListOf<Notification>()

    init {
        // ---- Dummy data -------------------------------------------------
        notifications.addAll(
            listOf(
                Notification(
                    id = "1",
                    title = "New connection request",
                    message = "Amanda Taylor wants to connect with you",
                    timestamp = System.currentTimeMillis() - 2 * 60 * 60 * 1000,
                    type = NotificationType.CONNECTION
                ),
                Notification(
                    id = "2",
                    title = "Job application update",
                    message = "Your application for Senior Android Engineer at Google has been viewed",
                    timestamp = System.currentTimeMillis() - 5 * 60 * 60 * 1000,
                    type = NotificationType.JOB
                ),
                Notification(
                    id = "3",
                    title = "Message from John Smith",
                    message = "Hey, are you free for a quick call tomorrow?",
                    timestamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000,
                    type = NotificationType.MESSAGE
                ),
                Notification(
                    id = "4",
                    title = "Network suggestion",
                    message = "You and Michael Chen have 8 mutual connections",
                    timestamp = System.currentTimeMillis() - 48 * 60 * 60 * 1000,
                    type = NotificationType.GENERAL
                )
            )
        )
    }

    /** Helper to format timestamp into a friendly string */
    fun formatTime(timestamp: Long): String = dateFormat.format(Date(timestamp))

    /** Mark a notification as read */
    fun markAsRead(notification: Notification) {
        val idx = notifications.indexOfFirst { it.id == notification.id }
        if (idx != -1) {
            notifications[idx] = notifications[idx].copy(isRead = true)
        }
    }

    /** Delete a notification */
    fun delete(notification: Notification) {
        notifications.removeIf { it.id == notification.id }
    }

    /** Clear all notifications */
    fun clearAll() = notifications.clear()
}

// ---------------------------------------------------------------
// 3. Main composable – NotificationScreen
// ---------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    // Optional "Mark all as read" action
                    if (NotificationManager.notifications.any { !it.isRead }) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF667eea).copy(alpha = 0.1f)
                        ) {
                            TextButton(
                                onClick = {
                                    NotificationManager.notifications.forEach {
                                        if (!it.isRead) NotificationManager.markAsRead(it)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DoneAll,
                                    contentDescription = null,
                                    tint = Color(0xFF667eea),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Mark all read",
                                    color = Color(0xFF667eea),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF2D3748)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = "notifications")
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8F9FF),
                            Color(0xFFF5F7FA)
                        )
                    )
                )
                .padding(paddingValues)
        ) {

            // -------------------------------------------------------
            // 3a. Empty state
            // -------------------------------------------------------
            if (NotificationManager.notifications.isEmpty()) {
                EmptyStateCard(
                    icon = Icons.Default.NotificationsOff,
                    title = "No notifications",
                    message = "You're all caught up! Check back later."
                )
            } else {
                // ---------------------------------------------------
                // 3b. List of notifications
                // ---------------------------------------------------
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = NotificationManager.notifications,
                        key = { it.id }
                    ) { notif ->
                        NotificationCard(
                            notification = notif,
                            onMarkRead = { NotificationManager.markAsRead(notif) },
                            onDelete = { NotificationManager.delete(notif) }
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------
// 4. Re-usable UI components
// ---------------------------------------------------------------
@Composable
private fun NotificationCard(
    notification: Notification,
    onMarkRead: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color.White else Color(0xFFF0F5FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // ---- Icon (type-based) with gradient ---------------------------------
            Box {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                when (notification.type) {
                                    NotificationType.CONNECTION -> Brush.linearGradient(
                                        colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
                                    )
                                    NotificationType.JOB -> Brush.linearGradient(
                                        colors = listOf(Color(0xFF11998e), Color(0xFF38ef7d))
                                    )
                                    NotificationType.MESSAGE -> Brush.linearGradient(
                                        colors = listOf(Color(0xFFF7B731), Color(0xFFFC5C7D))
                                    )
                                    else -> Brush.linearGradient(
                                        colors = listOf(Color(0xFF718096), Color(0xFF4A5568))
                                    )
                                },
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (notification.type) {
                                NotificationType.CONNECTION -> Icons.Default.PersonAdd
                                NotificationType.JOB -> Icons.Default.Work
                                NotificationType.MESSAGE -> Icons.Default.Message
                                else -> Icons.Default.Notifications
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // Unread indicator
                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.TopEnd)
                            .background(Color(0xFFf5576c), CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // ---- Text content ---------------------------------------
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (notification.isRead) FontWeight.SemiBold else FontWeight.Bold
                    ),
                    color = Color(0xFF2D3748),
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF718096),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF718096),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = NotificationManager.formatTime(notification.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF718096),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // ---- Actions (read / delete) ---------------------------
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (!notification.isRead) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF667eea).copy(alpha = 0.1f)
                    ) {
                        IconButton(
                            onClick = onMarkRead,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Mark as read",
                                tint = Color(0xFF667eea),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFf5576c).copy(alpha = 0.1f)
                ) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Delete",
                            tint = Color(0xFFf5576c),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------
// 5. Bottom navigation - matching HomeScreen
// ---------------------------------------------------------------
@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String) {
    Surface(
        shadowElevation = 12.dp,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.95f),
                            Color.White
                        )
                    )
                )
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                tonalElevation = 0.dp,
                modifier = Modifier.height(72.dp)
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == "home") Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    label = {
                        Text(
                            "Home",
                            fontSize = 12.sp,
                            fontWeight = if (currentRoute == "home") FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667eea),
                        selectedTextColor = Color(0xFF667eea),
                        indicatorColor = Color(0xFF667eea).copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF718096),
                        unselectedTextColor = Color(0xFF718096)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == "network") Icons.Filled.Group else Icons.Outlined.Group,
                            contentDescription = "Network",
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    label = {
                        Text(
                            "Network",
                            fontSize = 12.sp,
                            fontWeight = if (currentRoute == "network") FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    selected = currentRoute == "network",
                    onClick = { navController.navigate("network") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667eea),
                        selectedTextColor = Color(0xFF667eea),
                        indicatorColor = Color(0xFF667eea).copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF718096),
                        unselectedTextColor = Color(0xFF718096)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(8.dp, CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF667eea),
                                            Color(0xFF764ba2),
                                            Color(0xFFF093FB)
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Post",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            "Post",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    selected = false,
                    onClick = { navController.navigate("create_post") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667eea),
                        unselectedIconColor = Color(0xFF667eea),
                        selectedTextColor = Color(0xFF667eea),
                        unselectedTextColor = Color(0xFF667eea)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Box {
                            Icon(
                                imageVector = if (currentRoute == "notifications") Icons.Filled.Notifications else Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                modifier = Modifier.size(26.dp)
                            )
                            Badge(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 6.dp, y = (-2).dp),
                                containerColor = Color(0xFFf5576c)
                            ) {
                                Text(
                                    "17",
                                    fontSize = 9.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    label = {
                        Text(
                            "Alerts",
                            fontSize = 12.sp,
                            fontWeight = if (currentRoute == "notifications") FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    selected = currentRoute == "notifications",
                    onClick = { navController.navigate("notifications") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667eea),
                        selectedTextColor = Color(0xFF667eea),
                        indicatorColor = Color(0xFF667eea).copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF718096),
                        unselectedTextColor = Color(0xFF718096)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == "job_list") Icons.Filled.Work else Icons.Outlined.Work,
                            contentDescription = "Jobs",
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    label = {
                        Text(
                            "Jobs",
                            fontSize = 12.sp,
                            fontWeight = if (currentRoute == "job_list") FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    selected = currentRoute == "job_list",
                    onClick = { navController.navigate("job_list") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667eea),
                        selectedTextColor = Color(0xFF667eea),
                        indicatorColor = Color(0xFF667eea).copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF718096),
                        unselectedTextColor = Color(0xFF718096)
                    )
                )
            }
        }
    }
}

// ---------------------------------------------------------------
// 6. Empty-state card
// ---------------------------------------------------------------
@Composable
fun EmptyStateCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF667eea).copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFF667eea).copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF2D3748)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF718096)
                )
            }
        }
    }
}