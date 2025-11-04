package com.example.jobquestapp.ui.screens

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jobquestapp.data.models.Post
import com.example.jobquestapp.data.models.PostsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    // Access the global posts list
    val posts = PostsManager.posts

    // Filter posts based on search query
    val filteredPosts = remember(searchQuery, posts) {
        if (searchQuery.isBlank()) {
            posts
        } else {
            posts.filter { post ->
                post.content.contains(searchQuery, ignoreCase = true) ||
                        post.userName.contains(searchQuery, ignoreCase = true) ||
                        post.userRole.contains(searchQuery, ignoreCase = true) ||
                        (post.companyName?.contains(searchQuery, ignoreCase = true) == true)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Animated gradient background with floating orbs effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2),
                            Color(0xFFF093FB),
                            Color(0xFFF5F7FA)
                        ),
                        startY = 0f,
                        endY = 1500f
                    )
                )
        )

        // Floating orb effect 1
        Box(
            modifier = Modifier
                .offset(x = (-50).dp, y = 100.dp)
                .size(200.dp)
                .background(
                    Color(0xFF667eea).copy(alpha = 0.15f),
                    CircleShape
                )
                .blur(50.dp)
        )

        // Floating orb effect 2
        Box(
            modifier = Modifier
                .offset(x = 250.dp, y = 400.dp)
                .size(150.dp)
                .background(
                    Color(0xFFF093FB).copy(alpha = 0.2f),
                    CircleShape
                )
                .blur(60.dp)
        )

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = "home"
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Premium Glass-morphism Top Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF667eea).copy(alpha = 0.05f),
                                        Color(0xFFF093FB).copy(alpha = 0.05f)
                                    )
                                )
                            )
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                // Premium animated profile picture
                                val infiniteTransition = rememberInfiniteTransition(label = "profile_glow")
                                val profileGlow by infiniteTransition.animateFloat(
                                    initialValue = 0.8f,
                                    targetValue = 1.2f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(2000, easing = FastOutSlowInEasing),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "profile_glow"
                                )

                                Box(modifier = Modifier.clickable { navController.navigate("profile") }) {
                                    // Glow effect
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .scale(profileGlow)
                                            .background(
                                                Brush.radialGradient(
                                                    colors = listOf(
                                                        Color(0xFF667eea).copy(alpha = 0.3f),
                                                        Color.Transparent
                                                    )
                                                ),
                                                CircleShape
                                            )
                                            .blur(8.dp)
                                    )

                                    Surface(
                                        modifier = Modifier
                                            .size(48.dp)
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
                                                contentDescription = "Profile",
                                                tint = Color.White,
                                                modifier = Modifier.size(26.dp)
                                            )
                                        }
                                    }
                                }

                                // Premium search bar with glass effect
                                val searchScale by animateFloatAsState(
                                    targetValue = if (isSearchActive) 1.02f else 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    label = "search_scale"
                                )

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .scale(searchScale)
                                        .shadow(
                                            elevation = if (isSearchActive) 12.dp else 4.dp,
                                            shape = RoundedCornerShape(25.dp)
                                        ),
                                    shape = RoundedCornerShape(25.dp),
                                    color = if (isSearchActive)
                                        Color.White
                                    else
                                        Color.White.copy(alpha = 0.7f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                if (isSearchActive) {
                                                    Brush.horizontalGradient(
                                                        colors = listOf(
                                                            Color(0xFF667eea).copy(alpha = 0.08f),
                                                            Color(0xFFF093FB).copy(alpha = 0.08f)
                                                        )
                                                    )
                                                } else {
                                                    Brush.horizontalGradient(
                                                        colors = listOf(Color.White, Color.White)
                                                    )
                                                }
                                            )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 18.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Search",
                                                tint = if (isSearchActive)
                                                    Color(0xFF667eea)
                                                else
                                                    Color(0xFF718096),
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            BasicTextField(
                                                value = searchQuery,
                                                onValueChange = {
                                                    searchQuery = it
                                                    isSearchActive = it.isNotEmpty()
                                                },
                                                textStyle = TextStyle(
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF2D3748),
                                                    fontWeight = FontWeight.Medium
                                                ),
                                                decorationBox = { innerTextField ->
                                                    Box {
                                                        if (searchQuery.isEmpty()) {
                                                            Text(
                                                                "Discover amazing content...",
                                                                style = TextStyle(
                                                                    fontSize = 16.sp,
                                                                    color = Color(0xFF718096)
                                                                )
                                                            )
                                                        }
                                                        innerTextField()
                                                    }
                                                },
                                                modifier = Modifier.weight(1f)
                                            )

                                            AnimatedVisibility(
                                                visible = searchQuery.isNotEmpty(),
                                                enter = fadeIn() + scaleIn(),
                                                exit = fadeOut() + scaleOut()
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        searchQuery = ""
                                                        isSearchActive = false
                                                    },
                                                    modifier = Modifier.size(36.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Clear,
                                                        contentDescription = "Clear",
                                                        tint = Color(0xFF718096),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // Premium message icon with notification dot
                                Box {
                                    Surface(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .shadow(4.dp, CircleShape),
                                        shape = CircleShape,
                                        color = Color.White.copy(alpha = 0.9f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clickable { navController.navigate("messages") },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                                contentDescription = "Messages",
                                                tint = Color(0xFF2D3748),
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                    // Active indicator dot
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .align(Alignment.TopEnd)
                                            .offset(x = (-2).dp, y = 2.dp)
                                            .background(Color(0xFFf5576c), CircleShape)
                                            .border(2.dp, Color.White, CircleShape)
                                    )
                                }
                            }

                            // Premium search results indicator
                            AnimatedVisibility(
                                visible = searchQuery.isNotEmpty(),
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
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
                                        .padding(horizontal = 20.dp, vertical = 12.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF667eea).copy(alpha = 0.15f),
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                Text(
                                                    text = "${filteredPosts.size}",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF667eea)
                                                    )
                                                )
                                            }
                                        }
                                        Text(
                                            text = "result${if (filteredPosts.size != 1) "s" else ""} found",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF667eea)
                                            ),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Posts feed with premium animations
                AnimatedContent(
                    targetState = filteredPosts.isEmpty() && searchQuery.isNotEmpty(),
                    label = "posts_content",
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith
                                fadeOut(animationSpec = tween(300))
                    }
                ) { isEmpty ->
                    if (isEmpty) {
                        // Premium empty state
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(48.dp)
                            ) {
                                Box {
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
                                            )
                                    )
                                    Icon(
                                        imageVector = Icons.Default.SearchOff,
                                        contentDescription = null,
                                        tint = Color(0xFF667eea).copy(alpha = 0.6f),
                                        modifier = Modifier
                                            .size(100.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    "No results found",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2D3748)
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "Try adjusting your search terms",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF718096)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            items(
                                items = filteredPosts,
                                key = { it.hashCode() }
                            ) { post ->
                                EnhancedPostCard(
                                    post = post,
                                    onLike = { /* Handle like */ },
                                    onComment = { /* Handle comment */ },
                                    onRepost = { /* Handle repost */ },
                                    onShare = { /* Handle share */ },
                                    onProfileClick = { /* Navigate to profile */ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    Surface(
        shadowElevation = 12.dp,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.95f),
                            Color.White
                        )
                    )
                )
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                tonalElevation = 0.dp,
                modifier = Modifier.height(72.dp)
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == "home") Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    label = {
                        Text(
                            "Home",
                            fontSize = 12.sp,
                            fontWeight = if (currentRoute == "home") FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667eea),
                        selectedTextColor = Color(0xFF667eea),
                        indicatorColor = Color(0xFF667eea).copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF718096),
                        unselectedTextColor = Color(0xFF718096)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == "network") Icons.Filled.Group else Icons.Outlined.Group,
                            contentDescription = "Network",
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    label = {
                        Text(
                            "Network",
                            fontSize = 12.sp,
                            fontWeight = if (currentRoute == "network") FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    selected = currentRoute == "network",
                    onClick = { navController.navigate("network") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667eea),
                        selectedTextColor = Color(0xFF667eea),
                        indicatorColor = Color(0xFF667eea).copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF718096),
                        unselectedTextColor = Color(0xFF718096)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(8.dp, CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF667eea),
                                            Color(0xFF764ba2),
                                            Color(0xFFF093FB)
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Post",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            "Post",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    selected = false,
                    onClick = { navController.navigate("create_post") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667eea),
                        unselectedIconColor = Color(0xFF667eea),
                        selectedTextColor = Color(0xFF667eea),
                        unselectedTextColor = Color(0xFF667eea)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Box {
                            Icon(
                                imageVector = if (currentRoute == "notifications") Icons.Filled.Notifications else Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                modifier = Modifier.size(26.dp)
                            )
                            Badge(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 6.dp, y = (-2).dp),
                                containerColor = Color(0xFFf5576c)
                            ) {
                                Text(
                                    "17",
                                    fontSize = 9.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    label = {
                        Text(
                            "Alerts",
                            fontSize = 12.sp,
                            fontWeight = if (currentRoute == "notifications") FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    selected = currentRoute == "notifications",
                    onClick = { navController.navigate("notifications") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667eea),
                        selectedTextColor = Color(0xFF667eea),
                        indicatorColor = Color(0xFF667eea).copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF718096),
                        unselectedTextColor = Color(0xFF718096)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == "job_list") Icons.Filled.Work else Icons.Outlined.Work,
                            contentDescription = "Jobs",
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    label = {
                        Text(
                            "Jobs",
                            fontSize = 12.sp,
                            fontWeight = if (currentRoute == "job_list") FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    selected = currentRoute == "job_list",
                    onClick = { navController.navigate("job_list") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667eea),
                        selectedTextColor = Color(0xFF667eea),
                        indicatorColor = Color(0xFF667eea).copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF718096),
                        unselectedTextColor = Color(0xFF718096)
                    )
                )
            }
        }
    }
}

