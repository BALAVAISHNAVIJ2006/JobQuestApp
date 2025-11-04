package com.example.jobquestapp.data.models

data class Notification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.GENERAL
)

enum class NotificationType {
    GENERAL,
    APPLICATION_UPDATE,
    INTERVIEW_REMINDER,
    DEADLINE_ALERT
}