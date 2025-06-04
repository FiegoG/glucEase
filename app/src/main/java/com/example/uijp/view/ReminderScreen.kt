package com.example.uijp.view

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.view.ContextThemeWrapper
import androidx.navigation.NavController
import com.example.uijp.R
import java.util.Calendar

@Composable
fun ReminderScreen(navController: NavController) {
    val context = LocalContext.current
    var timeText by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val waktuBorderColor by remember {
        derivedStateOf {
            if (timeText.isEmpty()) Color(0xFFDDDDDD)
            else Color(0xFF000000)
        }
    }

    val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
    var expanded by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf("") }

    val hariBorderColor by remember {
        derivedStateOf {
            if (selectedDay.isEmpty()) Color(0xFFDDDDDD)
            else Color(0xFF000000)
        }
    }
    val timePickerDialog = TimePickerDialog(
        ContextThemeWrapper(context, R.style.CustomTimePickerDialog),
        { _, selectedHour: Int, selectedMinute: Int ->
            timeText = String.format("%02d:%02d", selectedHour, selectedMinute)
        },
        hour, minute, true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
//        HEADER
        Box(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
        ) {
            IconButton(modifier = Modifier.align(Alignment.CenterStart), onClick = {navController.navigate("home")}) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            Text(
                "Reminder",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        Spacer(Modifier.height(36.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(124.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF67669))
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center),
                    contentDescription = "Center Notification circle",
                    tint = Color.White
                )
            }
            Spacer(Modifier.height(12.dp))

            Text(
                "Pilih waktu untuk menerima notifikasi cek gula darah",
                color = Color(0xFF7A7A7A),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text("Waktu", fontSize = 14.sp, color = Color(0xFF666666))

//            CLOCK TEXT FIELD
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, waktuBorderColor, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = timeText,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier
                            .weight(1f),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        decorationBox = { innerTextField ->
                            if (timeText.isEmpty()) {
                                Text("Set alarm...", color = Color.LightGray)
                            }
                            innerTextField()
                        }
                    )

                    IconButton(onClick = { timePickerDialog.show() }) {
                        Image(
                            modifier = Modifier.padding(4.dp),
                            painter = painterResource(R.drawable.icon_clock),
                            contentDescription = null
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text("Ulangi Setiap", fontSize = 14.sp, color = Color(0xFF666666))

            Box {
                // Dropdown anchor
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(1.dp, hariBorderColor, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { expanded = true }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedDay.isEmpty()) "Pilih hari..." else selectedDay,
                            color = if (selectedDay.isEmpty()) Color.LightGray else Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.rotate(if (expanded) 180f else 0f)
                            )
                        }
                    }
                }

                // Dropdown itself
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    days.forEach { day ->
                        DropdownMenuItem(
                            text = { Text(day) },
                            onClick = {
                                selectedDay = day
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(48.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)).
                padding(horizontal = 8.dp)
                .height(40.dp),
            onClick = {
                Toast.makeText(context, "Reminder berhasil disimpan", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(8.dp),
            enabled = timeText.isNotEmpty() && selectedDay.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF67669), disabledContainerColor = Color(0xFFF7958F))
        ) { Text("Simpan", color = Color.White)

        }
    }
}
