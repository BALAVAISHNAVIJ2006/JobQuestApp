package com.example.jobquestapp.data.models

data class JobApplication(
    val id: String = "",
    val jobTitle: String = "",
    val companyName: String = "",
    val status: String = "Applied",
    val applicationDate: Long = 0L,
    val notes: String = "",
    val documents: List<String> = emptyList() // Firebase Storage URLs
)