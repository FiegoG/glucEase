package com.example.uijp.view.tracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uijp.R
import com.example.uijp.data.model.Food
import com.example.uijp.viewmodel.UiState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.uijp.viewmodel.SugarTrackerViewModel
import com.example.uijp.viewmodel.SugarTrackerViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahMakananScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModelFactory = remember { SugarTrackerViewModelFactory(context) }
    val viewModel: SugarTrackerViewModel = viewModel(factory = viewModelFactory)

    // Observe states
    val foodListState by viewModel.foodListState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isActionLoading by viewModel.isActionLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    // Show snackbar for messages
    LaunchedEffect(message) {
        message?.let {
            // Handle success message - you can show snackbar here if needed
            viewModel.clearMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tambah Makanan",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                viewModel.updateSearchQuery(query)
            },
            leadingIcon = {
                IconButton(onClick = {
                    viewModel.searchFoods(searchQuery)
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            placeholder = { Text("Cari makanan...") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.White,
                focusedContainerColor = Color(0xFFF6F6F6),
                unfocusedContainerColor = Color(0xFFFFFFFF)
            ),
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    TextButton(
                        onClick = { viewModel.searchFoods(searchQuery) }
                    ) {
                        Text("Cari", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "List makanan yang tersedia",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Content based on state
        when (val state = foodListState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                val foods = state.data
                if (foods.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada makanan ditemukan",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn {
                        items(foods) { food ->
                            FoodItem(
                                food = food,
                                isLoading = isActionLoading,
                                onAddClick = {
                                    viewModel.addFoodToTracker(food.id) {
                                        navController.popBackStack()
                                    }
                                }
                            )
                        }
                    }
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.message,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadFoodList() }
                        ) {
                            Text("Coba Lagi")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItem(
    food: Food,
    isLoading: Boolean,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(enabled = !isLoading) { onAddClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food Image (using placeholder for now)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFE5E0)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nasiputih), // Default image
                    contentDescription = food.name,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Food Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.name,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Portion" + food.portion_detail,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Gula: ${String.format("%.1f", food.sugar)}g", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Karbo: ${String.format("%.1f", food.carbohydrate)}g", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Protein: ${String.format("%.1f", food.protein)}g", fontSize = 12.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text("Kalori: ${String.format("%.0f", food.calories)} kcal", fontSize = 8.sp, color = Color.Gray)
            }

            // Add Button
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF67669)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Tambah", fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTambahMakanan () {
    val navController = rememberNavController()
    TambahMakananScreen(navController = navController)

}