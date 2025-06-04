package com.example.uijp.view.freemium

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uijp.R

@Composable
fun GetPremiumScreen(navController: NavController) {
    var selectedOption by remember { mutableStateOf("Bulanan") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        //        HEADER
        Box(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
        ) {
            IconButton(modifier = Modifier.align(Alignment.CenterStart), onClick = {navController.popBackStack()}) {
                Icon(
                    Icons.Default.Close, contentDescription = "close"
                )
            }

        }

        //text and vector
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 8.dp, end = 8.dp)
        ) {
            Column {
                Text(
                    text = "GlucEase Premium",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        textAlign = TextAlign.Center, fontSize = 32.sp, fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Nikmati fitur eksklusif untuk bantu kamu lebih sehat dan teratur!\u2028",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                    )
                )

                Image(
                    modifier = Modifier.padding(top = 16.dp),
                    painter = painterResource(R.drawable.vector_premium_page),
                    contentDescription = null
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 8.dp, end = 8.dp)
        ) {
            Column {
                // Bulanan Card
                SubscriptionOption(
                    title = "Bulanan",
                    price = "Rp19.000/Bulan",
                    selected = selectedOption == "Bulanan",
                    onClick = { selectedOption = "Bulanan" })

                Spacer(modifier = Modifier.height(12.dp))
                // Tahunan Card
                SubscriptionOption(
                    title = "Tahunan",
                    price = "Rp189.000/tahun (hemat 20%)",
                    selected = selectedOption == "Tahunan",
                    onClick = { selectedOption = "Tahunan" },
                    badge = "Best Value"
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Button Langganan Sekarang
                Button(
                    onClick = {navController.navigate("premiumPrice")},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Langganan Sekarang", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Syarat & Ketentuan Text
                Text(
                    text = "Dengan melanjutkan pembayaran, kamu menyetujui\n" + "Syarat & Ketentuan serta Kebijakan Privasi GlucEase.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp, textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }

    }

}

@Composable
fun SubscriptionOption(
    title: String, price: String, selected: Boolean, onClick: () -> Unit, badge: String? = null
) {
    val borderColor = if (selected) Color(0xFFFF6B6B) else Color.Transparent
    val backgroundColor = if (selected) Color(0xFFFFEBEB) else Color(0xFFFFEBEB)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, shape = RoundedCornerShape(16.dp))
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title, style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold, fontSize = 18.sp
                    ), modifier = Modifier.weight(1f)
                )
                if (badge != null) {
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFF2ECC71), shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badge, style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White, fontSize = 10.sp
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = price, style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp
                )
            )
        }
    }
}