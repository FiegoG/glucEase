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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import  com.example.uijp.R

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
                Modifier.fillMaxWidth()
                    .height(28.dp)
                    .padding(horizontal = 24.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Misi Harian",
                        fontFamily = robotoFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF181818),
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
                Column {
                    TaskStatusSelector()
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp, top = 60.dp)
                ) {
                    item {
                        TaskList(
                            taskName = "Cek gula darah & input ke tracker",
                            isDone = false,
                            navController = navController
                        )
                    }
                    item {
                        TaskList(
                            taskName = "Konsumsi gula < 35g hari ini",
                            isDone = true,
                            navController = navController
                        )
                    }
                    item {
                        TaskList(
                            taskName = "Tambahkan 1 makanan ke tracker hari ini",
                            isDone = false,
                            navController = navController
                        )
                    }
                    item {
                        TaskList(
                            taskName = "Jalan kaki minimal 3000 langkah",
                            isDone = true,
                            navController = navController
                        )
                    }
                    item {
                        TaskList(
                            taskName = "Baca 1 artikel kesehatan di GlucEase",
                            isDone = false,
                            navController = navController
                        )
                    }
                }

                Button(
                    onClick = {navController.navigate("reward")},
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

@Composable
fun TaskList(taskName: String, isDone: Boolean, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .padding(top = 30.dp)
            .clickable {
                // Navigate to DetailMisi screen
                navController.navigate("detailMisi")
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
                        color = if (isDone) Color(0xFFF5B304) else Color.Transparent,
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

@Composable
fun TaskStatusSelector() {
    var selected by remember { mutableStateOf("Sedang Dikerjakan") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("Sedang Dikerjakan", "Belum Dikerjakan").forEach { label ->
            val isSelected = selected == label
            Column(
                modifier = Modifier
                    .width(158.dp)
                    .clickable { selected = label },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = label,
                    color = if (isSelected) Color.Black else Color(0xFF959595),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(15.dp))

                if (isSelected) {
                    Spacer(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(Color.Black)
                    )
                } else {
                    Spacer(modifier = Modifier.height(6.dp)) // keep spacing consistent
                }
            }
        }
    }
}

@Preview
@Composable
fun GamifikasiUIPreview() {
    GamifikasiUI(navController = rememberNavController())
}