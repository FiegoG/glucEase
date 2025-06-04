package com.example.uijp.view.laporan

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RingkasanGulaDarahSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Ringkasan Gula Darah",
            style = MaterialTheme.typography.titleMedium
        )

        TableGula(
            listOf(
                GulaHarian("SENIN", "28G", "NORMAL"),
                GulaHarian("SELASA", "37G", "MENDEKATI"),
                GulaHarian("RABU", "42G", "MELEBIHI")
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Saran dari AI :", fontWeight = FontWeight.Bold)
        Text("Gula darah cenderung naik saat sore hingga malam hari, terutama di hari libur.")

        Spacer(modifier = Modifier.height(4.dp))

        Text("Disarankan untuk:", fontWeight = FontWeight.Bold)
        BulletPoint("Lakukan pengecekan secara rutin pada pagi dan malam")
        BulletPoint("Hindari konsumsi karbohidrat sederhana di malam hari")
        BulletPoint("Pertimbangkan aktivitas fisik ringan setelah makan malam")
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

@Preview(showBackground = true)
@Composable
fun PreviewRingkasanGulaDarahSection() {
    MaterialTheme {
        RingkasanGulaDarahSection()
    }
}

