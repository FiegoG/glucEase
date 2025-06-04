package com.example.uijp.gamifikasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uijp.R

@Composable
fun DetailMisi(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF67669))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 75.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Detail Misi",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF181818),
                        modifier = Modifier
                            .padding(end = 28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(78.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xFFFCFCFC),
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 100.dp)
                ) {
                    InsideDetailMisi(
                        taskName = "Selesaikan Latihan Harian",
                        taskDescription = "Kerjakan latihan soal minimal 5 nomor untuk menyelesaikan misi ini.",
                        taskTime = "Tersedia hingga pukul 23.59 hari ini"
                    )
                }

                Button(
                    onClick = {navController.navigate("gamifikasi")},
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 70.dp)
                        .width(180.dp)
                        .height(42.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF67669),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Selesaikan Misi",
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
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = taskName,
                fontSize = 22.sp,
                color = Color(0xFF120D26)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_desc),
                contentDescription = "Deskripsi Icon",
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Deskripsi",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = taskDescription,
            fontSize = 13.sp,
            fontWeight = FontWeight.Light,
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
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = taskTime,
            fontSize = 13.sp,
            fontWeight = FontWeight.Light,
            color = Color(0xFF494950)
        )
    }
}
