package app.ninesevennine.twofactorauthenticator

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.ninesevennine.twofactorauthenticator.features.theme.ThemeOption
import app.ninesevennine.twofactorauthenticator.ui.elements.UseIncognitoKeyboard

val LocalNavController =
    staticCompositionLocalOf<NavHostController> { error("NavController not provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        enableEdgeToEdge()
        setContent {
            val navController: NavHostController = rememberNavController()

            LaunchedEffect(themeViewModel.theme) {
                val isDarkTheme = themeViewModel.theme == ThemeOption.DARK.value
                val controller = WindowInsetsControllerCompat(window, window.decorView)
                controller.isAppearanceLightStatusBars = !isDarkTheme
                controller.isAppearanceLightNavigationBars = !isDarkTheme
            }

            CompositionLocalProvider(
                LocalNavController provides navController
            ) {
                UseIncognitoKeyboard {
                    AppNavigation()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        vaultViewModel.save(this)
        configViewModel.save(this)
    }
}