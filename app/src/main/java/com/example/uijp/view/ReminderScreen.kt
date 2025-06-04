package com.example.uijp.view

import android.app.TimePickerDialog
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uijp.R
import com.example.uijp.viewmodel.ReminderViewModel

@Composable
fun ReminderScreen(navController: NavController, viewModel: ReminderViewModel = viewModel()) {
    val context = LocalContext.current

    val (hour, minute) = viewModel.getCurrentHourMinute()
    var expanded by remember { mutableStateOf(false) }

    val timePickerDialog = TimePickerDialog(
        ContextThemeWrapper(context, R.style.CustomTimePickerDialog),
        { _, selectedHour: Int, selectedMinute: Int ->
            viewModel.updateTime(selectedHour, selectedMinute)
        },
        hour, minute, true
    )

    val waktuBorderColor = if (viewModel.selectedTime.isEmpty()) Color(0xFFDDDDDD) else Color(0xFF000000)
    val hariBorderColor = if (viewModel.selectedDay.isEmpty()) Color(0xFFDDDDDD) else Color(0xFF000000)

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {

        // HEADER
        Box(Modifier.height(64.dp).fillMaxWidth()) {
            IconButton(onClick = { navController.navigate("home") }, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
            }

            Text("Reminder", modifier = Modifier.align(Alignment.Center), fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        }

        Spacer(Modifier.height(36.dp))

        // ICON
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(124.dp).clip(CircleShape).background(Color(0xFFF67669))) {
                Icon(imageVector = Icons.Default.Notifications, modifier = Modifier.size(64.dp).align(Alignment.Center), contentDescription = null, tint = Color.White)
            }
            Spacer(Modifier.height(12.dp))
            Text("Pilih waktu untuk menerima notifikasi cek gula darah", color = Color(0xFF7A7A7A), textAlign = TextAlign.Center, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(24.dp))

        // WAKTU
        Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            Text("Waktu", fontSize = 14.sp, color = Color(0xFF666666))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, waktuBorderColor, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                    BasicTextField(
                        value = viewModel.selectedTime,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        decorationBox = { innerTextField ->
                            if (viewModel.selectedTime.isEmpty()) Text("Set alarm...", color = Color.LightGray)
                            innerTextField()
                        }
                    )

                    IconButton(onClick = { timePickerDialog.show() }) {
                        Image(painter = painterResource(R.drawable.icon_clock), contentDescription = null, modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // HARI
        Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            Text("Ulangi Setiap", fontSize = 14.sp, color = Color(0xFF666666))

            Box {
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
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (viewModel.selectedDay.isEmpty()) "Pilih hari..." else viewModel.selectedDay,
                            color = if (viewModel.selectedDay.isEmpty()) Color.LightGray else Color.Black,
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

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth().background(Color.White)
                ) {
                    viewModel.days.forEach { day ->
                        DropdownMenuItem(text = { Text(day) }, onClick = {
                            viewModel.updateDay(day)
                            expanded = false
                        })
                    }
                }
            }
        }

        Spacer(Modifier.height(48.dp))

        // BUTTON
        Button(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).padding(horizontal = 8.dp).height(40.dp),
            onClick = {
                Toast.makeText(context, "Reminder berhasil disimpan", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(8.dp),
            enabled = viewModel.isReminderValid(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF67669), disabledContainerColor = Color(0xFFF7958F))
        ) {
            Text("Simpan", color = Color.White)
        }
    }
}
