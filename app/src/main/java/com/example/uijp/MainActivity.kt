package com.example.uijp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.uijp.view.konsultasi.*
import com.example.konsultasiprofil.UI.Screen.NotifikasiUI
import com.example.konsultasiprofil.UI.Screen.PembayaranUI
import com.example.konsultasiprofil.UI.Screen.PilihWaktuUI
import com.example.uijp.view.Gamifikasi.GamifikasiUI
import com.example.uijp.view.Gamifikasi.KlaimReward
import com.example.uijp.view.artikel.ArtikelKategoriScreen
import com.example.uijp.view.laporan.LaporanMingguanPage
import com.example.uijp.ui.LoginScreen
import com.example.uijp.ui.RegisterScreen
import com.example.uijp.view.artikel.ArtikelScreen
import com.example.uijp.view.artikel.DetailArtikelScreen
import com.example.uijp.view.datadiri.PersonalDataScreen
import com.example.uijp.view.freemium.GetPremiumScreen
import com.example.uijp.gamifikasi.DetailMisi
import com.example.uijp.gulaDarah.ui.GulaDarahPage
import com.example.uijp.gulaDarah.ui.InsertPages
import com.example.uijp.view.konsultasi.DetailDokterUI
import com.example.uijp.view.konsultasi.ProfilUi
import com.example.uijp.navigation.MainNavigation
import com.example.uijp.view.LupaPasswordScreen
import com.example.uijp.view.splash.Onboard1
import com.example.uijp.view.splash.Onboard2
import com.example.uijp.view.splash.Onboard3
import com.example.uijp.view.splash.SplashScreen
import com.example.uijp.view.tracker.TambahMakananScreen
import com.example.uijp.view.tracker.TrackerGulaScreen
import com.example.uijp.view.theme.UijpTheme
import com.example.uijp.view.ReminderScreen
import com.example.uijp.view.ResetPasswordScreen
import com.example.uijp.view.VerificationScreen
import com.example.uijp.viewmodel.BloodSugarViewModel
import com.example.uijp.viewmodel.BloodSugarViewModelFactory
import com.example.uijp.viewmodel.LoginViewModel
import com.example.uijp.viewmodel.LoginViewModelFactory
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.viewmodel.MissionViewModel
import com.example.uijp.viewmodel.MissionViewModelFactory
import com.example.uijp.viewmodel.PremiumViewModel
import com.example.uijp.viewmodel.PremiumViewModelFactory


class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(applicationContext) // Gunakan applicationContext
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitClient.initialize(applicationContext)

        enableEdgeToEdge()
        setContent {
            UijpTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    // Buat factory untuk LoginViewModel
    val loginViewModelFactory = remember { LoginViewModelFactory(context.applicationContext) }
    // Buat factory untuk BloodSugarViewModel
    val bloodSugarViewModelFactory = remember { BloodSugarViewModelFactory(context.applicationContext) }

    val missionViewModelFactory = remember {
        MissionViewModelFactory(context.applicationContext) // Use context
    }

    val premiumViewModelFactory = remember { PremiumViewModelFactory(context.applicationContext) }
    val premiumViewModel: PremiumViewModel = viewModel(factory = premiumViewModelFactory)

    NavHost(navController = navController, startDestination = "splash", modifier = modifier) {
        composable("splash") { SplashScreen(navController) }
        composable("onboard1") { Onboard1(navController) }
        composable("onboard2") { Onboard2(navController) }
        composable("onboard3") { Onboard3(navController) }
        composable("login") { LoginScreen(navController) }
        composable("lupaPassword") { LupaPasswordScreen(navController) }
        composable("resetPassword") { ResetPasswordScreen(navController) }
        composable("verifikasi") { VerificationScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("reminder") { ReminderScreen(navController) }
        composable("tambahmakanan") { TambahMakananScreen(navController) }
        composable("LMP") { LaporanMingguanPage(navController) }
        composable("home") { MainNavigation(navController) }
        composable("artikel") { ArtikelScreen(navController)}
        composable("konsultasi") { KonsultasiUi(navController)}
        composable("tracker") { TrackerGulaScreen(navController)}

        composable("guladarah") {
            GulaDarahPage(navController = navController)
        }

        composable("reward") { KlaimReward(navController) }
        composable("gamifikasi") { GamifikasiUI(navController)}
        composable("detailMisi/{missionId}") { backStackEntry -> // Ubah route

            val missionId = backStackEntry.arguments?.getString("missionId") ?: ""
            val missionViewModel: MissionViewModel = viewModel(factory = missionViewModelFactory)
            if (missionId.isNotEmpty()) {
                DetailMisi(
                    navController = navController,
                    missionId = missionId,
                    missionViewModel = missionViewModel
                )
            }
        }
        composable("profil") { ProfilUi(navController) }
        composable("pilihWaktu") { PilihWaktuUI(navController) }
        composable("pembayaran") { PembayaranUI(navController) }
        composable("notifikasi") { NotifikasiUI(navController) }
        composable("premium") {
            GetPremiumScreen(navController = navController, viewModel = premiumViewModel)
        }
        composable("premiumPrice/{packageId}") { backStackEntry -> // Terima packageId
            val packageId = backStackEntry.arguments?.getString("packageId")?.toIntOrNull()
            PremiumPriceScreen(
                navController = navController,
                viewModel = premiumViewModel,
                packageId = packageId
            )
        }
        composable("paymentMethod") {
            PaymentMethodScreen(navController = navController, viewModel = premiumViewModel)
        }
        composable("paymentSuccess") { PaymentSuccessScreen(navController) }

        composable("insert") {
            val viewModel: BloodSugarViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = bloodSugarViewModelFactory)
            InsertPages(viewModel = viewModel, navController = navController)
        }

        composable("datadiri") { PersonalDataScreen(navController) }

        composable("artikel_kategori/{kategoriName}") { backStackEntry ->
            val kategoriName = backStackEntry.arguments?.getString("kategoriName") ?: ""
            ArtikelKategoriScreen(
                navController = navController, // ← ini penting untuk bisa navigasi
                kategoriName = kategoriName
            )
        }

        composable("detail/{articleId}") { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")?.toIntOrNull() ?: 0
            DetailArtikelScreen(articleId = articleId, navController = navController)
        }

        composable("detailDokter/{doctorId}") { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId")?.toIntOrNull() ?: 0
            DetailDokterUI(
                navController = navController,
                doctorId = doctorId
            )
        }
    }
}

