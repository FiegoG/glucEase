package com.example.uijp.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.uijp.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val activeColor = Color(0xFFFF6F61)

    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_home2),
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor
            )
        )

        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_konsultasi),
                    contentDescription = "Konsultasi",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            },
            label = { Text("Konsultasi") },
            selected = currentRoute == "konsultasi",
            onClick = { navController.navigate("konsultasi") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor
            )
        )

        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_chart),
                    contentDescription = "chart",
                    contentScale = ContentScale.Fit // Atau ContentScale.Crop / Inside
                )
            },
            label = { Text("Tracker") },
            selected = currentRoute == "tracker",
            onClick = { navController.navigate("tracker") }
        )


        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_artikel),
                    contentDescription = "Artikel",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            },
            label = { Text("Artikel") },
            selected = currentRoute == "artikel",
            onClick = { navController.navigate("artikel") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor
            )
        )

        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_laporan),
                    contentDescription = "Laporan",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            },
            label = { Text("Laporan") },
            selected = currentRoute == "laporan",
            onClick = { navController.navigate("laporan") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor
            )
        )
    }
}
