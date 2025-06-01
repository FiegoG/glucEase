package com.example.uijp.view.artikel

import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uijp.R
import com.example.uijp.data.model.Article
import com.example.uijp.viewmodel.ArticleViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ArtikelKategoriScreen(
    navController: NavController,
    kategoriName: String,
    viewModel: ArticleViewModel = viewModel()
) {
    val categoryUiState by viewModel.categoryUiState.collectAsState()

    // Load articles when screen is first displayed
    LaunchedEffect(kategoriName) {
        viewModel.loadArticlesByCategory(kategoriName)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Icon Kembali di sebelah kiri atas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = {
                navController.navigate("artikel") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Kembali",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Judul Halaman
        Text(
            text = kategoriName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Search Bar (belum aktif)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(25.dp),
            singleLine = true
        )

        // Loading State
        if (categoryUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Error State
        categoryUiState.error?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFFD32F2F)
                )
            }
        }

        // Daftar Artikel - Menggunakan LazyColumn untuk performa yang lebih baik
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categoryUiState.articles) { article ->
                ArtikelCard(
                    article = article,
                    onClick = {
                        navController.navigate("detail/${article.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun ArtikelCard(
    article: Article,
    onClick: () -> Unit
) {
    // Komponen kartu artikel
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        // Baris horizontal: Info artikel (kiri) + gambar (kanan)
        Row(
            modifier = Modifier
                .padding(12.dp)
        ) {
            // Kolom berisi info teks artikel
            Column(modifier = Modifier.weight(1f)) {
                // Label kategori + author
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Lingkaran label kategori
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when (article.genre) {
                                    "Kesehatan" -> Color(0xFFFFC1C1)
                                    "Lifestyle" -> Color(0xFFB39DDB)
                                    else -> Color(0xFFE0E0E0)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (article.genre) {
                                "Kesehatan" -> "K"
                                "Lifestyle" -> "L"
                                else -> "A"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = article.author ?: "Unkown Author",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Judul artikel
                Text(
                    text = article.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Waktu publish
                Text(
                    text = formatRelativeTime(article.published_at),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Gambar thumbnail - menggunakan placeholder karena image_url mungkin null
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                // Untuk sementara gunakan image placeholder
                // Nanti bisa ditambahkan AsyncImage dari Coil untuk load image dari URL
                Image(
                    painter = painterResource(
                        id = when (article.genre) {
                            "Kesehatan" -> R.drawable.artikel1
                            "Lifestyle" -> R.drawable.artikel2
                            else -> R.drawable.artikel3
                        }
                    ),
                    contentDescription = "Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

// Utility function untuk format tanggal menjadi relative time
private fun formatRelativeTime(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        val now = Date()
        val diffInMillis = now.time - (date?.time ?: 0)
        val diffInHours = diffInMillis / (1000 * 60 * 60)
        val diffInDays = diffInHours / 24

        when {
            diffInHours < 1 -> "Baru saja"
            diffInHours < 24 -> "$diffInHours jam yang lalu"
            diffInDays == 1L -> "Kemarin"
            diffInDays < 7 -> "$diffInDays hari yang lalu"
            else -> {
                val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                outputFormat.format(date ?: Date())
            }
        }
    } catch (e: Exception) {
        "Waktu tidak tersedia"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArtikelKategoriScreen() {
    val navController = rememberNavController()
    ArtikelKategoriScreen(navController, "Kesehatan")
}