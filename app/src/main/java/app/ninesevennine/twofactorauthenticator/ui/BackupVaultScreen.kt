package app.ninesevennine.twofactorauthenticator.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.ninesevennine.twofactorauthenticator.LocalNavController
import app.ninesevennine.twofactorauthenticator.LocalThemeViewModel
import app.ninesevennine.twofactorauthenticator.LocalVaultViewModel
import app.ninesevennine.twofactorauthenticator.ui.elements.WideText
import app.ninesevennine.twofactorauthenticator.ui.elements.WideTitle
import app.ninesevennine.twofactorauthenticator.ui.elements.textfields.ConfidentialSingleLineTextField
import app.ninesevennine.twofactorauthenticator.ui.elements.widebutton.WideButton
import app.ninesevennine.twofactorauthenticator.utils.Logger
import app.ninesevennine.twofactorauthenticator.utils.Password
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
object BackupVaultScreenRoute

@Composable
fun BackupVaultScreen() {
    val context = LocalContext.current
    val colors = LocalThemeViewModel.current.colors
    val navController = LocalNavController.current
    val vaultViewModel = LocalVaultViewModel.current

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordLong by remember { mutableStateOf(false) }
    var hasPasswordUppercase by remember { mutableStateOf(false) }
    var hasPasswordDigit by remember { mutableStateOf(false) }
    var hasPasswordSpecial by remember { mutableStateOf(false) }

    isPasswordLong = Password.isLong(password)
    hasPasswordUppercase = Password.hasUppercase(password)
    hasPasswordDigit = Password.hasDigit(password)
    hasPasswordSpecial = Password.hasSpecial(password)

    val isPasswordStrong =
        isPasswordLong && hasPasswordUppercase && hasPasswordDigit && hasPasswordSpecial

    var passwordsMatch by remember { mutableStateOf(true) }
    passwordsMatch = password == confirmPassword

    val backupScope = rememberCoroutineScope()
    var backupContent by remember { mutableStateOf("") }
    var isBackingUp by remember { mutableStateOf(false) }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = CreateDocument("application/json"),
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(backupContent.toByteArray())
                }

                Logger.i("BackupVaultScreen", "Vault successfully backed up")
                navController.popBackStack(
                    navController.graph.startDestinationId,
                    inclusive = false
                )
            } catch (e: Exception) {
                Logger.e("BackupVaultScreen", "Error saving vault: ${e.message}")
            }
        }

        isBackingUp = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
    ) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Upload,
                    contentDescription = null,
                    modifier = Modifier.size(192.dp),
                    tint = colors.onBackground
                )
            }

            WideTitle(text = "Credentials")

            ConfidentialSingleLineTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isError = !isPasswordStrong
            )

            if (!isPasswordLong) WideText(
                text = "• Must be at least 8 characters",
                color = colors.error
            )

            if (!hasPasswordUppercase) WideText(
                text = "• Include uppercase",
                color = colors.error
            )

            if (!hasPasswordDigit) WideText(
                text = "• Include number",
                color = colors.error
            )

            if (!hasPasswordSpecial) WideText(
                text = "• Include special character",
                color = colors.error
            )

            ConfidentialSingleLineTextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                isError = !passwordsMatch
            )

            WideButton(
                label = if (isBackingUp) "Backing up..." else "Backup",
                color = colors.primary,
                textColor = colors.onPrimary,
                onClick = {
                    if (!isPasswordStrong || !passwordsMatch || isBackingUp) {
                        return@WideButton
                    }

                    backupScope.launch {
                        isBackingUp = true

                        backupContent = withContext(Dispatchers.Default) {
                            vaultViewModel.backupVault(password)
                        }

                        if (backupContent.isEmpty()) {
                            Logger.e("BackupVaultScreen", "Backup content is empty")
                            isBackingUp = false
                            return@launch
                        }

                        createDocumentLauncher.launch("vault")
                    }
                }
            )
        }

        WideButton(
            label = "Cancel",
            onClick = { navController.popBackStack() }
        )
    }
}