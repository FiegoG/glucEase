package com.example.uijp.view.Gamifikasi

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import  com.example.uijp.R

@Composable
fun KlaimReward(navController: NavController){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
        ) {
            Row(
                Modifier.fillMaxWidth()
                    .height(28.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable{navController.popBackStack()}
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Reward ",
//                        fontFamily = robotoFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF494950),
                        modifier = Modifier
                            .padding(end = 28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .padding(horizontal = 24.dp)
                    .background(color = Color(0xFFF7958F), shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.width(288.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Kamu sudah mengumpulkan",
//                        fontFamily = robotoFontFamily,
                        fontSize = 14.sp,
                        color = Color(0xFF494950),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "3,280",
//                                fontFamily = robotoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = Color(0xFF494950),
                            )
                            Text(
                                text = " Poin",
//                                fontFamily = robotoFontFamily,
                                fontSize = 14.sp,
                                color = Color(0xFF494950),
                                modifier = Modifier.padding(bottom = 4.dp) // tweak as needed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tukarkan poinmu untuk mendapatkan reward menarik!",
//                        fontFamily = robotoFontFamily,
                        fontSize = 14.sp,
                        color = Color(0xFF494950),
                        textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth() // 👈 this too
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = { /* Handle button click */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE64646)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .width(140.dp)
                            .height(40.dp)
                    ) {
                        Text(
                            text = "Tukar Poin",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
//                            fontFamily = robotoFontFamily
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(horizontal = 24.dp)
            ){
                Text(
                    "Daftar Reward",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
//                    fontFamily = robotoFontFamily
                )

                Spacer(modifier = Modifier.width(165.dp))
                Text(
                    "Lihat Semua",
                    color = Color.Black,
                    fontSize = 14.sp,
//                    fontFamily = robotoFontFamily,
                )
            }
            Spacer(modifier = Modifier.height(23.dp))

            RewardListItem(
                rewardName = "Voucher e-Wallet",
                pointsRequired = "3000",
                expiryDate = "30 Apr 2025",
                onClaimClick = {}
            )

            RewardListItem(
                rewardName = "Diskon Konsultasi Dokter",
                pointsRequired = "7000",
                expiryDate = "30 Apr 2025",
                onClaimClick = {}
            )
            RewardListItem(
                rewardName = "Voucher e-Wallet",
                pointsRequired = "3000",
                expiryDate = "30 Apr 2025",
                onClaimClick = {}
            )
        }
    }
}

@Composable
fun RewardListItem(
    rewardName: String,
    pointsRequired: String,
    expiryDate: String,
    onClaimClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 8.dp)
            .height(114.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cornerRadius = 10.dp.toPx()
            val notchRadius = 4.dp.toPx()
            val paintArea = Size(size.width, size.height)

            drawRoundRect(
                color = Color(0xFFFFDAD6),
                size = paintArea,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )

            drawCircle(
                color = Color.White,
                radius = notchRadius,
                center = Offset(size.width - 32.5.dp.toPx(), 0f)
            )

            drawCircle(
                color = Color.White,
                radius = notchRadius,
                center = Offset(size.width - 32.5.dp.toPx(), size.height)
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-30).dp)
        ) {
            val dotHeight = 8.dp.toPx()
            val spaceHeight = 6.dp.toPx()
            var y = 0f
            while (y < size.height) {
                drawRect(
                    color = Color.White,
                    topLeft = Offset(0f, y),
                    size = Size(2.dp.toPx(), dotHeight)
                )
                y += dotHeight + spaceHeight
            }
        }

        // Content Row (keep this part unchanged)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = rewardName,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color(0xFF242422)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Poin dibutuhkan: $pointsRequired",
                        fontSize = 10.sp,
//                        fontFamily = robotoFontFamily,
                        color = Color(0xFF747474)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Berlaku sampai: $expiryDate",
                        fontSize = 10.sp,
//                        fontFamily = robotoFontFamily,
                        color = Color(0xFF747474)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .offset(x = (-30).dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(20.dp)
                            .background(color = Color(0xFFE64646), shape = RoundedCornerShape(8.dp))
                            .clickable(onClick = onClaimClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Klaim",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
//                            fontFamily = robotoFontFamily
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun KlaimRewardPreview(){
    KlaimReward(navController = rememberNavController())
}

