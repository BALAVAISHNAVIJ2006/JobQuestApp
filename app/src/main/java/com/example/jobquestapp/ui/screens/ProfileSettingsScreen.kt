package com.example.jobquestapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jobquestapp.data.models.Post
import com.example.jobquestapp.data.models.PostsManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Posts", "About", "Activity")

    // Dialogs state
    var showEditDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Post editing/deletion states
    var showEditPostDialog by remember { mutableStateOf(false) }
    var showDeletePostDialog by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<Post?>(null) }
    var editPostContent by remember { mutableStateOf("") }

    // User data
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Edit profile fields
    var editName by remember { mutableStateOf(currentUser?.displayName ?: "") }
    var editRole by remember { mutableStateOf("Software Developer") }
    var editCompany by remember { mutableStateOf("Tech Company") }
    var editLocation by remember { mutableStateOf("Tirupati, Andhra Pradesh") }
    var editBio by remember { mutableStateOf("Passionate software developer with 5+ years of experience") }

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
        // In production, upload to Firebase Storage
    }

    // Get user's posts - force recomposition when posts change
    val userPosts by remember {
        derivedStateOf {
            PostsManager.posts.filter { it.userId == currentUser?.uid }
        }
    }

    // Edit Post Dialog
    if (showEditPostDialog && selectedPost != null) {
        AlertDialog(
            onDismissRequest = { showEditPostDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color(0xFF667eea),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    "Edit Post",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = editPostContent,
                        onValueChange = { editPostContent = it },
                        label = { Text("Post Content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp),
                        minLines = 5,
                        maxLines = 10,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF667eea),
                            focusedLabelColor = Color(0xFF667eea)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${editPostContent.length} / 3000 characters",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (editPostContent.length > 2500) Color(0xFFf5576c) else Color(0xFF718096)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedPost?.let { post ->
                            val index = PostsManager.posts.indexOfFirst { it.id == post.id }
                            if (index != -1) {
                                PostsManager.posts[index] = post.copy(content = editPostContent)
                            }
                        }
                        showEditPostDialog = false
                        selectedPost = null
                    },
                    enabled = editPostContent.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF667eea)
                    )
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEditPostDialog = false
                    selectedPost = null
                }) {
                    Text("Cancel", color = Color(0xFF718096))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Delete Post Confirmation Dialog
    if (showDeletePostDialog && selectedPost != null) {
        AlertDialog(
            onDismissRequest = { showDeletePostDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(40.dp)
                )
            },
            title = {
                Text(
                    "Delete Post?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete this post? This action cannot be undone.",
                    color = Color(0xFF718096)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedPost?.let { post ->
                            PostsManager.posts.removeIf { it.id == post.id }
                        }
                        showDeletePostDialog = false
                        selectedPost = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF4444)
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeletePostDialog = false
                    selectedPost = null
                }) {
                    Text("Cancel", color = Color(0xFF718096))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Edit Profile Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    "Edit Profile",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF667eea),
                                focusedLabelColor = Color(0xFF667eea)
                            )
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = editRole,
                            onValueChange = { editRole = it },
                            label = { Text("Job Title") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF667eea),
                                focusedLabelColor = Color(0xFF667eea)
                            )
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = editCompany,
                            onValueChange = { editCompany = it },
                            label = { Text("Company") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF667eea),
                                focusedLabelColor = Color(0xFF667eea)
                            )
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = editLocation,
                            onValueChange = { editLocation = it },
                            label = { Text("Location") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF667eea),
                                focusedLabelColor = Color(0xFF667eea)
                            )
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = editBio,
                            onValueChange = { editBio = it },
                            label = { Text("Bio") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 5,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF667eea),
                                focusedLabelColor = Color(0xFF667eea)
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Update Firebase user profile
                        currentUser?.updateProfile(
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(editName)
                                .build()
                        )
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF667eea)
                    )
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = Color(0xFF718096))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Share Profile Dialog
    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = Color(0xFF667eea),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    "Share Profile",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ShareOption(
                        icon = Icons.Default.Link,
                        text = "Copy profile link",
                        onClick = { /* Copy link */ }
                    )
                    ShareOption(
                        icon = Icons.Default.Email,
                        text = "Share via Email",
                        onClick = { /* Share email */ }
                    )
                    ShareOption(
                        icon = Icons.Default.Message,
                        text = "Share via Message",
                        onClick = { /* Share message */ }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showShareDialog = false }) {
                    Text("Close", color = Color(0xFF667eea))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Enhanced Settings Dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color(0xFF667eea),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    "Settings",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SettingsOption(
                        icon = Icons.Default.Lock,
                        text = "Privacy Settings",
                        onClick = {
                            showSettingsDialog = false
                            navController.navigate("privacy_settings")
                        }
                    )
                    SettingsOption(
                        icon = Icons.Default.Security,
                        text = "Security Settings",
                        onClick = {
                            showSettingsDialog = false
                            navController.navigate("security_settings")
                        }
                    )
                    SettingsOption(
                        icon = Icons.Default.Help,
                        text = "Help & Support",
                        onClick = {
                            showSettingsDialog = false
                            navController.navigate("help_support")
                        }
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = Color(0xFFE2E8F0)
                    )
                    SettingsOption(
                        icon = Icons.Default.Logout,
                        text = "Logout",
                        onClick = {
                            auth.signOut()
                            showSettingsDialog = false
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        textColor = Color(0xFFEF4444)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Close", color = Color(0xFF667eea))
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
                        "Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF2D3748)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = "profile"
            )
        }
    ) { paddingValues ->
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
                .padding(paddingValues)
        ) {
            // Profile Header
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile Picture with Camera Icon
                        Box {
                            if (profileImageUri != null) {
                                AsyncImage(
                                    model = profileImageUri,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(110.dp)
                                        .shadow(8.dp, CircleShape)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Surface(
                                    modifier = Modifier
                                        .size(110.dp)
                                        .shadow(8.dp, CircleShape),
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
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(55.dp)
                                        )
                                    }
                                }
                            }

                            // Camera Icon Button
                            Surface(
                                modifier = Modifier
                                    .size(36.dp)
                                    .align(Alignment.BottomEnd)
                                    .clickable { imagePickerLauncher.launch("image/*") },
                                shape = CircleShape,
                                color = Color(0xFF667eea),
                                shadowElevation = 6.dp
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Change photo",
                                    tint = Color.White,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = currentUser?.displayName ?: "User",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF2D3748)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = editRole,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF667eea)
                        )

                        Text(
                            text = editCompany,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF718096)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ProfileStat("Posts", userPosts.size.toString())
                            ProfileStat("Connections", "342")
                            ProfileStat("Following", "128")
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { showEditDialog = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF667eea)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Edit Profile", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { showShareDialog = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF667eea)
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    width = 2.dp
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Share", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Tabs
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.White,
                        contentColor = Color(0xFF667eea),
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = Color(0xFF667eea),
                                height = 3.dp
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontWeight = if (selectedTab == index)
                                            FontWeight.Bold
                                        else
                                            FontWeight.Medium,
                                        fontSize = 15.sp
                                    )
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Content based on selected tab
            when (selectedTab) {
                0 -> {
                    // Posts Tab
                    if (userPosts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .background(
                                                Brush.radialGradient(
                                                    colors = listOf(
                                                        Color(0xFF667eea).copy(alpha = 0.1f),
                                                        Color.Transparent
                                                    )
                                                ),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PostAdd,
                                            contentDescription = null,
                                            tint = Color(0xFF667eea).copy(alpha = 0.6f),
                                            modifier = Modifier.size(64.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        "No posts yet",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color(0xFF2D3748)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "Share your first post with your network!",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF718096)
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Button(
                                        onClick = { navController.navigate("create_post") },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF667eea)
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.Add, null)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Create Post", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    } else {
                        items(
                            items = userPosts,
                            key = { it.hashCode() }
                        ) { post ->
                            UserPostCard(
                                post = post,
                                onEdit = {
                                    selectedPost = post
                                    editPostContent = post.content
                                    showEditPostDialog = true
                                },
                                onDelete = {
                                    selectedPost = post
                                    showDeletePostDialog = true
                                },
                                onLike = { /* Handle like */ },
                                onComment = { /* Handle comment */ },
                                onRepost = { /* Handle repost */ },
                                onShare = { /* Handle share */ }
                            )
                        }
                    }
                }
                1 -> {
                    // About Tab
                    item {
                        AboutSection(editBio, editLocation, currentUser?.email ?: "")
                    }
                }
                2 -> {
                    // Activity Tab
                    item {
                        ActivitySection()
                    }
                }
            }
        }
    }
}

