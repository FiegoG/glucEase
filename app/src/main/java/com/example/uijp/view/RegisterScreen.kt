package com.example.uijp.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun RegisterScreen(navController: NavController) {
    //inisialisasi variabel untuk menyimpan input pengguna
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    //Membuat kolom sebagai halaman utama dengan tata letak vertikal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF67669)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        //Membuat bagian kartu putih tempat form registrasi berada
        Card(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Selamat Datang di GlucEase!",
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth() // Memastikan teks memenuhi lebar maksimal
                        .padding(horizontal = 16.dp, vertical = 4.dp) //dari kolom luar yang dibuat membuat batas lagi agar tidak mepet padding
                )
                Text(
                    "Pantau gula darah & gaya hidup sehatmu dengan lebih mudah.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth() // Memastikan teks memenuhi lebar maksimal
                        .padding(horizontal = 20.dp, vertical = 8.dp) //dari kolom luar yang dibuat membuat batas lagi agar tidak mepet padding
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Input Nama
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,  // Background saat fokus
                        unfocusedContainerColor = Color.White // Background saat tidak fokus
                    )
                )

                //Input Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,  // Background saat fokus
                        unfocusedContainerColor = Color.White // Background saat tidak fokus
                    )
                )

                //Input Provinsi
                OutlinedTextField(
                    value = province,
                    onValueChange = { province = it },
                    label = { Text("Provinsi") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,  // Background saat fokus
                        unfocusedContainerColor = Color.White // Background saat tidak fokus
                    )
                )

                //Input Kota
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Kota") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,  // Background saat fokus
                        unfocusedContainerColor = Color.White // Background saat tidak fokus
                    )
                )

                //Input Password dengan toggle untuk melihat/mengubah visibilitas
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,  // Background saat fokus
                        unfocusedContainerColor = Color.White // Background saat tidak fokus
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Tombol Daftar
                Button(
                    onClick = { navController.navigate("datadiri") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF67669))
                ) {
                    Text("DAFTAR", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                //Navigasi ke halaman login jika sudah punya akun
                Row {
                    Text("Sudah punya akun? ")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Login",
                        color = Color(0xFFFF6F61),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { navController.navigate("login") } // Navigasi ke halaman Register
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    val navController = rememberNavController() // Dummy NavController
    RegisterScreen(navController)
}