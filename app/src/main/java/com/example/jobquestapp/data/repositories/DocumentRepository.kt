package com.example.jobquest.data.repositories

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class DocumentRepository {
    private val storage = FirebaseStorage.getInstance()
    private val userId = AuthRepository().getCurrentUserId() ?: ""

    suspend fun uploadDocument(uri: Uri, fileName: String): Result<String> {
        return try {
            val ref = storage.reference.child("users/$userId/documents/$fileName")
            ref.putFile(uri).await()
            val url = ref.downloadUrl.await().toString()
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // List documents, delete, etc.
}