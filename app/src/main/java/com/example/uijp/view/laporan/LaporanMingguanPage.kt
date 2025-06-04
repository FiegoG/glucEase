package com.example.uijp.view.laporan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun LaporanMingguanPage(navController: NavController) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(bottom = 64.dp) // adjust bottom padding for the navbar
        ) {
            TopBarSection(navController)
            Spacer(modifier = Modifier.height(16.dp))
            RingkasanGulaSection()
            Spacer(modifier = Modifier.height(16.dp))
            RingkasanGulaDarahSection()
            Spacer(modifier = Modifier.height(16.dp))
            AktivitasMisiSection()
            Spacer(modifier = Modifier.weight(1f)) // Push content upwards
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLaporanMingguanPage() {
    LaporanMingguanPage(navController = rememberNavController())
}



//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(bottom = 64.dp)
//    ) {
//        TopBarSection()
//        Spacer(modifier = Modifier.height(16.dp))
//        RingkasanGulaSection()
//        Spacer(modifier = Modifier.height(16.dp))
//        RingkasanGulaDarahSection()
//        Spacer(modifier = Modifier.height(16.dp))
//        AktivitasMisiSection()
//        Spacer(modifier = Modifier.weight(1f))
//        BottomNavigationBar()
//    }
