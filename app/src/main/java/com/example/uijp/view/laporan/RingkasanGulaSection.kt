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
fun RingkasanGulaSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Ringkasan Asupan Gula Harian",
            style = MaterialTheme.typography.titleMedium
        )

        BorderedTable {
            TableHeader("Hari", "Total Gula (G)", "Status")

            // Contoh data dummy
            TableRowGula("Senin", "30g", "Normal")
            TableRowGula("Selasa", "48g", "Mendekati")
            TableRowGula("Rabu", "60g", "Melebihi")
            TableRowGula("Kamis", "28g", "Normal")
            TableRowGula("Jumat", "35g", "Normal")
            TableRowGula("Sabtu", "55g", "Melebihi")
            TableRowGula("Minggu", "42g", "Mendekati")
        }

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

@Preview(showBackground = true)
@Composable
fun PreviewRingkasanGulaSection() {
    MaterialTheme {
        RingkasanGulaSection()
    }
}
