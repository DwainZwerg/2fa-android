package app.ninesevennine.twofactorauthenticator.ui.elements.otpcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.ninesevennine.twofactorauthenticator.LocalThemeViewModel
import app.ninesevennine.twofactorauthenticator.LocalVaultViewModel
import app.ninesevennine.twofactorauthenticator.features.vault.VaultItem
import app.ninesevennine.twofactorauthenticator.utils.Base32

@Composable
fun OtpCard(
    modifier: Modifier = Modifier,
    item: VaultItem,
    dragging: Boolean,
    enableEditing: Boolean = true
) {
    val colors = LocalThemeViewModel.current.getOtpCardColors(item.otpCardColor)
    val vaultViewModel = LocalVaultViewModel.current

    val currentTimeSeconds by vaultViewModel.currentTimeSeconds.collectAsState()
    val secondsLeft = item.period - (currentTimeSeconds % item.period)
    val progress = secondsLeft.toFloat() / item.period.toFloat()

    val currentCycle = currentTimeSeconds / item.period

    val otpCode = remember(item, currentCycle) {
        if (item.secret.isEmpty()) return@remember ""

        vaultViewModel.generateOtp(
            otpType = item.otpType,
            otpHashFunction = item.otpHashFunction,
            secret = item.secret,
            digits = item.digits,
            period = item.period,
            count = item.counter,
            currentTimeSeconds = currentCycle * item.period
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .height(208.dp)
            .background(
                color = colors.firstColor,
                shape = RoundedCornerShape(32.dp)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OtpCardUpper(
                enableEditing = enableEditing,
                id = item.id,
                otpType = item.otpType,
                name = item.name,
                issuer = item.issuer,
                sweepAngle = 360f * progress,
                colors = colors,
                onRefreshButton = {
                    vaultViewModel.updateItem(item.copy(counter = item.counter + 1))
                }
            )
            Spacer(Modifier.height(16.dp))
            OtpCardLower(
                colors = colors,
                code = otpCode
            )
        }
    }
}