package com.example.jobquest.data.models

import com.google.firebase.firestore.DocumentId

data class JobApplication(
    @DocumentId
    val id: String = "",
    val jobTitle: String = "",
    val companyName: String = "",
    val companyId: String = "",
    val applicationDate: Long = System.currentTimeMillis(),
    val status: ApplicationStatus = ApplicationStatus.APPLIED,
    val jobDescription: String = "",
    val salary: String = "",
    val location: String = "",
    val jobType: String = "", // Full-time, Part-time, Contract, etc.
    val notes: String = "",
    val interviewDate: Long? = null,
    val documents: List<String> = emptyList(), // URLs to uploaded documents
    val userId: String = ""
)

enum class ApplicationStatus {
    APPLIED,
    UNDER_REVIEW,
    INTERVIEW_SCHEDULED,
    INTERVIEWED,
    OFFERED,
    ACCEPTED,
    REJECTED,
    WITHDRAWN
}