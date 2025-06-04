package com.example.uijp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.uijp.R


@Composable
fun ResetPasswordScreen(navController: NavController) {
    var passwordBaru by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    //Membuat kolom sebagai halaman utama untuk isi konten ui login
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

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

        //Teks "Atur Ulang Kata Sandi"
        Text(
            text = "Atur Ulang Kata Sandi",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            color = Color(0xFFFF6F61),
            modifier = Modifier
                .fillMaxWidth() // Memastikan teks memenuhi lebar maksimal
        )

        Text(
            text = "Silahkan ketik sesuatu yang mudah diingat",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth() // Memastikan teks memenuhi lebar maksimal
                .padding(top = 8.dp)
        )

        Text(
            text = "Kata Sandi Baru",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth() // Memastikan teks memenuhi lebar maksimal
                .padding(top = 40.dp)
        )

        OutlinedTextField(
            value = passwordBaru,
            onValueChange = { passwordBaru = it },
            label = { Text("minimal 8 karakter", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray,  // Background saat fokus
                unfocusedContainerColor = Color.White // Background saat tidak fokus
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Konfirmasi Kata Sandi Baru",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth() // Memastikan teks memenuhi lebar maksimal
                .padding(top = 16.dp)
        )

        OutlinedTextField(
            value = passwordBaru,
            onValueChange = { passwordBaru = it },
            label = { Text("ulangi kata sandi", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray,  // Background saat fokus
                unfocusedContainerColor = Color.White // Background saat tidak fokus
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        )

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = { navController.navigate("login") },  // Navigasi ke halaman login
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF67669)),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Atur Ulang Kata Sandi",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewResetPassword() {
    val navController = rememberNavController() // Dummy NavController
    ResetPasswordScreen(navController)
}