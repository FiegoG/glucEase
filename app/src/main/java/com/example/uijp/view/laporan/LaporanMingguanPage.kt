package com.example.uijp.view.laporan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uijp.viewmodel.LaporanMingguanViewModel
import com.example.uijp.viewmodel.LaporanMingguanViewModelFactory

@Composable
fun LaporanMingguanPage(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LaporanMingguanViewModel = viewModel(
        factory = LaporanMingguanViewModelFactory(context.applicationContext)
    )

    val weeklyReportData by viewModel.weeklyReportData
    val missions by viewModel.missions
    val isLoadingReport by viewModel.isLoadingReport
    val isLoadingMissions by viewModel.isLoadingMissions
    val errorMessage by viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.fetchWeeklyReportAndMissions()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(bottom = 64.dp) // adjust bottom padding for the navbar
        ) {
            TopBarSection(navController = navController, period = weeklyReportData?.reportInfo?.period ?: "Memuat...")

            if (isLoadingReport || isLoadingMissions) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            errorMessage?.let {
                Text(
                    text = it,
                    color = androidx.compose.ui.graphics.Color.Red,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            // Hanya tampilkan section jika tidak loading DAN tidak ada error (atau error sudah ditampilkan)
            // atau jika data adalah objek kosong (bukan null)
            if (!isLoadingReport) {
                RingkasanGulaSection(sugarIntakeDetail = weeklyReportData?.sugarIntake)
                Spacer(modifier = Modifier.height(16.dp))
                RingkasanGulaDarahSection(bloodSugarDetail = weeklyReportData?.bloodSugar)
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (!isLoadingMissions) {
                AktivitasMisiSection() // Kirim data misi
            }

            Spacer(modifier = Modifier.weight(1f)) // Push content upwards
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewLaporanMingguanPage() {
//    LaporanMingguanPage(navController = rememberNavController())
//}



//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(bottom = 64.dp)
//    ) {
//        TopBarSection()
//        Spacer(modifier = Modifier.height(16.dp))
//        RingkasanGulaSection()
//        Spacer(modifier = Modifier.height(16.dp))
//        RingkasanGulaDarahSection()
//        Spacer(modifier = Modifier.height(16.dp))
//        AktivitasMisiSection()
//        Spacer(modifier = Modifier.weight(1f))
//        BottomNavigationBar()
//    }
