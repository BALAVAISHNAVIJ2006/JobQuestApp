package com.example.jobquestapp.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobquest.data.repositories.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DocumentViewModel : ViewModel() {
    private val repo = DocumentRepository()
    val documents = MutableStateFlow<List<String>>(emptyList()) // URLs

    fun uploadDocument(uri: Uri, fileName: String) {
        viewModelScope.launch {
            repo.uploadDocument(uri, fileName).onSuccess { url ->
                documents.value = documents.value + url
            }
        }
    }
}