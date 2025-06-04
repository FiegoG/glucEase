package com.example.uijp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uijp.R

@Composable
fun VerificationScreen(navController: NavController) {
    var otpCode by remember { mutableStateOf(listOf("", "", "", "")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
                    .clickable { navController.navigate("lupaPassword") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Kembali ke lupaPassword",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Header Text
        Text(
            text = "Silakan periksa email Anda.",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE57373), // Warna merah
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Kami telah mengirimkan kode ke test@gmail.com",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // OTP Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            otpCode.forEachIndexed { index, _ ->
                OutlinedTextField(
                    value = otpCode[index],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1) {
                            val newOtp = otpCode.toMutableList()
                            newOtp[index] = newValue
                            otpCode = newOtp
                        }
                    },
                    modifier = Modifier.size(64.dp),
                    textStyle = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors( // ✅ Ganti outlinedTextFieldColors() dengan colors()
                        focusedIndicatorColor = Color(0xFFE57373), // Warna merah saat fokus
                        unfocusedIndicatorColor = Color.LightGray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Verifikasi
        Button(
            onClick = { navController.navigate("resetPassword") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Verifikasi", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kirim ulang kode (belum diatur logikanya)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Kirim ulang kode",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "00:20",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVerifikasi() {
    val navController = rememberNavController()
    VerificationScreen(navController)
}