@Composable
fun UserPostCard(
    post: Post,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onRepost: () -> Unit,
    onShare: () -> Unit
) {
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var likesCount by remember { mutableStateOf(post.likes) }
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        color = Color.White,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            // Post Header with Menu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(48.dp),
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
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = post.userName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF2D3748)
                        )
                        Text(
                            text = post.userRole,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF718096)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.timeAgo,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF718096),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = Color(0xFF718096),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                // Post Options Menu
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Post options",
                            tint = Color(0xFF718096)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = Color(0xFF667eea),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text("Edit Post", fontWeight = FontWeight.Medium)
                                }
                            },
                            onClick = {
                                showMenu = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        "Delete Post",
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFEF4444)
                                    )
                                }
                            },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            // Post Content
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 24.sp
                ),
                color = Color(0xFF2D3748),
                modifier = Modifier.padding(horizontal = 18.dp),
                fontSize = 16.sp
            )

            // Post Image (if exists)
            if (post.imageUrl != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .height(320.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    color = Color(0xFFF5F7FA)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFF5F7FA),
                                        Color(0xFFE2E8F0)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Post image",
                            tint = Color(0xFFCBD5E0),
                            modifier = Modifier.size(72.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Engagement row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .shadow(4.dp, CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF667eea),
                                        Color(0xFF764ba2)
                                    )
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$likesCount",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF2D3748),
                        fontSize = 14.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "${post.comments} comments",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF718096),
                        fontSize = 13.sp
                    )
                    Text(
                        text = "${post.reposts} reposts",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF718096),
                        fontSize = 13.sp
                    )
                }
            }

            Divider(
                color = Color(0xFFE2E8F0).copy(alpha = 0.5f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 18.dp)
            )

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EnhancedPostInteractionButton(
                    icon = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                    text = "Like",
                    onClick = {
                        isLiked = !isLiked
                        likesCount = if (isLiked) likesCount + 1 else likesCount - 1
                        onLike()
                    },
                    isActive = isLiked
                )
                EnhancedPostInteractionButton(Icons.Outlined.ChatBubbleOutline, "Comment", onComment)
                EnhancedPostInteractionButton(Icons.Outlined.Repeat, "Repost", onRepost)
                EnhancedPostInteractionButton(Icons.Outlined.Send, "Share", onShare)
            }
        }
    }
}

