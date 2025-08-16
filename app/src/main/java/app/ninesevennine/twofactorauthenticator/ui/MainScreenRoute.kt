package app.ninesevennine.twofactorauthenticator.ui

import androidx.compose.runtime.Composable
import app.ninesevennine.twofactorauthenticator.LocalNavController
import app.ninesevennine.twofactorauthenticator.features.vault.VaultView
import app.ninesevennine.twofactorauthenticator.ui.elements.bottomappbar.MainAppBar
import kotlinx.serialization.Serializable

@Serializable
object MainScreenRoute

@Composable
fun MainScreen() {
    val navController = LocalNavController.current

    VaultView()

    MainAppBar(
        onSettings = {
            navController.navigate(SettingsScreenRoute)
        },
        onAdd = {
            navController.navigate(EditScreenRoute(0))
        }
    )
}