package com.example.jobquestapp.data.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Post(
    val id: String,
    val userName: String,
    val userRole: String,
    val timeAgo: String,
    val content: String,
    val imageUrl: String? = null,
    val likes: Int = 0,
    val comments: Int = 0,
    val reposts: Int = 0,
    val isLiked: Boolean = false,
    val companyName: String? = null,
    val userId: String? = null // Track post owner
)

object PostsManager {
    val posts = mutableStateListOf<Post>()

    init {
        // Sample posts
        posts.addAll(
            listOf(
                Post(
                    id = "1",
                    userName = "Sarah Johnson",
                    userRole = "Senior Product Manager at Google",
                    timeAgo = "2h",
                    content = "Excited to share that our team just launched a new feature that will help millions of users! 🚀 The journey from concept to launch has been incredible. Grateful for the amazing team.",
                    likes = 234,
                    comments = 45,
                    reposts = 12,
                    userId = "user_sarah"
                ),
                Post(
                    id = "2",
                    userName = "Michael Chen",
                    userRole = "Software Engineer at Microsoft",
                    timeAgo = "5h",
                    content = "Just finished a deep dive into Kotlin Coroutines. The way structured concurrency works is beautiful! Here are my top 3 takeaways...",
                    imageUrl = "tech_image",
                    likes = 156,
                    comments = 28,
                    reposts = 8,
                    userId = "user_michael"
                ),
                Post(
                    id = "3",
                    userName = "Emily Davis",
                    userRole = "UX Designer",
                    timeAgo = "1d",
                    content = "Design tip: Always start with user research. The best designs come from understanding real user pain points, not assumptions.",
                    likes = 89,
                    comments = 15,
                    reposts = 5,
                    companyName = "Meta",
                    userId = "user_emily"
                )
            )
        )
    }

    fun addPost(post: Post) {
        posts.add(0, post) // Add to beginning of list
    }

    fun getUserPosts(userId: String?): List<Post> {
        return if (userId != null) {
            posts.filter { it.userId == userId }
        } else {
            emptyList()
        }
    }
}

private fun SnapshotStateList<Post>.add(
    index: Int,
    element: Any
) {
}
