package com.example.uijp.view.artikel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import coil.compose.AsyncImage
import com.example.uijp.R
import com.example.uijp.viewmodel.ArticleViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailArtikelScreen(
    articleId: Int,
    navController: NavController,
    viewModel: ArticleViewModel = viewModel()
) {
    val detailUiState by viewModel.detailUiState.collectAsState()

    // Load article detail when screen is first displayed
    LaunchedEffect(articleId) {
        viewModel.loadArticleDetail(articleId)
    }

    Scaffold { paddingValues ->
        when {
            detailUiState.isLoading -> {
                // Loading State
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            detailUiState.error != null -> {
                // Error State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    // Back button
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.7f), shape = CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Error message
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Text(
                            text = detailUiState.error ?: "Terjadi kesalahan",
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFFD32F2F)
                        )
                    }
                }
            }

            detailUiState.article != null -> {
                // Success State - Show article content
                val article = detailUiState.article!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues)
                ) {
                    Box {
                        // Article image
                        if (!article.image_url.isNullOrEmpty()) {
                            AsyncImage(
                                model = article.image_url,
                                contentDescription = "Gambar Artikel",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp),
                                contentScale = ContentScale.Crop,
                                error = painterResource(
                                    id = when (article.genre) {
                                        "Kesehatan" -> R.drawable.artikel1
                                        "Lifestyle" -> R.drawable.artikel2
                                        else -> R.drawable.artikel3
                                    }
                                ),
                                placeholder = painterResource(
                                    id = when (article.genre) {
                                        "Kesehatan" -> R.drawable.artikel1
                                        "Lifestyle" -> R.drawable.artikel2
                                        else -> R.drawable.artikel3
                                    }
                                )
                            )
                        } else {
                            // Fallback image
                            Image(
                                painter = painterResource(
                                    id = when (article.genre) {
                                        "Kesehatan" -> R.drawable.artikel1
                                        "Lifestyle" -> R.drawable.artikel2
                                        else -> R.drawable.artikel3
                                    }
                                ),
                                contentDescription = "Gambar Artikel",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Back button
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .padding(16.dp)
                                .background(Color.White.copy(alpha = 0.7f), shape = CircleShape)
                                .size(36.dp)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }

                        // Like button
                        IconButton(
                            onClick = { /* TODO: Like action */ },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .background(Color(0xFFFFD9CF), shape = CircleShape)
                                .size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = Color(0xFFE86A33)
                            )
                        }

                        // Article title overlay
                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                                .padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Author and publish time section
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Author avatar/initial
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    when (article.genre) {
                                        "Kesehatan" -> Color(0xFFFFC1C1)
                                        "Lifestyle" -> Color(0xFFB39DDB)
                                        else -> Color(0xFFE8EDF3)
                                    },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (article.author?.firstOrNull()?.toString() ?: "A").uppercase(),
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = article.author ?: "Unknown Author",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = formatRelativeTime(article.published_at),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    // Article content
                    Text(
                        text = article.content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 24.dp)
                    )
                }
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
fun PreviewDetailArtikelScreen() {
    val navController = rememberNavController()
    DetailArtikelScreen(articleId = 1, navController = navController)
}