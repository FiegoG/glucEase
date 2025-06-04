package com.example.konsultasiprofil.UI.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.navigation.NavController
import androidx.compose.material.OutlinedTextField
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uijp.R

@Composable
fun DokterCard(name: String, spesialis: String, rating: Double, navController: NavController, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFFFFEEEE)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(id = R.drawable.dokterpilih),
                contentDescription = "Dokter Image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name, fontWeight = FontWeight.Bold)
                Text(text = spesialis, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {navController.navigate("detailDokter")},
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF97D7D))
                ) {
                    Text("Mulai Konsultasi", color = Color.White, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107))
                Text(text = rating.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun KonsultasiUi(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE7ECF4))
            .padding(16.dp)
    ) {
        //TopBar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.navigate("home") }
            )
            Text("Konsultasi Kesehatan", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Icon(Icons.Default.MoreVert, contentDescription = "More")
        }

        //Search Menu
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = { Icon(Icons.Default.ThumbUp, contentDescription = "Search") },
            placeholder = { Text("Search") },
            label = { Text("Cari Dokter") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
            )
        )

        //Banner
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF97D7D))
                .padding(16.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Temukan dokter terpercaya untuk bantu kamu jaga gula darah & gaya hidup sehat.",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.doktermenu),
                    contentDescription = "Banner",
                    modifier = Modifier
                        .size(80.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Semua Dokter",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )

        //ListDokter
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(4) {
                DokterCard(
                    name = "dr. Andi Pratama, Sp.PD",
                    spesialis = "Spesialis Penyakit Dalam - Kontrasepsi Diabetes",
                    rating = 5.0,
                    navController = navController
                )
            }
        }
    }
}
