package com.example.uijp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PremiumPriceScreen(navController: NavController) {
    Column(
    modifier = Modifier
    .fillMaxSize()
    .padding(24.dp),
    verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Back button
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(modifier = Modifier.height(56.dp))

            // Title
            Text(
                text = "Langganan Tahunan",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Price
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Rp ",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "189.000",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "/tahun",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light
                    ),
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }

            // Diskon text
            Text(
                text = "(hemat 20% dibanding bulanan)",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column (modifier = Modifier.padding(top= 48.dp,start = 16.dp, end = 16.dp)) {
                Text(
                    text = "Apa yang Kamu Dapatkan?",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // List Benefits
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BenefitItem(text = "Akses Misi Harian Premium")
                    BenefitItem(text = "Tukar Poin dengan Reward Eksklusif")
                    BenefitItem(text = "Konsultasi Gratis 1x/3 Bulan")
                }
            }

        }

        // Button Lanjutkan Pembayaran
        Button(
            onClick = { navController.navigate("paymentMethod") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Lanjutkan Pembayaran",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BenefitItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.ic_check_green), // <-- gambar icon checklist hijau
            contentDescription = null,
            tint = Color.Unspecified, // supaya warnanya asli dari drawable
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
        )
    }
}


