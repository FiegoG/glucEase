// view/artikel/ArtikelScreen.kt (Updated)
package com.example.uijp.view.artikel

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uijp.R
import com.example.uijp.data.model.Article
import com.example.uijp.viewmodel.ArticleViewModel
import com.example.uijp.viewmodel.ArticleViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ArtikelScreen(
    navController: NavController,
    viewModel: ArticleViewModel = viewModel(
        factory = ArticleViewModelFactory(LocalContext.current.applicationContext)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable{navController.navigate("home")}
                    .padding(end = 6.dp)
            )
            Text(
                text = "Artikel & Edukasi",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
        }

        // Search Bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(25.dp),
            singleLine = true,
            trailingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        )

        // Loading State
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Error State
        uiState.error?.let { errorMessage ->
            Spacer(modifier = Modifier.height(16.dp))
            ErrorStateComponent(
                message = errorMessage,
                onRetry = { viewModel.loadHomepageArticles() } // Aksi retry yang sesuai
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Terpopuler/Terbaru Section
        Text(
            text = "Terpopuler/Terbaru",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Highlighted Article
        uiState.highlightedArticle?.let { article ->
            HighlightedArticleCard(
                article = article,
                onClick = {
                    navController.navigate("detail/${article.id}")
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Kesehatan Section
        CategorySection(
            title = "Kesehatan",
            articles = uiState.kesehatanArticles.take(3),
            onSeeAllClick = {
                navController.navigate("artikel_kategori/Kesehatan")
            },
            onArticleClick = { article ->
                navController.navigate("detail/${article.id}")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Lifestyle Section
        CategorySection(
            title = "Life style",
            articles = uiState.lifestyleArticles.take(3),
            onSeeAllClick = {
                navController.navigate("artikel_kategori/Lifestyle")
            },
            onArticleClick = { article ->
                navController.navigate("detail/${article.id}")
            }
        )
    }
}

@Composable
fun HighlightedArticleCard(
    article: Article,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() }
    ) {
        Box {
            // Background image (placeholder)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF81D4FA))
            ) {
                // Placeholder untuk gambar
                Image(
                    painter = painterResource(id = R.drawable.artikel1), // placeholder
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Overlay content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = article.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Baca selengkapnya",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySection(
    title: String,
    articles: List<Article>,
    onSeeAllClick: () -> Unit,
    onArticleClick: (Article) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onSeeAllClick() }
            ) {
                Icon(
                    Icons.Default.NavigateNext,
                    contentDescription = "Lihat semua",
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(articles) { article ->
                CategoryArticleCard(
                    article = article,
                    onClick = { onArticleClick(article) }
                )
            }
        }
    }
}

@Composable
fun CategoryArticleCard(
    article: Article,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(280.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E0E0))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.artikel2), // placeholder
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formatDate(article.published_at),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        "Tanggal tidak tersedia"
    }
}

@Composable
fun ErrorStateComponent(message: String, onRetry: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Gagal memuat data",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFD32F2F)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message, // Menampilkan pesan error dinamis dari ViewModel
                fontSize = 14.sp,
                color = Color(0xFF7A7A7A),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry, // Memanggil fungsi retry
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF67669)
                )
            ) {
                Text("Coba Lagi", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArtikelScreen() {
    val navController = rememberNavController() // Dummy NavController
    ArtikelScreen(navController)
}
