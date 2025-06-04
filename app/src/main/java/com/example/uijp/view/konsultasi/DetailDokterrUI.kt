package com.example.uijp.view.konsultasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uijp.R
import com.example.uijp.viewmodel.DetailDokterViewModel
import com.example.uijp.viewmodel.DetailDokterViewModelFactory

@Composable
fun DetailDokterUI(
    navController: NavController,
    doctorId: Int = 1, // Default doctor ID, should be passed from navigation
    modifier: Modifier = Modifier
) {
    val viewModelFactory = remember { DetailDokterViewModelFactory(doctorId) }
    val viewModel: DetailDokterViewModel = viewModel(factory = viewModelFactory)

    val uiState by viewModel.uiState.collectAsState()
    val doctorDetail by viewModel.doctorDetail.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTime by viewModel.selectedTime.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFFFFF))
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Text("Jadwalkan Konsultasi", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(24.dp)) // Balance the layout
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFFF6B6B))
                }
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.error!!,
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.retryLoadDoctorDetail() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF6B6B)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Coba Lagi", color = Color.White)
                    }
                }
            }

            doctorDetail != null -> {
                DoctorDetailContent(
                    doctorDetail = doctorDetail!!,
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    onDateSelected = viewModel::selectDate,
                    onTimeSelected = viewModel::selectTime,
                    onContinueClicked = {
                        if (viewModel.canProceedToPayment()) {
                            navController.navigate("pembayaran")
                        }
                    },
                    getAvailableDates = viewModel::getAvailableDates,
                    getAvailableTimesForSelectedDate = viewModel::getAvailableTimesForSelectedDate,
                    canProceedToPayment = viewModel.canProceedToPayment()
                )
            }
        }
    }
}

@Composable
private fun DoctorDetailContent(
    doctorDetail: com.example.uijp.data.model.DoctorDetail,
    selectedDate: String,
    selectedTime: String,
    onDateSelected: (String) -> Unit,
    onTimeSelected: (String) -> Unit,
    onContinueClicked: () -> Unit,
    getAvailableDates: () -> List<Pair<String, String>>,
    getAvailableTimesForSelectedDate: () -> List<String>,
    canProceedToPayment: Boolean
) {
    Column {
        // Doctor Photo
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.dokterpilih),
                contentDescription = "Foto Dokter",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Doctor Name and Info
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = doctorDetail.doctor.doctor_name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = doctorDetail.doctor.expertise,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = doctorDetail.doctor.rating.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Biography
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8F9FA), shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text("Biografi Singkat", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = doctorDetail.doctor.bio,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rp${doctorDetail.doctor.consultation_fee.toString().reversed().chunked(3).joinToString(".").reversed()} / sesi (30 menit)",
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFF6B6B),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Schedule Selection
        Text("Jadwal Konsultasi:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))

        // Date Selection
        val availableDates = getAvailableDates()
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableDates) { (dayName, dayNumber) ->
                val isSelected = selectedDate == dayNumber
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0xFFFF6B6B) else Color(0xFFF0F0F0))
                        .clickable { onDateSelected(dayNumber) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = dayName,
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 12.sp
                    )
                    Text(
                        text = dayNumber,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Time Selection
        if (selectedDate.isNotEmpty()) {
            Text("Jam Tersedia:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            val availableTimes = getAvailableTimesForSelectedDate()
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableTimes) { time ->
                    val isSelected = selectedTime == time
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFFFF6B6B) else Color(0xFFF0F0F0))
                            .clickable { onTimeSelected(time) }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = time,
                            color = if (isSelected) Color.White else Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Continue Button
        Button(
            onClick = onContinueClicked,
            enabled = canProceedToPayment,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (canProceedToPayment) Color(0xFFFF6B6B) else Color.Gray,
                disabledBackgroundColor = Color.Gray
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Lanjutkan",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

