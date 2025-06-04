package com.example.uijp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TtsAnnotation
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uijp.view.konsultasi.*
import com.example.uijp.view.artikel.ArtikelScreen
import com.example.uijp.view.laporan.LaporanMingguanPage
import com.example.uijp.view.tracker.TrackerGulaScreen
import com.example.uijp.view.HomeScreen

@Composable
fun MainNavigation(navController: NavHostController) {
    val buttomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = buttomNavController) }
    ) { paddingValues ->
        NavHost(
            navController = buttomNavController,
            startDestination = "home", // Default start destination
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("konsultasi") { KonsultasiUi(navController) }
            composable("artikel") { ArtikelScreen(navController) }
            composable("laporan") { LaporanMingguanPage(navController) }
            composable("tracker") { TrackerGulaScreen(navController) }
        }
    }
}
