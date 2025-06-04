package com.example.konsultasiprofil.UI.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PilihWaktuUI(navController: NavController, modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf(8) }
    var selectedTime by remember { mutableStateOf("5:00 pm") }

    val dates = listOf(4, 8)
    val times = listOf("1:00 pm", "3:00 pm", "5:00 pm", "7:00 pm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pilih Tanggal dan Jam",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Maret 2025",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF76E64)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Simulasi kalender (hanya sebagian)
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                userScrollEnabled = false,
                modifier = Modifier.height(200.dp)
            ) {
                val days = listOf("M", "T", "W", "T", "F", "S", "S")
                items(days.size) {
                    Text(
                        text = days[it],
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Simulasi tanggal
                val datesGrid = (1..31).toList()
                items(datesGrid.size) { index ->
                    val day = datesGrid[index]
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (day == selectedDate) Color(0xFFF76E64) else Color.Transparent
                            )
                            .clickable {
                                selectedDate = day
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$day",
                            color = if (day == selectedDate) Color.White else Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Jam Tersedia:", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                times.forEach { time ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (time == selectedTime) Color(0xFFF76E64) else Color(0xFFF1F1F1)
                            )
                            .clickable { selectedTime = time }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = time,
                            color = if (time == selectedTime) Color.White else Color.Black
                        )
                    }
                }
            }
        }

        // Tombol Lanjutkan Pembayaran
        Button(
            onClick = {navController.navigate("pembayaran")},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF76E64)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Lanjutkan Pembayaran", color = Color.White)
        }
    }
}
