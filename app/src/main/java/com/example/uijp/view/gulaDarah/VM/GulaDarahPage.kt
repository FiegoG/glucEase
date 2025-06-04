package com.example.uijp.gulaDarah.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.example.uijp.R
import com.example.uijp.data.model.BloodSugarRecord
import com.example.uijp.data.model.ChartDataItem
import com.example.uijp.viewmodel.BloodSugarViewModel
import com.example.uijp.viewmodel.BloodSugarViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GulaDarahPage(
    navController: NavController
) {
    // Dapatkan Context dari Composable
    val context = LocalContext.current
    // Buat instance factory dengan applicationContext
    val factory = remember { BloodSugarViewModelFactory(context.applicationContext) }
    // Inisialisasi ViewModel menggunakan factory
    val viewModel: BloodSugarViewModel = viewModel(factory = factory)

    var selectedIndex by remember { mutableStateOf(2) }
    val uiState by viewModel.uiState.collectAsState()

    // Handle success message
    LaunchedEffect(uiState.addSuccess) {
        if (uiState.addSuccess) {
            // Show success message or navigate
            viewModel.clearAddSuccess()
        }
    }

    // Handle error message
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            // Show error snackbar atau toast
            // viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        containerColor = Color.White,
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Gray
                        )
                    }
                    Text(
                        text = "Tracker Gula Darah",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        color = Color.Black
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFC7D6D7))
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_menu_sort_by_size),
                                            contentDescription = "Chart",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.padding(start = 12.dp)) {
                                        Text(
                                            text = "Gula Darah",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = "(mg/dL per Hari)",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

//
                                }

                                // Use API data instead of dummy data
                                ApiBasedGulaDarahChart(
                                    chartData = uiState.chartData,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .padding(top = 16.dp, bottom = 12.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            LegendItem(
                                color = Color(0xFF6DD6D3),
                                text = "Normal"
                            )
                            LegendItem(
                                color = Color(0xFFFFCC66),
                                text = "Waspada"
                            )
                            LegendItem(
                                color = Color(0xFFFF6666),
                                text = "Tinggi - Perlu konsultasi"
                            )
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Riwayat Gula Darah",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(
                        onClick = { navController.navigate("insert") },
                        shape = RoundedCornerShape(15),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEE6C6C)),
                        enabled = !uiState.isAddingRecord
                    ) {
                        if (uiState.isAddingRecord) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text("Tambah Gula Darah", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f)
                        .border(1.dp, color = Color(0xFFB6B6B6), RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFE0E0))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "TANGGAL & WAKTU",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "GULA DARAH",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "STATUS",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(0.6f),
                                textAlign = TextAlign.End
                            )
                        }

                        // Use API data instead of dummy data
                        LazyColumn {
                            itemsIndexed(uiState.recentHistory) { index, entry ->
                                ApiBloodSugarItem(entry, index)
                            }
                        }
                    }
                }
            }

            // Show error message
            uiState.errorMessage?.let { error ->
                LaunchedEffect(error) {
                    // Show snackbar or toast
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ApiBasedGulaDarahChart(
    chartData: List<ChartDataItem>,
    modifier: Modifier = Modifier
) {
    val cappedChartData = chartData.map { item ->
        val cappedReadings = item.readings.map { reading ->
            reading.copy(level = minOf(reading.level, 190))
        }
        val cappedAverage = minOf(item.averageLevel, 190.0)
        item.copy(readings = cappedReadings, averageLevel = cappedAverage)
    }

    DayBasedGulaDarahChart(
        weeklyData = cappedChartData,
        modifier = modifier
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ApiBloodSugarItem(record: BloodSugarRecord, index: Int) {
    val backgroundColor = if (index % 2 == 0) Color.White else Color(0xFFFFE0E0)

    val inputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val outputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("dd MM yyyy")

    val formattedDate = try {
        val date = OffsetDateTime.parse(record.check_date, inputFormatter)
        date.toLocalDate().format(outputFormatter)
    } catch (e: Exception) {
        Log.e("BloodSugarItem", "Error parsing date", e)
        record.check_date
    }


    val formattedTime = try {
        LocalTime.parse(record.check_time, inputTimeFormatter).format(outputTimeFormatter)
    } catch (e: Exception) {
        record.check_time
    }

    Log.d("BloodSugarItem", "formattedDate: $formattedDate, formattedTime: $formattedTime")


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$formattedDate, $formattedTime",
            fontSize = 10.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${record.blood_sugar_level} MG/DL",
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier.weight(0.6f),
            contentAlignment = Alignment.CenterEnd
        ) {
            StatusBadge(record.blood_sugar_level)
        }
    }
}

fun convertDateToDayName(dateString: String): String {
    val dayNames = listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab")
    return dayNames.random()
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDayLabel(date: LocalDate): String {
    return when (date.dayOfWeek) {
        java.time.DayOfWeek.MONDAY -> "Sen"
        java.time.DayOfWeek.TUESDAY -> "Sel"
        java.time.DayOfWeek.WEDNESDAY -> "Rab"
        java.time.DayOfWeek.THURSDAY -> "Kam"
        java.time.DayOfWeek.FRIDAY -> "Jum"
        java.time.DayOfWeek.SATURDAY -> "Sab"
        java.time.DayOfWeek.SUNDAY -> "Min"
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayBasedGulaDarahChart(
    weeklyData: List<ChartDataItem>,
    modifier: Modifier = Modifier
) {
    val lineColor = Color(0xFFA8CEF1)
    val minValue = 50f
    val maxValue = 200f
    val step = 20

    val valueRange = maxValue - minValue

    val referenceValues = (minValue.toInt()..maxValue.toInt() step step).toList()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val sortedData = weeklyData.sortedBy { LocalDate.parse(it.date, formatter) }

    Box(modifier = modifier.background(Color.White)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height - 40f
            val horizontalStep = width / sortedData.size

            // Garis horizontal (garis nilai referensi)
            referenceValues.forEach { value ->
                val y = height - ((value - minValue) / valueRange * height)

                drawLine(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )

                drawContext.canvas.nativeCanvas.drawText(
                    value.toString(),
                    10f,
                    y - 5f,
                    Paint().asFrameworkPaint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 30f
                        textAlign = android.graphics.Paint.Align.LEFT
                    }
                )
            }

            val points = mutableListOf<Pair<Offset, Int>>()

            sortedData.forEachIndexed { index, item ->
                val x = index * horizontalStep + horizontalStep / 2
                val y = height - (((item.averageLevel - minValue) / valueRange) * height).toFloat()
                val status = getStatus(item.averageLevel)

                points.add(Offset(x, y) to status)

                val date = LocalDate.parse(item.date, formatter)
                val dayLabel = getDayLabel(date)

                drawContext.canvas.nativeCanvas.drawText(
                    dayLabel,
                    x,
                    height + 30f,
                    Paint().asFrameworkPaint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 30f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }

            // Gambar garis koneksi antar titik
            if (points.size > 1) {
                val path = Path().apply {
                    moveTo(points[0].first.x, points[0].first.y)
                    for (i in 1 until points.size) {
                        lineTo(points[i].first.x, points[i].first.y)
                    }
                }

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                )
            }

            // Gambar titik per hari
            points.forEach { (point, status) ->
                val pointColor = when (status) {
                    0 -> Color(0xFF6DD6D3) // Normal
                    1 -> Color(0xFFFFCC66) // Waspada
                    else -> Color(0xFFFF6666) // Tinggi
                }

                drawCircle(Color.White, radius = 10f, center = point)
                drawCircle(pointColor, radius = 8f, center = point)
            }
        }
    }
}

fun getStatus(avg: Double): Int = when {
    avg < 80 -> 0 // Normal
    avg < 100 -> 1 // Waspada
    else -> 2 // Tinggi
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun StatusBadge(level: Int) {
    val (text, color) = when {
        level < 70 -> "WASPADA" to Color(0xFFFFCC66)
        level in 70..140 -> "NORMAL" to Color(0xFFBCFFD4)
        else -> "TINGGI" to Color(0xFFFF6666)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color = Color(0xFF58C6CD))
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = text,
                fontSize = 10.sp,
                fontWeight = FontWeight.W500
            )
        }
    }
}
