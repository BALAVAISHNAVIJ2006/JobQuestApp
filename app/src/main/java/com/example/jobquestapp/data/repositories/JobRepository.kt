package com.example.jobquestapp.data.repositories

import com.example.jobquestapp.data.models.JobApplication
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class JobRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("job_applications")

    suspend fun getJobApplications(): List<JobApplication> {
        // Return dummy data for now
        return listOf(
            JobApplication(
                id = "1",
                jobTitle = "Software Engineer",
                companyName = "Google",
                status = "Applied",
                applicationDate = System.currentTimeMillis(),
                notes = "Initial application"
            ),
            JobApplication(
                id = "2",
                jobTitle = "Product Manager",
                companyName = "Facebook",
                status = "Interviewing",
                applicationDate = System.currentTimeMillis() - 86400000, // Yesterday
                notes = "Scheduled a phone screen"
            ),
            JobApplication(
                id = "3",
                jobTitle = "Data Scientist",
                companyName = "Netflix",
                status = "Offer",
                applicationDate = System.currentTimeMillis() - 172800000, // Two days ago
                notes = "Received an offer!"
            )
        )
    }

    suspend fun addJobApplication(application: JobApplication) {
        try {
            val docRef = collection.document()
            val appWithId = application.copy(id = docRef.id)
            docRef.set(appWithId).await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun updateJobApplication(application: JobApplication) {
        try {
            collection.document(application.id).set(application).await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun deleteJobApplication(id: String) {
        try {
            collection.document(id).delete().await()
        } catch (e: Exception) {
            // Handle error
        }
    }
}