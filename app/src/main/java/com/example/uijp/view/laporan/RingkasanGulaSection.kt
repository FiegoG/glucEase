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
fun RingkasanGulaSection(sugarIntakeDetail: HealthReportDetail?) { // Terima HealthReportDetail
    val dailyData = sugarIntakeDetail?.dailyData ?: emptyList()
    val aiAnalysis = sugarIntakeDetail?.aiAnalysis

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Ringkasan Asupan Gula Harian",
            style = MaterialTheme.typography.titleMedium
        )

        // Gunakan data dinamis untuk tabel
        // TableGula dari kode Anda sebelumnya sudah menerima List<GulaHarian>
        // Kita perlu memetakan DailyDataPoint ke GulaHarian atau memodifikasi TableGula
        // Untuk simple, kita buat list GulaHarian dari DailyDataPoint
        val gulaHarianList = dailyData.map {
            GulaHarian(
                hari = it.day ?: "-",
                totalGula = "${it.totalSugar?.toInt() ?: 0}G", // Sesuaikan format jika perlu
                status = it.status ?: "N/A"
            )
        }
        // Jika gulaHarianList kosong, TableGula akan menampilkan header saja
        TableGula(data = gulaHarianList)


        if (aiAnalysis != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(aiAnalysis.kesimpulan ?: "Tidak ada kesimpulan AI.", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))

            if (!aiAnalysis.saran.isNullOrEmpty()) {
                Text("Disarankan untuk:", fontWeight = FontWeight.Bold)
                aiAnalysis.saran
            }
            aiAnalysis.peringatan?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Peringatan:", fontWeight = FontWeight.Bold, color = Color.Red)
                Text(it, color = Color.Red)
            }
        } else if (dailyData.isNotEmpty()) { // Ada data harian tapi tidak ada AI
            Spacer(modifier = Modifier.height(8.dp))
            Text("Analisis AI tidak tersedia untuk periode ini.")
        }
        // Jika dailyData kosong dan aiAnalysis null, tidak ada apa-apa di bawah tabel (sesuai)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewRingkasanGulaSection() {
//    MaterialTheme {
//        RingkasanGulaSection()
//    }
//}
