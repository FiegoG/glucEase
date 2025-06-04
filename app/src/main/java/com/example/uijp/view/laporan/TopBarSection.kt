package com.example.uijp.view.laporan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.media3.exoplayer.offline.Download
import com.example.uijp.R

@Composable
fun TopBarSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Laporan Mingguan",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(), // Membuat Row mengisi lebar maksimum
        ) {
            Spacer(modifier = Modifier.weight(1f)) // Menambahkan spacer agar tanggal dan ikon didorong ke kanan
            Icon(painter = painterResource(id = R.drawable.icon_clock), contentDescription = "Tanggal Laporan", tint = Color.Unspecified)
            //Icon(Icons.Filled.DateRange, contentDescription = "Tanggal Laporan")
            Text(text = "18 - 24 Maret 2025", textAlign = TextAlign.End)
            Spacer(modifier = Modifier.padding(4.dp))

        }
    }
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//            .padding(horizontal = 16.dp, vertical = 12.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column {
//            Text(
//                text = "Laporan Mingguan",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black
//            )
//            Text(
//                text = "8 - 14 April 2025",
//                fontSize = 14.sp,
//                color = Color.Gray
//            )
//        }
//
//    }
}

