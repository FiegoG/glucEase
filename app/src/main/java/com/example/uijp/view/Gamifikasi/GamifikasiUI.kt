package com.example.uijp.view.Gamifikasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.GoogleFont.Provider
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import  com.example.uijp.R
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.viewmodel.MissionViewModel
import com.example.uijp.viewmodel.MissionViewModelFactory

val provider = Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val roboto = GoogleFont("Roboto")

val robotoFontFamily = FontFamily(
    Font(googleFont = roboto, fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = roboto, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = roboto, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = roboto, fontProvider = provider, weight = FontWeight.Bold)
)

@Composable
fun GamifikasiUI(navController: NavController) {
    val context = LocalContext.current // Get the current context

    // Instantiate MissionViewModel using the modified factory that takes context
    val missionViewModel: MissionViewModel = viewModel(
        factory = MissionViewModelFactory(context.applicationContext) // Pass applicationContext
    )

    val missions by missionViewModel.missions.collectAsState()
    val isLoading by missionViewModel.isLoading.collectAsState()
    val errorMessage by missionViewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF67669))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 74.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        "Misi Harian",
                        fontFamily = robotoFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF181818)
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    "Ingin dapat poin tambahan? Yuk, selesaikan tantangan sehatmu hari ini!",
                    fontFamily = robotoFontFamily,
                    fontSize = 18.sp,
                    color = Color(0xFF263238),
                    modifier = Modifier
                        .width(225.dp),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xFFF5EFFB),
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    ),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // TaskStatusSelector() // <-- BARIS INI DIHAPUS/DIKOMENTARI

                    // Spacer kecil mungkin dibutuhkan di sini jika TaskStatusSelector sebelumnya memberi padding vertikal
                    Spacer(modifier = Modifier.height(20.dp)) // Tambahkan ini jika perlu spasi atas untuk list

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = 40.dp)
                                .padding(top = 10.dp), // Sedikit padding atas
                            textAlign = TextAlign.Center
                        )
                    }

                    if (!isLoading && errorMessage == null) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(bottom = 10.dp) // Top padding dihilangkan dari sini jika Spacer di atas sudah cukup
                        ) {
                            items(missions) { missionUiState -> // ganti nama variabel agar jelas
                                TaskList(
                                    taskName = missionUiState.title,
                                    isDone = missionUiState.isDone,
                                    navController = navController,
                                    missionId = missionUiState.id // Teruskan ID misi
                                )
                            }
                            if (missions.isEmpty() && !isLoading) {
                                item {
                                    Text(
                                        "Tidak ada misi untuk hari ini.",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(40.dp),
                                        textAlign = TextAlign.Center,
                                        fontFamily = robotoFontFamily,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = { navController.navigate("reward") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 28.dp, end = 40.dp)
                        .width(180.dp)
                        .height(42.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF67669),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Klaim Reward",
                        fontSize = 13.sp,
                        fontFamily = robotoFontFamily
                    )
                }
            }
        }
    }
}

// Definisi TaskList tetap sama
@Composable
fun TaskList(taskName: String, isDone: Boolean, navController: NavController, missionId: String) {
    // ... implementasi TaskList Anda
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .padding(top = 30.dp)
            .clickable {
                navController.navigate("detailMisi/$missionId")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = taskName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = robotoFontFamily,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .weight(1f)
                    .background(
                        color = if (isDone) Color(0xFFF5B304) else Color.LightGray,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .border(
                        width = if (!isDone) 0.5.dp else 0.dp,
                        color = if (!isDone) Color.Gray else Color.Transparent,
                        shape = RoundedCornerShape(100.dp)
                    )
            )

            if (isDone) {
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_task_checked),
                    contentDescription = "Task Completed",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
        Box(
            modifier = Modifier
                .width(350.dp)
                .height(0.5.dp)
                .background(Color.Gray)
        )
    }
}


@Preview
@Composable
fun GamifikasiUIPreview() {
    GamifikasiUI(navController = rememberNavController())
}