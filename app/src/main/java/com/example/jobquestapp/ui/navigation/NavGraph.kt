package com.example.jobquest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.jobquest.ui.screens.AddEditJobScreen
import com.example.jobquest.ui.screens.AnalyticsScreen
import com.example.jobquest.ui.screens.DocumentManagementScreen
import com.example.jobquest.ui.screens.FakeDetectionScreen
import com.example.jobquestapp.ui.screens.HomeScreen
import com.example.jobquest.ui.screens.JobDetailScreen
import com.example.jobquest.ui.screens.JobListScreen
import com.example.jobquestapp.ui.screens.ProfileSettingsScreen
import com.example.jobquest.ui.screens.SplashScreen
import com.example.jobquest.ui.screens.NotificationScreen
import com.example.jobquestapp.ui.screens.PrivacySettingsScreen
import com.example.jobquestapp.ui.screens.SecuritySettingsScreen
import com.example.jobquestapp.ui.screens.HelpSupportScreen
import com.example.jobquestapp.ui.screens.auth.ChangePasswordScreen
import com.example.jobquestapp.ui.screens.auth.EditProfileScreen
import com.example.jobquestapp.ui.screens.auth.CreatePostScreen
import com.example.jobquestapp.ui.screens.auth.NetworkScreen
import com.example.jobquestapp.ui.screens.auth.MessagesScreen
import com.example.jobquest.ui.screens.auth.LoginScreen
import com.example.jobquestapp.ui.screens.auth.RegisterScreen
import com.example.jobquest.ui.screens.auth.ResetPasswordScreen
import com.example.jobquestapp.ui.screens.admin.AdminHomeScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("reset_password") { ResetPasswordScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("admin_home") { AdminHomeScreen(navController) }
        composable("job_list") { JobListScreen(navController) }
        composable("job_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            JobDetailScreen(navController, id)
        }
        composable("add_edit_job/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            AddEditJobScreen(navController, id)
        }
        composable("documents") { DocumentManagementScreen(navController) }
        composable("notifications") { NotificationScreen(navController) }
        composable("profile") { ProfileSettingsScreen(navController) }
        composable("analytics") { AnalyticsScreen(navController) }
        composable("fake_detection") { FakeDetectionScreen(navController) }
        composable("edit_profile") { EditProfileScreen(navController) }
        composable("privacy_settings") { PrivacySettingsScreen(navController) }
        composable("security_settings") { SecuritySettingsScreen(navController) }
        composable("help_support") { HelpSupportScreen(navController) }
        composable("change_password") { ChangePasswordScreen(navController) }
        composable("create_post") { CreatePostScreen(navController) }
        composable("network") { NetworkScreen(navController) }
        composable("messages") { MessagesScreen(navController) }

    }
}