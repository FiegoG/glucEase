package com.example.uijp.view.laporan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Misi(val hari: String, val jumlah: Int, val poin: Int)

@Composable
fun AktivitasMisiSection() {
    Column(modifier = Modifier.padding(16.dp) ) {
        Text("Aktivitas Misi Mingguan", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))
        TableMisi(
            listOf(
                Misi("SENIN", 3, 150),
                Misi("RABU", 2, 100),
                Misi("JUMAT", 3, 150),
                Misi("TOTAL", 8, 400)
            )
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun TableMisi(listOf: List<Misi>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        BorderedTable {
            TableHeader("Hari", "JUMLAH MISI DISELESAIKAN", "Status")

            // Contoh data dummy
            TableRowMisi("Senin", "3 Misi", "+150")
            TableRowMisi("Selasa", "2 Misi", "+100")
            TableRowMisi("Rabu", "3 Misi", "+150")
            TableRowMisi("Total", "8 Misi", "+400")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAMS() {
    AktivitasMisiSection()
}
