package com.example.jobquestapp.data.models

data class Company(
    val id: String = "",
    val name: String = "",
    val location: String? = "",
    val address: String = "",
    val description: String? = "",
    val reviews: List<String> = emptyList(),
    val isFake: Boolean = false
)