@Composable
fun ShareOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF667eea),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2D3748)
            )
        }
    }
}

@Composable
fun SettingsOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    textColor: Color = Color(0xFF2D3748)
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }
    }
}

@Composable
fun ProfileStat(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF2D3748)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF718096)
        )
    }
}

@Composable
fun AboutSection(bio: String, location: String, email: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "About",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF2D3748)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = bio,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF718096),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(20.dp))

            InfoRow(Icons.Default.Work, "Software Developer at Tech Company")
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(Icons.Default.School, "B.Tech in Computer Science")
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(Icons.Default.LocationOn, location)
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(Icons.Default.Email, email)
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF667eea),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF2D3748)
        )
    }
}

@Composable
fun ActivitySection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF2D3748)
            )
            Spacer(modifier = Modifier.height(20.dp))

            ActivityItem(
                icon = Icons.Default.ThumbUp,
                text = "Liked a post by John Smith",
                time = "2 hours ago"
            )
            Spacer(modifier = Modifier.height(16.dp))
            ActivityItem(
                icon = Icons.Default.Comment,
                text = "Commented on Sarah's post",
                time = "5 hours ago"
            )
            Spacer(modifier = Modifier.height(16.dp))
            ActivityItem(
                icon = Icons.Default.PersonAdd,
                text = "Connected with Michael Chen",
                time = "1 day ago"
            )
            Spacer(modifier = Modifier.height(16.dp))
            ActivityItem(
                icon = Icons.Default.Share,
                text = "Shared an article",
                time = "2 days ago"
            )
        }
    }
}

@Composable
fun ActivityItem(icon: ImageVector, text: String, time: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = Color(0xFF667eea).copy(alpha = 0.1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF667eea),
                modifier = Modifier.padding(11.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2D3748)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF718096),
                fontSize = 13.sp
            )
        }
    }
}