@Composable
fun EnhancedPostCard(
    post: Post,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onRepost: () -> Unit,
    onShare: () -> Unit,
    onProfileClick: () -> Unit
) {
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var likesCount by remember { mutableStateOf(post.likes) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        color = Color.White,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            // Post Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Box {
                        // Glow effect for profile
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF667eea).copy(alpha = 0.2f),
                                            Color.Transparent
                                        )
                                    ),
                                    CircleShape
                                )
                                .blur(6.dp)
                        )

                        Surface(
                            modifier = Modifier
                                .size(54.dp)
                                .clickable(onClick = onProfileClick)
                                .shadow(4.dp, CircleShape),
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
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = post.userName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF2D3748)
                        )
                        if (post.companyName != null) {
                            Text(
                                text = "reposted this",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF718096)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    modifier = Modifier.size(30.dp),
                                    shape = CircleShape,
                                    color = Color(0xFFF5F7FA)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Business,
                                        contentDescription = null,
                                        tint = Color(0xFF667eea),
                                        modifier = Modifier.padding(7.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = post.companyName,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = Color(0xFF2D3748)
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
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                        } else {
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
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                }

                Row {
                    if (post.companyName != null) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF667eea).copy(alpha = 0.1f)
                        ) {
                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color(0xFF667eea)
                                ),
                                modifier = Modifier.height(36.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Follow",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Follow",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color(0xFF718096)
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

            // Engagement row with premium design
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

            // Action Buttons with premium animations
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
fun EnhancedPostInteractionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    isActive: Boolean = false
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isActive)
            Color(0xFF667eea).copy(alpha = 0.1f)
        else
            Color.Transparent
    ) {
        TextButton(
            onClick = onClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (isActive) Color(0xFF667eea) else Color(0xFF718096)
            ),
            modifier = Modifier.scale(scale)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                ),
                fontSize = 14.sp
            )
        }
    }
}