package com.example.uijp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.uijp.R
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults





@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Menambahkan jarak antar elemen
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Halo, User", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Row {
                IconButton(onClick = {navController.navigate("reminder")}) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notification")
                }
                IconButton(onClick = {navController.navigate("profil")}) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                }
                IconButton(onClick = {navController.navigate("login")}) {
                    Icon(Icons.Default.Lock, contentDescription = "Logout")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Statistik Kesehatan Harian
        Text("Statistik Kesehatan Harian", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            StatistikCard(
                title = "Gula Darah",
                value = "110 mg/dL",
                icon = painterResource(id = R.drawable.ic_blood),
                backgroundColor = Color(0xFFE1D5FB),
                modifier = Modifier
                    .width(130.dp)
                    .height(170.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            StatistikCard(
                title = "Asupan Gula",
                value = "32g / 40g",
                icon = painterResource(id = R.drawable.ic_sugar),
                backgroundColor = Color(0xFFB2EBF2),
                modifier = Modifier
                    .width(130.dp)
                    .height(170.dp)
            )
        }



        Spacer(modifier = Modifier.height(24.dp))

        // Fitur yang Tersedia
        Text("Fitur yang tersedia", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            FeatureButton("Konsultasi", painterResource(id = R.drawable.ic_konsultasifix)) {
                navController.navigate("konsultasi") {
                    popUpTo("home") { inclusive = false }
                    launchSingleTop = true
                }

            }
            FeatureButton("Tracker Gula Darah", painterResource(id = R.drawable.ic_chartfix)) {
                navController.navigate("guladarah")
            }
            FeatureButton("Reward", painterResource(id = R.drawable.ic_rewardfix)) {
                navController.navigate("reward")
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        // Daily Mission
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Daily Mission", fontWeight = FontWeight.SemiBold)
            TextButton(onClick = { navController.navigate("gamifikasi") }) {
                Text("Selesaikan Misi ->")
            }
        }

        DailyMissionList(navController)

        Spacer(modifier = Modifier.height(24.dp)) // Pastikan ada jarak sebelum artikel

        // Artikel Pilihan
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Artikel Pilihan untuk Kamu", fontWeight = FontWeight.SemiBold)
            TextButton(onClick = {navController.navigate("artikel")}) {
                Text("Baca semua")
            }
        }
        ArticleList() // Pastikan ini ditempatkan dengan benar setelah Spacer
    }
}


@Composable
fun StatistikCard(
    title: String,
    value: String,
    icon: Painter,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )
            Column {
                Text(text = title, fontSize = 14.sp)
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}



@Composable
fun FeatureButton(text: String, iconPainter: Painter, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Image(
            painter = iconPainter,
            contentDescription = text,
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFFFFFFF))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}




@Composable
fun DailyMissionList(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MissionItem("Cek gula darah & input ke tracker", false, navController)
        MissionItem("Asupan gula < 50g hari ini", false, navController)
        MissionItem("Gunakan fitur tracker hari ini", true, navController)
        MissionItem("Jalan minimal 3000 langkah", false, navController)
        MissionItem("Baca 1 artikel kesehatan di GulaCare", false, navController)
    }
}


@Composable
fun MissionItem(text: String, locked: Boolean, navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (locked) showDialog = true
            },
        colors = CardDefaults.cardColors(
            containerColor = if (locked) Color(0xFFEEEEEE) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (locked) Icons.Default.Lock else Icons.Default.CheckCircle,
                contentDescription = if (locked) "Locked" else "Completed",
                tint = if (locked) Color.Gray else Color(0xFF4CAF50), // hijau untuk selesai
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = if (locked) Color.Gray else Color.Black
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        navController.navigate("premium")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF67669),
                        contentColor = Color.White
                    )
                ) {
                    Text("Langganan Sekarang")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Tutup")
                }
            },
            title = { Text("Fitur Premium") },
            text = { Text("Untuk mengakses misi ini, kamu perlu berlangganan GulaCare Premium.") }
        )
    }
}



     @Composable
fun ArticleList() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) { index ->
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(120.dp)
                    .border(
                        width = 2.dp,
                        color = Color.LightGray, // Warna abu-abu muda
                        shape = RoundedCornerShape(8.dp)
                    ),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        "Aktivitas Ringan yang Efektif Menurunkan Gula Darah",
                        fontSize = 14.sp,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Kemarin", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
    }
}
