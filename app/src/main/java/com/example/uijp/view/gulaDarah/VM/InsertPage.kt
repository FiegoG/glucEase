package com.example.uijp.gulaDarah.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uijp.viewmodel.BloodSugarViewModel
import com.example.uijp.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertPages(viewModel: BloodSugarViewModel, navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()
    val snackbarHostState = remember { SnackbarHostState() }

    // Observe UI state from ViewModel
    val uiState by viewModel.uiState.collectAsState()

    var gulaDarah by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf(SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date()).uppercase()) }
    var jam by remember { mutableStateOf(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())) }

    // Format tanggal untuk API (yyyy-MM-dd)
    var tanggalForApi by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }

//    fun showDatePicker() {
//        DatePickerDialog(
//            ContextThemeWrapper(context, R.style.CustomDatePickerDialog),
//            { _, year, month, dayOfMonth ->
//                val selectedCalendar = Calendar.getInstance()
//                selectedCalendar.set(year, month, dayOfMonth)
//
//                // Format untuk tampilan
//                tanggal = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(selectedCalendar.time).uppercase()
//                // Format untuk API
//                tanggalForApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCalendar.time)
//            },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        ).show()
//    }
fun showDatePicker() {
    DatePickerDialog(
        ContextThemeWrapper(context, R.style.CustomDatePickerDialog),
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Format untuk tampilan (pakai lokal, contoh "05 JUN 2025")
            tanggal = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                .format(selectedCalendar.time)
                .uppercase()

            tanggalForApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(selectedCalendar.time)

        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}


    fun showTimePicker() {
        TimePickerDialog(
            ContextThemeWrapper(context, R.style.CustomTimePickerDialog),
            { _, hour, minute ->
                jam = String.format("%02d:%02d", hour, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    // Handle success state
    LaunchedEffect(uiState.addSuccess) {
        if (uiState.addSuccess) {
            snackbarHostState.showSnackbar("Data berhasil disimpan")
            viewModel.clearAddSuccess()
            // Reset form
            gulaDarah = ""
            tanggal = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date()).uppercase()
            tanggalForApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            jam = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

            // Navigate back to dashboard
            navController.navigate("guladarah") {
                popUpTo("guladarah") { inclusive = true }
            }
        }
    }

    // Handle error state
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearErrorMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF67669))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.padding(start = 18.dp),
                onClick = { navController.navigate("guladarah") }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back Button",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(
                "Input Gula Darah",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181818),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 50.dp)
            )
        }

        // Main Content Card
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 145.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Input Gula Darah
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 39.dp, start = 31.dp)
                ) {
                    Text(
                        "Input Gula Darah (mg/dL)",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF666666),
                            letterSpacing = 0.84.sp,
                        )
                    )
                    Spacer(modifier = Modifier.height(17.dp))

                    OutlinedTextField(
                        value = gulaDarah,
                        onValueChange = {
                            // Only allow numeric input
                            if (it.all { char -> char.isDigit() } || it.isEmpty()) {
                                gulaDarah = it
                            }
                        },
                        placeholder = {
                            Text(
                                "Masukkan nilai gula darah",
                                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(end = 31.dp),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 14.sp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = gulaDarah.isNotEmpty() && (gulaDarah.toIntOrNull() == null || gulaDarah.toInt() <= 0),
                        supportingText = {
                            if (gulaDarah.isNotEmpty() && (gulaDarah.toIntOrNull() == null || gulaDarah.toInt() <= 0)) {
                                Text(
                                    "Masukkan nilai yang valid (lebih dari 0)",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    )
                }

                // Input Tanggal
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 21.dp, start = 31.dp)
                ) {
                    Text(
                        "Input Tanggal",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF666666),
                            letterSpacing = 0.84.sp,
                        )
                    )
                    Spacer(modifier = Modifier.height(17.dp))

                    OutlinedTextField(
                        value = tanggal,
                        onValueChange = {},
                        placeholder = {
                            Text(
                                "Pilih tanggal",
                                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(end = 31.dp),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 14.sp),
                        singleLine = true,
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker() }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                            }
                        }
                    )
                }

                // Input Jam
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 21.dp, start = 31.dp)
                ) {
                    Text(
                        "Input Jam",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF666666),
                            letterSpacing = 0.84.sp,
                        )
                    )
                    Spacer(modifier = Modifier.height(17.dp))

                    OutlinedTextField(
                        value = jam,
                        onValueChange = {},
                        placeholder = {
                            Text(
                                "Pilih waktu",
                                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(end = 31.dp),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 14.sp),
                        singleLine = true,
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showTimePicker() }) {
                                Icon(Icons.Default.AccessTime, contentDescription = "Pick Time")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Submit Button
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 31.dp, end = 31.dp, bottom = 32.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        if (gulaDarah.isNotEmpty()) {
                            val gulaDarahInt = gulaDarah.toIntOrNull()
                            if (gulaDarahInt != null && gulaDarahInt > 0) {
                                // Call ViewModel to add record
                                viewModel.addBloodSugarRecord(gulaDarahInt, tanggalForApi, jam)
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Nilai gula darah harus berupa angka yang valid")
                                }
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Harap masukkan nilai gula darah")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF67669)
                    ),
                    enabled = !uiState.isAddingRecord && uiState.isUserLoggedIn
                ) {
                    if (uiState.isAddingRecord) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Menyimpan...",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Text(
                            "Tambahkan Gula Darah",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Show login message if user is not logged in
                if (!uiState.isUserLoggedIn) {
                    Text(
                        "Anda harus login terlebih dahulu untuk menambahkan data",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 31.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = Color(0xFF323232),
                contentColor = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}