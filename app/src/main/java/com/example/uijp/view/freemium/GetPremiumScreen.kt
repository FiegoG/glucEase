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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.uijp.data.model.ApiPremiumPackage
import com.example.uijp.viewmodel.PremiumUiState
import com.example.uijp.viewmodel.PremiumViewModel

@Composable
fun GetPremiumScreen(navController: NavController, viewModel: PremiumViewModel) {
    // var selectedOption by remember { mutableStateOf("Bulanan") } // Akan dikelola oleh state paket dari ViewModel
    // State lokal untuk tracking pilihan UI sementara, sebelum dikonfirmasi ke ViewModel
    var uiSelectedPackageTitle by remember { mutableStateOf<String?>(null) }

    val packagesState by viewModel.premiumPackagesState.collectAsState()


    // LaunchedEffect untuk memilih paket pertama sebagai default jika ada
    LaunchedEffect(packagesState) {
        if (packagesState is PremiumUiState.Success) {
            val packages = (packagesState as PremiumUiState.Success<List<ApiPremiumPackage>>).data
            if (packages.isNotEmpty() && uiSelectedPackageTitle == null) {
                // Pilih paket pertama secara default untuk UI, misal "Bulanan" jika itu yang pertama
                // Anda mungkin ingin logika yang lebih canggih di sini
                // atau biarkan user yang memilih pertama kali
                val defaultSelection = packages.firstOrNull { it.packageName?.contains("Bulanan", ignoreCase = true) == true }
                    ?: packages.firstOrNull()
                defaultSelection?.let {
                    uiSelectedPackageTitle = it.packageName // atau field lain yang cocok dengan title di SubscriptionOption
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // ... (HEADER)

        //text and vector
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 8.dp, end = 8.dp)
        ) {
            // ... (Teks dan Image GlucEase Premium)
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
                when (val state = packagesState) {
                    is PremiumUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is PremiumUiState.Success -> {
                        val packages = state.data
                        // Asumsi ada 2 paket utama: Bulanan dan Tahunan dari API
                        // Anda mungkin perlu menyesuaikan cara mencari paket "Bulanan" dan "Tahunan"
                        val monthlyPackage = packages.find { it.packageName?.contains("Bulanan", ignoreCase = true) == true }
                        val yearlyPackage = packages.find { it.packageName?.contains("Tahunan", ignoreCase = true) == true }

                        monthlyPackage?.let { pkg ->
                            SubscriptionOption(
                                title = pkg.packageName ?: "Bulanan", // Tampilkan nama dari API
                                price = "Rp${pkg.price ?: "N/A"}/${if (pkg.durationMonths == 1) "Bulan" else "${pkg.durationMonths} Bulan"}",
                                selected = uiSelectedPackageTitle == pkg.packageName,
                                onClick = { uiSelectedPackageTitle = pkg.packageName }
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        yearlyPackage?.let { pkg ->
                            SubscriptionOption(
                                title = pkg.packageName ?: "Tahunan", // Tampilkan nama dari API
                                price = "Rp${pkg.price ?: "N/A"}/${if (pkg.durationMonths == 12) "tahun" else "${pkg.durationMonths} Bulan"} (hemat 20%)", // Contoh format
                                selected = uiSelectedPackageTitle == pkg.packageName,
                                onClick = { uiSelectedPackageTitle = pkg.packageName },
                                badge = if (pkg.packageName?.contains("Tahunan", ignoreCase = true) == true) "Best Value" else null
                            )
                        }

                        if (packages.isEmpty()) {
                            Text("Tidak ada paket tersedia saat ini.", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        }
                    }
                    is PremiumUiState.Error -> {
                        Text("Error: ${state.message}", color = Color.Red, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                    is PremiumUiState.Idle -> {
                        // Initial state, loading will likely be triggered soon
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (packagesState is PremiumUiState.Success) {
                            val selectedApiPackage = (packagesState as PremiumUiState.Success<List<ApiPremiumPackage>>).data
                                .find { it.packageName == uiSelectedPackageTitle }
                            selectedApiPackage?.let {
                                viewModel.selectPackage(it) // Set di ViewModel
                                navController.navigate("premiumPrice/${it.id}") // Kirim ID paket
                            }
                        }
                    },
                    enabled = uiSelectedPackageTitle != null && packagesState is PremiumUiState.Success && (packagesState as PremiumUiState.Success<List<ApiPremiumPackage>>).data.any{it.packageName == uiSelectedPackageTitle},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Langganan Sekarang", color = Color.White)
                }
                // ... (Syarat & Ketentuan Text)
                Spacer(modifier = Modifier.height(16.dp))

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