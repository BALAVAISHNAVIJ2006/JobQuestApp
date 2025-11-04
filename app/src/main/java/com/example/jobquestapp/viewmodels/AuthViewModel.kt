package com.example.jobquest.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String, val isAdmin: Boolean = false) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole

    fun login(
        email: String,
        password: String,
        onSuccess: (isAdmin: Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _loginState.value = AuthState.Loading
                Log.d("AuthViewModel", "Attempting login for: $email")

                val result = auth.signInWithEmailAndPassword(email, password).await()

                result.user?.let { user ->
                    Log.d("AuthViewModel", "Login successful for: ${user.email}")
                    Log.d("AuthViewModel", "Email verified status: ${user.isEmailVerified}")

                    if (user.isEmailVerified) {
                        // Check user role from Firestore
                        val isAdmin = checkUserRole(user.uid)
                        _userRole.value = if (isAdmin) "admin" else "user"

                        _loginState.value = AuthState.Success("Login successful", isAdmin)
                        onSuccess(isAdmin)
                    } else {
                        auth.signOut()
                        val errorMsg = "Please verify your email before logging in"
                        _loginState.value = AuthState.Error(errorMsg)
                        onError(errorMsg)
                    }
                }
            } catch (e: FirebaseAuthException) {
                Log.e("AuthViewModel", "Firebase Auth Error: ${e.errorCode}", e)
                val errorMsg = when (e.errorCode) {
                    "ERROR_INVALID_CREDENTIAL",
                    "ERROR_WRONG_PASSWORD",
                    "ERROR_USER_NOT_FOUND" -> "Invalid email or password"
                    "ERROR_USER_DISABLED" -> "This account has been disabled"
                    "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Try again later"
                    "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Check your connection"
                    else -> "Login failed: ${e.message}"
                }
                _loginState.value = AuthState.Error(errorMsg)
                onError(errorMsg)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login error", e)
                val errorMsg = "Login failed: ${e.message}"
                _loginState.value = AuthState.Error(errorMsg)
                onError(errorMsg)
            }
        }
    }

    private suspend fun checkUserRole(userId: String): Boolean {
        return try {
            val userDoc = firestore.collection("users").document(userId).get().await()
            val role = userDoc.getString("role") ?: "user"
            Log.d("AuthViewModel", "User role: $role")
            role == "admin"
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error checking user role", e)
            false
        }
    }

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _registerState.value = AuthState.Loading
                Log.d("AuthViewModel", "Attempting registration for: $email")

                val result = auth.createUserWithEmailAndPassword(email, password).await()

                result.user?.let { user ->
                    Log.d("AuthViewModel", "User created successfully: ${user.uid}")

                    try {
                        // Create user document in Firestore with default role
                        val userData = hashMapOf(
                            "email" to email,
                            "role" to "user",
                            "createdAt" to System.currentTimeMillis()
                        )
                        firestore.collection("users").document(user.uid).set(userData).await()
                        Log.d("AuthViewModel", "Firestore user document created for: ${user.uid}")

                        // Enhanced email verification with specific error handling
                        try {
                            user.sendEmailVerification().await()
                            Log.d("AuthViewModel", "✓ Verification email sent successfully to: $email")
                            Log.d("AuthViewModel", "User UID: ${user.uid}")
                            _registerState.value = AuthState.Success("Registration successful")
                            onSuccess()
                        } catch (e: FirebaseAuthException) {
                            // Specific handling for Firebase Authentication errors
                            Log.e("AuthViewModel", "FirebaseAuthException when sending verification email: ${e.errorCode} - ${e.message}", e)
                            val errorMsg = when (e.errorCode) {
                                "ERROR_TOO_MANY_REQUESTS" -> "Too many verification emails sent. Please wait and try again."
                                "ERROR_OPERATION_NOT_ALLOWED" -> "Email verification is not enabled for this project."
                                "ERROR_INVALID_RECIPIENT_EMAIL" -> "The recipient email address is invalid."
                                "ERROR_USER_DISABLED" -> "This user account has been disabled."
                                else -> "Account created but failed to send verification email: ${e.message}"
                            }
                            _registerState.value = AuthState.Error(errorMsg)
                            onError(errorMsg)
                        } catch (e: Exception) {
                            // Generic exception for network issues or other problems
                            Log.e("AuthViewModel", "Generic error sending verification email: ${e.message}", e)
                            Log.e("AuthViewModel", "Exception type: ${e.javaClass.simpleName}")
                            val errorMsg = "Account created but failed to send verification email: ${e.message}"
                            _registerState.value = AuthState.Error(errorMsg)
                            onError(errorMsg)
                        }
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Failed to create Firestore document", e)
                        val errorMsg = "Failed to complete registration: ${e.message}"
                        _registerState.value = AuthState.Error(errorMsg)
                        onError(errorMsg)
                    }
                }
            } catch (e: FirebaseAuthException) {
                Log.e("AuthViewModel", "Firebase Auth Error during registration: ${e.errorCode}", e)
                val errorMsg = when (e.errorCode) {
                    "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already registered"
                    "ERROR_WEAK_PASSWORD" -> "Password is too weak"
                    "ERROR_INVALID_EMAIL" -> "Invalid email format"
                    "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Check your connection"
                    else -> "Registration failed: ${e.message}"
                }
                _registerState.value = AuthState.Error(errorMsg)
                onError(errorMsg)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration error", e)
                val errorMsg = "Registration failed: ${e.message}"
                _registerState.value = AuthState.Error(errorMsg)
                onError(errorMsg)
            }
        }
    }

    fun resetPassword(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                Log.d("AuthViewModel", "Password reset email sent to: $email")
                onSuccess()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Failed to send password reset email", e)
                onError("Failed to send reset email: ${e.message}")
            }
        }
    }

    fun logout() {
        auth.signOut()
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
        _userRole.value = null
        Log.d("AuthViewModel", "User logged out")
    }
}