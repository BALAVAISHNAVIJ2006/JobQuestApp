package com.example.jobquestapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobquestapp.data.models.JobApplication
import com.example.jobquestapp.data.models.Notification
import com.example.jobquestapp.data.repositories.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JobViewModel : ViewModel() {
    private val repo = JobRepository()

    private val _applications = MutableStateFlow<List<JobApplication>>(emptyList())
    val applications: StateFlow<List<JobApplication>> = _applications.asStateFlow()

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    init {
        loadApplications()
        loadNotifications()
    }

    private fun loadApplications() {
        viewModelScope.launch {
            _applications.value = repo.getJobApplications()
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            // Load notifications from repository
            // For now, sample data:
            _notifications.value = listOf(
                Notification(
                    id = "1",
                    title = "Application Update",
                    message = "Your application at Google was viewed"
                ),
                Notification(
                    id = "2",
                    title = "Interview Scheduled",
                    message = "Interview scheduled for tomorrow at 2 PM"
                )
            )
        }
    }

    fun getJobApplication(id: String): StateFlow<JobApplication?> {
        return _applications.map { list ->
            list.find { it.id == id }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _applications.value.find { it.id == id }
        )
    }

    fun addJobApplication(application: JobApplication) {
        viewModelScope.launch {
            repo.addJobApplication(application)
            loadApplications() // Reload after adding
        }
    }

    fun updateJobApplication(application: JobApplication) {
        viewModelScope.launch {
            repo.updateJobApplication(application)
            loadApplications() // Reload after updating
        }
    }

    fun deleteJobApplication(id: String) {
        viewModelScope.launch {
            repo.deleteJobApplication(id)
            loadApplications() // Reload after deleting
        }
    }
}