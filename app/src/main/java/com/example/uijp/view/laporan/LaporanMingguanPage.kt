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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uijp.viewmodel.WeeklyReportViewModel
import com.example.uijp.viewmodel.LaporanMingguanViewModelFactory

@Composable
fun LaporanMingguanPage(navController: NavController) {
    val context = LocalContext.current
    val viewModel: WeeklyReportViewModel = viewModel(
        factory = LaporanMingguanViewModelFactory(context.applicationContext)
    )

    // State yang diamati sekarang adalah entity dari database
    val weeklyReportEntity by viewModel.weeklyReportData
    val missions by viewModel.missions
    val isLoading by viewModel.isLoading // Gunakan state isLoading tunggal
    val errorMessage by viewModel.errorMessage

//    LaunchedEffect(Unit) {
//        viewModel.fetchWeeklyReportAndMissions()
//    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(bottom = 64.dp) // adjust bottom padding for the navbar
        ) {
            TopBarSection(navController = navController, period = weeklyReportEntity?.reportInfo?.period ?: "Memuat...")

            if (isLoading) {
                // Tampilkan loading indicator saat sinkronisasi berjalan
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
            RingkasanGulaSection(sugarIntakeDetail = weeklyReportEntity?.sugarIntake)
            Spacer(modifier = Modifier.height(16.dp))
            RingkasanGulaDarahSection(bloodSugarDetail = weeklyReportEntity?.bloodSugar)
            Spacer(modifier = Modifier.height(16.dp))
//            AktivitasMisiSection(missions = missions)

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
