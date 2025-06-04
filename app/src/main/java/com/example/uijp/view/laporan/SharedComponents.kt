package com.example.uijp.view.laporan

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BulletPoint(text: String) {
    Row(modifier = Modifier.padding(start = 16.dp, top = 4.dp)) {
        Text(
            text = "\u2022",
            fontSize = 18.sp,
            modifier = Modifier.padding(end = 8.dp),
            color = Color.Black
        )
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        color = Color.Black,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun SuggestionText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
    )
}
