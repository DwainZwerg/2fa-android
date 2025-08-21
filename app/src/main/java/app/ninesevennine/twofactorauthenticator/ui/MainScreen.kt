package app.ninesevennine.twofactorauthenticator.ui

import androidx.compose.runtime.Composable
import app.ninesevennine.twofactorauthenticator.LocalNavController
import app.ninesevennine.twofactorauthenticator.features.vault.VaultView
import app.ninesevennine.twofactorauthenticator.ui.elements.bottomappbar.MainAppBar
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
            @OptIn(ExperimentalUuidApi::class)
            navController.navigate(EditScreenRoute("00000000-0000-0000-0000-000000000000"))
        }
    )
}