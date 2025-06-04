package com.example.konsultasiprofil.UI.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import com.example.uijp.R

@Composable
fun PembayaranUI(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text("Ringkasan Pembayaran", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Doctor Info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.dokterpilih),
                contentDescription = "Doctor Photo",
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("dr. Andi Pratama, Sp.PD", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Sp.PD - Konsentrasi Diabetes", fontSize = 14.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Summary
        Divider()
        SummaryItem(label = "Biaya Sesi 30 Menit", value = "Rp150.000")
        SummaryItem(label = "Biaya Layanan", value = "Rp2.000")
        SummaryItem(label = "Kupon(DISKONDOKTER)", value = "Rp0")
        SummaryItem(label = "Pembayaranmu", value = "Rp152.000", isBold = true)
        Divider()

        Spacer(modifier = Modifier.height(16.dp))

        // Promo Banner
        Card(
            backgroundColor = Color(0xFFFFF1E6),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFEF6C00))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pakai Kupon & Makin Hemat!", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Methods
        Text("Pilih Metode Pembayaran", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        PaymentMethodItem("GOPAY", R.drawable.gopay_logo)
        PaymentMethodItem("OVO", R.drawable.ovo_logo)
        PaymentMethodItem("SHOPEE PAY", R.drawable.shopee_logo)

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Total and Button
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Total", fontSize = 14.sp, color = Color.Gray)
                Text("Rp150.000", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Button(
                onClick = {navController.navigate("notifikasi")},
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF6D60)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Bayar", color = Color.White)
            }
        }
        Text(
            "Ini adalah langkah terakhir, setelah Anda menekan tombol Bayar, pembayaran akan menjadi transaksi.",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun SummaryItem(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(value, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun PaymentMethodItem(name: String, logoResId: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { /* Pilih metode */ },
        elevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = logoResId),
                contentDescription = "$name Logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(name)
        }
    }
}