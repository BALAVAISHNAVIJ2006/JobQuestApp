package com.example.jobquestapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(navController: NavController) {
    var showContactDialog by remember { mutableStateOf(false) }

    if (showContactDialog) {
        AlertDialog(
            onDismissRequest = { showContactDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Color(0xFF667eea),
                    modifier = Modifier.size(40.dp)
                )
            },
            title = {
                Text(
                    "Contact Support",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("Get in touch with our support team:")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Email: support@jobquest.com",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Phone: +1 (800) 123-4567",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showContactDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF667eea)
                    )
                ) {
                    Text("Close")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Help & Support",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF2D3748)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8F9FF),
                            Color(0xFFF5F7FA)
                        )
                    )
                )
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF667eea).copy(alpha = 0.1f),
                                        Color(0xFFF093FB).copy(alpha = 0.1f)
                                    )
                                )
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(56.dp),
                                shape = CircleShape,
                                color = Color.Transparent
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFF667eea),
                                                    Color(0xFF764ba2),
                                                    Color(0xFFF093FB)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SupportAgent,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "How can we help you?",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF2D3748)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "We're here to assist you 24/7",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF718096)
                                )
                            }
                        }
                    }
                }
            }

            // Get Started Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = Color(0xFF667eea).copy(alpha = 0.1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QuestionAnswer,
                                    contentDescription = null,
                                    tint = Color(0xFF667eea),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Getting Started",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF2D3748)
                            )
                        }

                        HelpOption(
                            icon = Icons.Default.LiveHelp,
                            title = "Frequently Asked Questions",
                            subtitle = "Find quick answers to common questions",
                            iconColor = Color(0xFF667eea),
                            onClick = { /* Navigate to FAQs */ }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        HelpOption(
                            icon = Icons.Default.Book,
                            title = "User Guide",
                            subtitle = "Learn how to use JobQuest effectively",
                            iconColor = Color(0xFF667eea),
                            onClick = { /* Navigate to guide */ }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        HelpOption(
                            icon = Icons.Default.VideoLibrary,
                            title = "Video Tutorials",
                            subtitle = "Watch step-by-step tutorials",
                            iconColor = Color(0xFF667eea),
                            onClick = { /* Navigate to tutorials */ }
                        )
                    }
                }
            }

            // Contact Support Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = Color(0xFF667eea).copy(alpha = 0.1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContactSupport,
                                    contentDescription = null,
                                    tint = Color(0xFF667eea),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Contact Support",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF2D3748)
                            )
                        }

                        HelpOption(
                            icon = Icons.Default.Email,
                            title = "Email Support",
                            subtitle = "Get help from our team via email",
                            iconColor = Color(0xFF667eea),
                            onClick = { showContactDialog = true }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        HelpOption(
                            icon = Icons.Default.Chat,
                            title = "Live Chat",
                            subtitle = "Chat with a support representative",
                            iconColor = Color(0xFF10B981),
                            onClick = { /* Open live chat */ }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        HelpOption(
                            icon = Icons.Default.Phone,
                            title = "Phone Support",
                            subtitle = "Call us for immediate assistance",
                            iconColor = Color(0xFF667eea),
                            onClick = { showContactDialog = true }
                        )
                    }
                }
            }

            // Report Issues Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = Color(0xFFEF4444).copy(alpha = 0.1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BugReport,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Report Issues",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF2D3748)
                            )
                        }

                        HelpOption(
                            icon = Icons.Default.BugReport,
                            title = "Report a Bug",
                            subtitle = "Help us improve the app",
                            iconColor = Color(0xFFEF4444),
                            onClick = { /* Navigate to bug report */ }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        HelpOption(
                            icon = Icons.Default.Feedback,
                            title = "Send Feedback",
                            subtitle = "Share your suggestions with us",
                            iconColor = Color(0xFF667eea),
                            onClick = { /* Navigate to feedback */ }
                        )
                    }
                }
            }

            // Legal & About Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = Color(0xFF667eea).copy(alpha = 0.1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF667eea),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Legal & About",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF2D3748)
                            )
                        }

                        HelpOption(
                            icon = Icons.Default.Policy,
                            title = "Terms & Conditions",
                            subtitle = "Read our terms of service",
                            iconColor = Color(0xFF667eea),
                            onClick = { /* Navigate to terms */ }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        HelpOption(
                            icon = Icons.Default.PrivacyTip,
                            title = "Privacy Policy",
                            subtitle = "View our privacy policy",
                            iconColor = Color(0xFF667eea),
                            onClick = { /* Navigate to privacy */ }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        HelpOption(
                            icon = Icons.Default.Info,
                            title = "About JobQuest",
                            subtitle = "Learn more about our app • Version 1.0.0",
                            iconColor = Color(0xFF667eea),
                            onClick = { /* Navigate to about */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HelpOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8F9FF)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF2D3748)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF718096),
                    lineHeight = 18.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF718096)
            )
        }
    }
}