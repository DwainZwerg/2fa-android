package app.ninesevennine.twofactorauthenticator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.ninesevennine.twofactorauthenticator.features.crypto.SecureCrypto
import app.ninesevennine.twofactorauthenticator.features.locale.LocaleModel
import app.ninesevennine.twofactorauthenticator.features.locale.LocaleViewModel
import app.ninesevennine.twofactorauthenticator.features.locale.LocaleViewModelFactory
import app.ninesevennine.twofactorauthenticator.features.theme.ThemeModel
import app.ninesevennine.twofactorauthenticator.features.theme.ThemeViewModel
import app.ninesevennine.twofactorauthenticator.features.theme.ThemeViewModelFactory
import app.ninesevennine.twofactorauthenticator.features.vault.VaultViewModel
import app.ninesevennine.twofactorauthenticator.features.vault.VaultViewModelFactory
import app.ninesevennine.twofactorauthenticator.utils.Logger
import app.ninesevennine.twofactorauthenticator.utils.System

val LocalNavController =
    staticCompositionLocalOf<NavHostController> { error("NavController not provided") }
val LocalThemeViewModel =
    staticCompositionLocalOf<ThemeViewModel> { error("ThemeViewModel not provided") }
val LocalLocaleViewModel =
    staticCompositionLocalOf<LocaleViewModel> { error("LocaleViewModel not provided") }

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

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            val navController: NavHostController = rememberNavController()
            val themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModelFactory(context))
            val localeViewModel: LocaleViewModel =
                viewModel(factory = LocaleViewModelFactory(context))
            val vaultViewModel: VaultViewModel = viewModel(factory = VaultViewModelFactory(context))

            themeViewModel.updateTheme(ThemeModel.readTheme(context))
            localeViewModel.updateLocale(LocaleModel.readLocale(context))

            this@MainActivity.vaultViewModel = vaultViewModel

            CompositionLocalProvider(
                LocalNavController provides navController,
                LocalThemeViewModel provides themeViewModel,
                LocalLocaleViewModel provides localeViewModel,
                LocalVaultViewModel provides vaultViewModel
            ) {
                AppNavigation()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        vaultViewModel.saveVault()
    }
}