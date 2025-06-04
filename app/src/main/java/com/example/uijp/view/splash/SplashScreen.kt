package com.example.uijp.view.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import com.example.uijp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Navigasi otomatis setelah delay
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("onboard1") {
            popUpTo("splash") { inclusive = true }
        }
    }

    // UI Splash dengan Logo
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.glucease_logo), // pastikan nama sesuai
                contentDescription = "Logo Aplikasi",
                modifier = Modifier.size(150.dp)
            )
        }
    }
}
