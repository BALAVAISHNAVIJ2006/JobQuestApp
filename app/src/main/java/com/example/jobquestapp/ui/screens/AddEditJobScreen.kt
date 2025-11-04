package com.example.jobquest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Job(
    val id: String,
    val title: String,
    val company: String,
    val location: String = "",
    val salary: String = "",
    val status: JobStatus,
    val notes: String = "",
    val appliedDate: String = "",
    val description: String = ""
)

enum class JobStatus {
    APPLIED,
    INTERVIEW,
    OFFER,
    REJECTED,
    WISHLIST
}

object JobsManager {
    val jobs = mutableStateListOf<Job>()

    fun addJob(job: Job) {
        jobs.add(job)
    }

    fun updateJob(updatedJob: Job) {
        val index = jobs.indexOfFirst { it.id == updatedJob.id }
        if (index != -1) {
            jobs[index] = updatedJob
        }
    }

    fun deleteJob(jobId: String) {
        jobs.removeAll { it.id == jobId }
    }

    fun getJobById(jobId: String): Job? {
        return jobs.find { it.id == jobId }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditJobScreen(
    navController: NavController,
    jobId: String? = null
) {
    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf(JobStatus.WISHLIST) }
    var notes by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var appliedDate by remember { mutableStateOf("") }
    var showStatusDropdown by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val isEditMode = jobId != null && jobId != "new"

    // Load existing job data if editing
    LaunchedEffect(jobId) {
        if (isEditMode) {
            JobsManager.getJobById(jobId!!)?.let { job ->
                title = job.title
                company = job.company
                location = job.location
                salary = job.salary
                selectedStatus = job.status
                notes = job.notes
                description = job.description
                appliedDate = job.appliedDate
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                navController.navigateUp()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = if (isEditMode) "Job Updated!" else "Job Added!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isEditMode)
                        "Your job application has been updated successfully."
                    else
                        "Your job application has been added successfully."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigateUp()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF667eea)
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Edit Job" else "Add Job",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF2D3748)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Job Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Job Title *") },
                placeholder = { Text("e.g. Senior Software Engineer") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667eea),
                    focusedLabelColor = Color(0xFF667eea)
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = Color(0xFF667eea)
                    )
                }
            )

            // Company
            OutlinedTextField(
                value = company,
                onValueChange = { company = it },
                label = { Text("Company *") },
                placeholder = { Text("e.g. Google") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667eea),
                    focusedLabelColor = Color(0xFF667eea)
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = Color(0xFF667eea)
                    )
                }
            )

            // Location
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                placeholder = { Text("e.g. San Francisco, CA") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667eea),
                    focusedLabelColor = Color(0xFF667eea)
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF667eea)
                    )
                }
            )

            // Salary
            OutlinedTextField(
                value = salary,
                onValueChange = { salary = it },
                label = { Text("Salary Range") },
                placeholder = { Text("e.g. $100k - $150k") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667eea),
                    focusedLabelColor = Color(0xFF667eea)
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = Color(0xFF667eea)
                    )
                }
            )

            // Status Dropdown
            ExposedDropdownMenuBox(
                expanded = showStatusDropdown,
                onExpandedChange = { showStatusDropdown = it }
            ) {
                OutlinedTextField(
                    value = selectedStatus.name.replace("_", " "),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status *") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showStatusDropdown)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF667eea),
                        focusedLabelColor = Color(0xFF667eea)
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = getStatusColor(selectedStatus)
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = showStatusDropdown,
                    onDismissRequest = { showStatusDropdown = false }
                ) {
                    JobStatus.values().forEach { status ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Circle,
                                        contentDescription = null,
                                        tint = getStatusColor(status),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(status.name.replace("_", " "))
                                }
                            },
                            onClick = {
                                selectedStatus = status
                                showStatusDropdown = false
                            }
                        )
                    }
                }
            }

            // Applied Date
            OutlinedTextField(
                value = appliedDate,
                onValueChange = { appliedDate = it },
                label = { Text("Applied Date") },
                placeholder = { Text("e.g. 2025-01-15") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667eea),
                    focusedLabelColor = Color(0xFF667eea)
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF667eea)
                    )
                }
            )

            // Job Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Job Description") },
                placeholder = { Text("Enter job description...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667eea),
                    focusedLabelColor = Color(0xFF667eea)
                ),
                maxLines = 5
            )

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                placeholder = { Text("Add any additional notes...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667eea),
                    focusedLabelColor = Color(0xFF667eea)
                ),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel Button
                OutlinedButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF718096)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancel", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                // Save Button
                Button(
                    onClick = {
                        if (title.isNotBlank() && company.isNotBlank()) {
                            val job = Job(
                                id = jobId ?: System.currentTimeMillis().toString(),
                                title = title,
                                company = company,
                                location = location,
                                salary = salary,
                                status = selectedStatus,
                                notes = notes,
                                appliedDate = appliedDate,
                                description = description
                            )

                            if (isEditMode) {
                                JobsManager.updateJob(job)
                            } else {
                                JobsManager.addJob(job)
                            }

                            showSuccessDialog = true
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF667eea)
                    ),
                    enabled = title.isNotBlank() && company.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isEditMode) "Update" else "Save",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Delete Button (only in edit mode)
            if (isEditMode) {
                var showDeleteDialog by remember { mutableStateOf(false) }

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(48.dp)
                            )
                        },
                        title = {
                            Text(
                                "Delete Job?",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        text = {
                            Text("Are you sure you want to delete this job application? This action cannot be undone.")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    JobsManager.deleteJob(jobId!!)
                                    showDeleteDialog = false
                                    navController.navigateUp()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD32F2F)
                                )
                            ) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD32F2F)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFD32F2F)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Job", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun getStatusColor(status: JobStatus): Color {
    return when (status) {
        JobStatus.APPLIED -> Color(0xFF2196F3)
        JobStatus.INTERVIEW -> Color(0xFFFF9800)
        JobStatus.OFFER -> Color(0xFF4CAF50)
        JobStatus.REJECTED -> Color(0xFFD32F2F)
        JobStatus.WISHLIST -> Color(0xFF9C27B0)
    }
}