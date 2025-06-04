package com.example.uijp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.painterResource
import com.example.uijp.R


@Composable
fun LupaPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }

    //Membuat kolom sebagai halaman utama untuk isi konten ui login
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // Tombol Back (Kiri Atas)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable { navController.navigate("login")},
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Kembali ke login",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        //Teks "Lupa Password?"
        Text(
            text = "Lupa password?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            color = Color(0xFFFF6F61),
            modifier = Modifier
                .fillMaxWidth() // Memastikan teks memenuhi lebar maksimal
        )

        Text(
            text = "Jangan khawatir! Hal seperti ini bisa terjadi.\nSilahkan masukkan email yang terkait dengan akun Anda",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth() // Memastikan teks memenuhi lebar maksimal
                .padding(top = 8.dp)
        )

        Text(
            text = "Email",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth() // Memastikan teks memenuhi lebar maksimal
                .padding(top = 40.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Masukkan Alamat Email Anda", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray,  // Background saat fokus
                unfocusedContainerColor = Color.White // Background saat tidak fokus
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("verifikasi") },  // Navigasi ke halaman Home
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF67669)),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Kirim Kode",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(250.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Ingat Password?",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(end = 8.dp) // Tambahkan jarak ke kanan
            )

            Text(
                text = "Login",
                color = Color(0xFFFF6F61),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate("login") }
            )
        }
    }
}

