package com.example.uijp.view.laporan

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uijp.data.model.HealthReportDetail

@Composable
fun RingkasanGulaDarahSection(bloodSugarDetail: HealthReportDetail?) { // Terima HealthReportDetail
    val dailyData = bloodSugarDetail?.dailyData ?: emptyList()
    val aiAnalysis = bloodSugarDetail?.aiAnalysis

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Ringkasan Gula Darah",
            style = MaterialTheme.typography.titleMedium
        )

        val gulaHarianList = dailyData.map {
            GulaHarian(
                hari = it.day ?: "-",
                totalGula = "${it.avgBloodSugar?.toInt() ?: 0}", // Unit sudah ada di header tabel (G), atau mg/dL
                status = it.status ?: "N/A"
            )
        }
        // Jika gulaHarianList kosong, TableGula akan menampilkan header saja
        // Anda mungkin perlu TableHeader yang berbeda untuk Gula Darah (misal "Rata-rata Gula Darah (mg/dL)")
        // Untuk saat ini menggunakan TableGula yang ada
        TableGula(data = gulaHarianList)


        if (aiAnalysis != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(aiAnalysis.kesimpulan ?: "Tidak ada kesimpulan AI.", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))

            if (!aiAnalysis.saran.isNullOrEmpty()) {
                Text("Disarankan untuk:", fontWeight = FontWeight.Bold)
                aiAnalysis.saran.forEach { BulletPoint(it) }
            }
            aiAnalysis.peringatan?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Peringatan:", fontWeight = FontWeight.Bold, color = Color.Red)
                Text(it, color = Color.Red)
            }
        } else if (dailyData.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Analisis AI tidak tersedia untuk periode ini.")
        }
    }
}

@Composable
fun TableGula(data: List<GulaHarian>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp), // Padding ditangani oleh parent Column
        colors = CardDefaults.cardColors(containerColor = Color.Transparent), // latar transparan
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        BorderedTable {
            TableHeader("Hari", "Total Gula (G)", "Status")
            data.forEach {
                TableRowGula(hari = it.hari, totalGula = it.totalGula, status = it.status)
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewRingkasanGulaDarahSection() {
//    MaterialTheme {
//        RingkasanGulaDarahSection()
//    }
//}

