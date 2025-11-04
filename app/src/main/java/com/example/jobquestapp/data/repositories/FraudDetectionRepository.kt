package com.example.jobquest.data.repositories

import com.example.jobquestapp.data.models.Company
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class FraudDetectionRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {

    suspend fun detectFakeCompany(company: Company): Boolean {
        // Simulate ML via Cloud Function
        val data = hashMapOf(
            "name" to company.name,
            "address" to company.location,
            "reviews" to company.reviews
        )
        return try {
            val result: HttpsCallableResult = functions
                .getHttpsCallable("detectFraud")
                .call(data)
                .await()

            // Use getData() method instead of data property
            val resultData = result.getData() as? Map<*, *>
            resultData?.get("isFraud") as? Boolean ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun manualFlagCompany(company: Company) {
        try {
            db.collection("flaggedCompanies")
                .add(company)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getFlaggedCompanies(): List<Company> {
        return try {
            val snapshot = db.collection("flaggedCompanies")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Company::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}