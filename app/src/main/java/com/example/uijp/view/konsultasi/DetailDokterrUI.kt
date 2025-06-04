package com.example.uijp.view.konsultasi

import android.widget.Space
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import com.example.uijp.R

@Composable
fun DetailDokterUI(
    navController : NavController,
    modifier: Modifier = Modifier
) {
    val tanggalList = listOf("Sen" to "22", "Sel" to "23", "Rab" to "24", "Kam" to "25", "Jum" to "26", "Sab" to "27")
    val waktuList = listOf("1:00 pm", "3:00 pm", "5:00 pm", "7:00 pm")
    var selectedTanggal by remember { mutableStateOf("24") }
    var selectedWaktu by remember { mutableStateOf("5:00 pm") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFE8EDF4))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            Text("Konsultasi Kesehatan", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))
        }

            //FotoDokter
            Image(
                painter = painterResource(id = R.drawable.dokterpilih),
                contentDescription = "Foto Dokter",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("dr. Andi Pratama, Sp.PD", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Sp.PD – Konsentrasi Diabetes", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107))
                Text("5.0", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Biografi
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text("Biografi Singkat", fontWeight = FontWeight.Bold)
                Text(
                    "dr. Andi Pratama adalah spesialis penyakit dalam dengan fokus pada penanganan diabetes tipe 1 & 2. " +
                            "Berpengalaman lebih dari 12 tahun, beliau telah menangani ratusan pasien dengan pendekatan yang holistik dan edukatif.",
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
                Text("Rp150.000 / sesi (30 menit)", fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Jadwal Konsultasi
            Text("Jadwal Konsultasi:", fontWeight = FontWeight.Bold)
            LazyRow {
                items(tanggalList) { (hari, tanggal) ->
                    val isSelected = tanggal == selectedTanggal
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFFFF6B6B) else Color.Transparent)
                            .clickable { selectedTanggal = tanggal }
                            .padding(12.dp)
                    ) {
                        Text(hari, color = if (isSelected) Color.White else Color.Black)
                        Text(tanggal, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.Black)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            //Waktu Konsultasi
            Text("Jam Tersedia:", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                waktuList.forEach { time ->
                    val isSelected = time == selectedWaktu
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFFFF6B6B) else Color.White)
                            .clickable { selectedWaktu = time }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(time, color = if (isSelected) Color.White else Color.Black)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {navController.navigate("pilihWaktu")},
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF6B6B)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Lanjutkan", color = Color.White, fontWeight = FontWeight.Bold)
            }

    }
}

