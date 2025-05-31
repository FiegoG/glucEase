package com.example.uijp.view.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uijp.viewmodel.SugarTrackerViewModel
import com.example.uijp.viewmodel.SugarTrackerViewModelFactory
import com.example.uijp.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TrackerGulaScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModelFactory = remember { SugarTrackerViewModelFactory(context) }
    val viewModel: SugarTrackerViewModel = viewModel(factory = viewModelFactory)

    val dailyTrackerState by viewModel.dailyTrackerState.collectAsState()
    val message by viewModel.message.collectAsState()

    // Show message if any
    LaunchedEffect(message) {
        message?.let {
            // You can show a snackbar or toast here if needed
            viewModel.clearMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { navController.navigate("home") }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            Text(
                "Tracker Asupan Gula Harian",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        Spacer(Modifier.height(36.dp))

        // Content based on state
        when (val state = dailyTrackerState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFF67669)
                    )
                }
            }

            is UiState.Error -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color(0xFFFFEBEE),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Gagal memuat data",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFFD32F2F)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            state.message,
                            fontSize = 14.sp,
                            color = Color(0xFF7A7A7A)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadDailyTracker() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF67669)
                            )
                        ) {
                            Text("Coba Lagi", color = Color.White)
                        }
                    }
                }
            }

            is UiState.Success -> {
                val data = state.data

                // Konsumsi Gula Harian
                Card(
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color(0xFFFFFFFF),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val progress = (data.summary.percentage_of_recommendation / 100).toFloat().coerceIn(0f, 1f)

                        CircularProgressIndicator(
                            progress = progress,
                            modifier = Modifier.size(100.dp),
                            strokeWidth = 10.dp,
                            backgroundColor = Color(0xFFEEEEEE),
                            color = when {
                                progress < 0.5f -> Color(0xFF4CAF50) // Green
                                progress < 0.8f -> Color(0xFFFF9800) // Orange
                                else -> Color(0xFFF67669) // Red
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "${String.format("%.1f", data.summary.total_sugar)}g / ${data.summary.recommended_daily_intake}g",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        val statusText = when {
                            progress < 0.5f -> "Status: Aman"
                            progress < 0.8f -> "Status: Mendekati Batas"
                            else -> "Status: Melebihi Batas Harian"
                        }

                        Text(
                            statusText,
                            fontSize = 14.sp,
                            color = Color(0xFF7A7A7A),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(36.dp))

                Text(
                    "Daftar Makanan Hari Ini",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (data.consumed_foods.isEmpty()) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = Color(0xFFF5F5F5),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Belum ada makanan yang dicatat",
                                fontSize = 14.sp,
                                color = Color(0xFF7A7A7A),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Tambahkan makanan untuk mulai tracking",
                                fontSize = 12.sp,
                                color = Color(0xFF999999)
                            )
                        }
                    }
                } else {
                    data.consumed_foods.forEach { consumedFood ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEAEAEA)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    consumedFood.food_name.take(1).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF666666)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    consumedFood.food_name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    "Kandungan gula: ${String.format("%.1f", consumedFood.total_sugar)}g",
                                    fontSize = 12.sp,
                                    color = Color(0xFF7A7A7A)
                                )
                                if (consumedFood.quantity > 1) {
                                    Text(
                                        "Porsi: ${consumedFood.quantity}x",
                                        fontSize = 12.sp,
                                        color = Color(0xFF999999)
                                    )
                                }
                            }

                            // Format waktu
                            val timeText = try {
                                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                                val date = inputFormat.parse(consumedFood.consumed_at)
                                outputFormat.format(date ?: Date())
                            } catch (e: Exception) {
                                consumedFood.consumed_at.take(5) // Fallback
                            }

                            Text(
                                timeText,
                                fontSize = 12.sp,
                                color = Color(0xFF999999)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Tambah Makanan
        Button(
            onClick = { navController.navigate("tambahmakanan") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF67669)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Tambah Makanan",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}