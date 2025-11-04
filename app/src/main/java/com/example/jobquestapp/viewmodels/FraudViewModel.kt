package com.example.jobquestapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobquestapp.data.models.Company
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FraudViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val functions = FirebaseFunctions.getInstance()

    private val _fraudCheckResult = MutableStateFlow<FraudCheckState>(FraudCheckState.Idle)
    val fraudCheckResult: StateFlow<FraudCheckState> = _fraudCheckResult.asStateFlow()

    fun detectFake(company: Company, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _fraudCheckResult.value = FraudCheckState.Loading
            try {
                val isFake = detectFakeCompany(company)
                _fraudCheckResult.value = FraudCheckState.Success(isFake)
                onResult(isFake)
            } catch (e: Exception) {
                _fraudCheckResult.value = FraudCheckState.Error(e.message ?: "Fraud detection failed")
                onResult(false)
            }
        }
    }

    private suspend fun detectFakeCompany(company: Company): Boolean {
        return try {
            // Check if company is flagged in Firestore
            val flaggedDoc = db.collection("flagged_companies")
                .document(company.id.ifEmpty { company.name })
                .get()
                .await()

            if (flaggedDoc.exists()) {
                return true // Already flagged
            }

            // Optional: Call Firebase Function for AI-based detection
            try {
                val data = hashMapOf(
                    "companyName" to company.name,
                    "location" to (company.location ?: ""),
                    "description" to (company.description ?: "")
                )

                val result = functions
                    .getHttpsCallable("detectFraudulentCompany")
                    .call(data)
                    .await()

                // Fix: Use getData() method instead of accessing data property
                val resultData = result.getData() as? Map<*, *>
                val isFraudulent = resultData?.get("isFraudulent") as? Boolean ?: false

                if (isFraudulent) {
                    manualFlag(company)
                }

                isFraudulent
            } catch (_: Exception) {
                // If Firebase Function fails, return false
                false
            }
        } catch (_: Exception) {
            false
        }
    }

    fun manualFlag(company: Company, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val flagData = hashMapOf(
                    "companyId" to company.id.ifEmpty { company.name },
                    "companyName" to company.name,
                    "location" to (company.location ?: ""),
                    "flaggedAt" to System.currentTimeMillis(),
                    "reason" to "Manual flag"
                )

                db.collection("flagged_companies")
                    .document(company.id.ifEmpty { company.name })
                    .set(flagData)
                    .await()

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to flag company")
            }
        }
    }
}

sealed class FraudCheckState {
    object Idle : FraudCheckState()
    object Loading : FraudCheckState()
    data class Success(val isFraudulent: Boolean) : FraudCheckState()
    data class Error(val message: String) : FraudCheckState()
}