package com.example.jobquestapp

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JobQuestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            // Initialize Firebase
            FirebaseApp.initializeApp(this)
            Log.d("Firebase", "Firebase initialized successfully")

            // Configure Firestore
            val firestore = FirebaseFirestore.getInstance()
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            firestore.firestoreSettings = settings

            // Log Auth status
            val auth = FirebaseAuth.getInstance()
            Log.d("Firebase", "Auth instance: ${auth.app.name}")

        } catch (e: Exception) {
            Log.e("Firebase", "Firebase initialization failed", e)
        }
    }
}