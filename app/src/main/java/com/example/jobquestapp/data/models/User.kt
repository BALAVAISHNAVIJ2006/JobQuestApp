package com.example.jobquestapp.data.models

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val role: UserRole = UserRole.USER
)

enum class UserRole {
    USER,
    ADMIN
}