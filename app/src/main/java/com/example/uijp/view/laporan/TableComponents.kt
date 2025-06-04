package com.example.uijp.view.laporan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Wrapper untuk seluruh tabel agar ada border luar
@Composable
fun BorderedTable(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 0.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)) // sudut tabel
            .background(Color.White) // latar dalam tabel
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0), // warna abu abu netral
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(modifier = Modifier.padding(bottom = 4.dp)) {
            content()
        }
    }
}


@Composable
fun TableHeader(vararg titles: String, backgroundColor: Color = Color(0xFFFF9E9E)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 12.dp)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = titles[0].uppercase(), // Elemen pertama
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF3A3A3A),
            modifier = Modifier.weight(0.5f) // Lebar lebih kecil untuk kolom pertama
        )
        Text(
            text = titles[1].uppercase(), // Elemen kedua
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            textAlign = TextAlign.Center, // Posisikan teks di tengah
            color = Color(0xFF3A3A3A),
            modifier = Modifier.weight(1f) // Lebar fleksibel untuk kolom kedua
        )
        Text(
            text = titles[2].uppercase(), // Elemen ketiga
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF3A3A3A),
            modifier = Modifier.weight(1f) // Lebar fleksibel untuk kolom ketiga
        )
//        titles.forEach { title ->
//            Text(
//                text = title.uppercase(),
//                fontWeight = FontWeight.Bold,
//                fontSize = 13.sp,
//                color = Color(0xFF3A3A3A),
//                modifier = Modifier.weight(1f)
//            )
//        }
    }
    Divider(color = Color.LightGray, thickness = 1.dp)
}

@Composable
fun TableRowGula(hari: String, totalGula: String, status: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = hari, modifier = Modifier.weight(0.5f), textAlign = TextAlign.Center, fontSize = 10.sp)
            Text(text = totalGula, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 10.sp)
            StatusChip(status = status, modifier = Modifier.weight(1f))
        }
        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
    }
}

@Composable
fun TableRowMisi(hari: String, jumlahMisi: String, poin: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = hari, modifier = Modifier.weight(0.5f), textAlign = TextAlign.Center, fontSize = 10.sp)
            Text(text = jumlahMisi, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 10.sp)
            Text(
                text = poin,
                color = Color(0xFF34A853),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                modifier = Modifier.weight(1f)
            )
        }
        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
    }
}

@Composable
fun StatusChip(status: String, modifier: Modifier = Modifier) {
    val (backgroundColor, textColor) = when (status.uppercase()) {
        "NORMAL" -> Color(0xFFB5F8D0) to Color(0xFF007F3D)
        "MENDEKATI" -> Color(0xFFFFECB3) to Color(0xFF7A6200)
        "MELEBIHI" -> Color(0xFFFFCDD2) to Color(0xFFB00020)
        else -> Color.LightGray to Color.DarkGray
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}


