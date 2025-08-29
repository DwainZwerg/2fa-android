package app.ninesevennine.twofactorauthenticator

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.ninesevennine.twofactorauthenticator.features.crypto.SecureCrypto
import app.ninesevennine.twofactorauthenticator.features.locale.LocaleViewModel
import app.ninesevennine.twofactorauthenticator.features.theme.ThemeOption
import app.ninesevennine.twofactorauthenticator.features.vault.VaultViewModel
import app.ninesevennine.twofactorauthenticator.features.vault.VaultViewModelFactory
import app.ninesevennine.twofactorauthenticator.utils.Logger
import app.ninesevennine.twofactorauthenticator.utils.System

val LocalNavController =
    staticCompositionLocalOf<NavHostController> { error("NavController not provided") }

val LocalVaultViewModel =
    staticCompositionLocalOf<VaultViewModel> { error("VaultViewModel not provided") }

class MainActivity : ComponentActivity() {
    private var init = false
    private lateinit var vaultViewModel: VaultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.initialize(this)

        if (!init) {
            Logger.i("APP", "APPLICATION_ID = ${BuildConfig.APPLICATION_ID}")
            Logger.i("APP", "BUILD_TYPE = ${BuildConfig.BUILD_TYPE}")
            Logger.i("APP", "VERSION_CODE = ${BuildConfig.VERSION_CODE}")
            Logger.i("APP", "VERSION_NAME = ${BuildConfig.VERSION_NAME}")
            Logger.i(
                "SYSTEM",
                "ro.build.fingerprint = ${System.getSystemProperty("ro.build.fingerprint")}"
            )

            init = true
        }

        SecureCrypto.initialize(this)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            val navController: NavHostController = rememberNavController()
            val vaultViewModel: VaultViewModel = viewModel(factory = VaultViewModelFactory(context))

            this@MainActivity.vaultViewModel = vaultViewModel

            LaunchedEffect(themeViewModel.theme) {
                val isDarkTheme = themeViewModel.theme == ThemeOption.DARK.value
                val controller = WindowInsetsControllerCompat(window, window.decorView)
                controller.isAppearanceLightStatusBars = !isDarkTheme
                controller.isAppearanceLightNavigationBars = !isDarkTheme
            }

            CompositionLocalProvider(
                LocalNavController provides navController,
                LocalVaultViewModel provides vaultViewModel
            ) {
                AppNavigation()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        vaultViewModel.saveVault()
        configViewModel.save(this)
    }
}