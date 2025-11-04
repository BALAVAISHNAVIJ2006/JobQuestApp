
// 1. Correct the package name to match your project's structure
package com.example.jobquest.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
// 2. Import the correct R class from your app's namespace
import com.example.jobquest.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(visible = true, enter = fadeIn()) {
            // This reference should now resolve correctly
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Logo")
        }
    }
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("login") { popUpTo("splash") { inclusive = true } }
    }
}
