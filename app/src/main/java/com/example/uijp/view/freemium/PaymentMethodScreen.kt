package com.example.uijp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.CircularProgressIndicator // Untuk loading
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.uijp.viewmodel.PremiumViewModel
import com.example.uijp.viewmodel.PremiumUiState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PaymentMethodScreen(navController: NavController, viewModel: PremiumViewModel) {
    var selectedMethod by remember { mutableStateOf("") }
    val subscriptionState by viewModel.subscriptionResultState.collectAsState()
    val context = LocalContext.current

    // Handle subscription result
    LaunchedEffect(subscriptionState) {
        when (val state = subscriptionState) {
            is PremiumUiState.Success -> {
                Toast.makeText(context, state.data.message ?: "Langganan berhasil!", Toast.LENGTH_LONG).show()
                navController.navigate("paymentSuccess") {
                    // Membersihkan backstack sampai home atau tujuan lain yang sesuai
                    popUpTo(navController.graph.startDestinationId) { inclusive = false }
                    launchSingleTop = true
                }
                viewModel.resetSubscriptionResultState() // Reset state setelah sukses
            }
            is PremiumUiState.Error -> {
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                viewModel.resetSubscriptionResultState() // Reset state setelah error
            }
            else -> { /* Idle or Loading */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Back Button
            IconButton(
                onClick = { if (subscriptionState !is PremiumUiState.Loading) navController.popBackStack() },
                enabled = subscriptionState !is PremiumUiState.Loading,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            // ... (Sisa UI PaymentMethodScreen seperti Title, PaymentOptions)
            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Pilih Metode Pembayaran",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Payment Options
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                PaymentOption(
                    name = "SHOPEE PAY",
                    logoRes = R.drawable.logo_shopee,
                    isSelected = selectedMethod == "SHOPEE PAY",
                    onClick = { if (subscriptionState !is PremiumUiState.Loading) selectedMethod = "SHOPEE PAY" }
                )
                PaymentOption(
                    name = "OVO",
                    logoRes = R.drawable.logo_ovo,
                    isSelected = selectedMethod == "OVO",
                    onClick = { if (subscriptionState !is PremiumUiState.Loading) selectedMethod = "OVO" }
                )
                PaymentOption(
                    name = "Gopay",
                    logoRes = R.drawable.logo_gopay,
                    isSelected = selectedMethod == "Gopay",
                    onClick = { if (subscriptionState !is PremiumUiState.Loading) selectedMethod = "Gopay" }
                )
            }
        }

        if (subscriptionState is PremiumUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        Button(
            onClick = {
                if (selectedMethod.isNotEmpty()) {
                    viewModel.subscribeToSelectedPackage(selectedMethod)
                } else {
                    Toast.makeText(context, "Pilih metode pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = selectedMethod.isNotEmpty() && subscriptionState !is PremiumUiState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B6B),
                disabledContainerColor = Color(0xFFFFC0C0) // Warna saat disabled
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (subscriptionState is PremiumUiState.Loading) {
                Text(text = "Memproses...", color = Color.White)
            } else {
                Text(
                    text = "Bayar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun PaymentOption(
    name: String,
    logoRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F8F8), shape = RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = if (isSelected) Color(0xFFFF6B6B) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next",
                tint = Color(0xFFFF6B6B)
            )
        }
    }
}