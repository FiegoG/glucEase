package com.example.uijp.gamifikasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uijp.R
import com.example.uijp.viewmodel.MissionViewModel

@Composable
fun DetailMisi(
    navController: NavController,
    missionId: String,
    missionViewModel: MissionViewModel // Pass the ViewModel
) {
    // Fetch mission detail when the composable is launched or missionId changes
    LaunchedEffect(missionId) {
        if (missionId.isNotBlank()) {
            missionViewModel.fetchMissionDetail(missionId)
        }
    }

    // Clear the selected mission when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            missionViewModel.clearSelectedMissionDetail()
        }
    }

    val selectedMission by missionViewModel.selectedMissionDetail.collectAsState()
    val isLoading by missionViewModel.isLoading.collectAsState()
    val errorMessage by missionViewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF67669))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top Bar with Back Button and Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding() // Add padding for the status bar
                    .height(56.dp) // Standard toolbar height
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )
                // Spacer to push title to center if needed, or use weight
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "Detail Misi",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF181818),
                    modifier = Modifier.weight(1f), // Allow text to take space
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(28.dp)) // To balance the back button space for centering
            }

            // Spacer(modifier = Modifier.height(19.dp)) // Adjusted from 75 (top padding) - 56 (toolbar)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 19.dp) // Spacing between top bar and content area
                    .background(
                        color = Color(0xFFFCFCFC),
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    ),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (errorMessage != null) {
                    Text(
                        text = "Error: $errorMessage",
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                } else if (selectedMission != null) {
                    val mission = selectedMission!!
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 120.dp) // Added more top padding, ensure bottom padding for button
                    ) {
                        InsideDetailMisi(
                            taskName = mission.title,
                            taskDescription = mission.description ?: "Tidak ada deskripsi.",
                            taskTime = "Tersedia hingga pukul 23.59 hari ini" // Static as per requirement
                        )
                    }
                } else if (missionId.isBlank()){
                    Text(
                        text = "ID Misi tidak valid.",
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    // This case might occur if loading is finished, no error, but mission is null
                    // (e.g. mission not found but backend returned success:false without specific error msg handled by ViewModel)
                    Text(
                        text = "Detail misi tidak ditemukan.",
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }


                Button(
                    onClick = {
                        // Navigate to the screen where the user can complete the mission.
                        // This might involve navigating to a specific exercise screen,
                        // or if it's just a conceptual "start mission", then back to gamifikasi.
                        // For now, let's assume it goes back to the gamifikasi overview or a dedicated task screen.
                        // If the button is just to acknowledge, it might just pop back or navigate.
                        navController.navigate("gamifikasi") { // Or relevant screen
                            popUpTo("gamifikasi") { inclusive = true } // Example: go back to gamifikasi main
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 70.dp)
                        .width(180.dp)
                        .height(42.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF67669),
                        contentColor = Color.White
                    ),
                    // Disable button if mission details are not loaded, or if it's already completed/expired (add logic if needed)
                    enabled = selectedMission != null && !isLoading
                ) {
                    Text(
                        "Selesaikan Misi", // Or "Kerjakan Misi"
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InsideDetailMisi(taskName: String, taskDescription: String, taskTime: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), // Consider reducing this if top padding in parent is large
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = taskName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold, // Made bold for more emphasis
                color = Color(0xFF120D26),
                textAlign = TextAlign.Center
            )
        }

        // Spacer(modifier = Modifier.height(22.dp)) // Can be adjusted

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp) // Added top padding for separation
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_desc),
                contentDescription = "Deskripsi Icon",
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Deskripsi",
                fontSize = 15.sp, // Slightly larger for section title
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = taskDescription,
            fontSize = 14.sp, // Slightly larger for readability
            fontWeight = FontWeight.Normal, // Changed from Light for better readability
            color = Color(0xFF494950),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_time),
                contentDescription = "Time Icon",
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Durasi Aktif",
                fontSize = 15.sp, // Slightly larger
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = taskTime,
            fontSize = 14.sp, // Slightly larger
            fontWeight = FontWeight.Normal, // Changed from Light
            color = Color(0xFF494950)
        )
    }
}
