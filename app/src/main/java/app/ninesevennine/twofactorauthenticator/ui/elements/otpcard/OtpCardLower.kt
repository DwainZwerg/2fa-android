package app.ninesevennine.twofactorauthenticator.ui.elements.otpcard

import android.content.ClipData
import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.LocalVaultViewModel
import app.ninesevennine.twofactorauthenticator.configViewModel
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable
import app.ninesevennine.twofactorauthenticator.features.vault.VaultItem
import app.ninesevennine.twofactorauthenticator.themeViewModel
import app.ninesevennine.twofactorauthenticator.utils.Logger

@Composable
fun OtpCardLower(
    item: VaultItem
) {
    val context = LocalContext.current
    val view = LocalView.current
    val haptic = LocalHapticFeedback.current
    val theme = context.themeViewModel
    val colors = remember(item.otpCardColor) {
        theme.getOtpCardColors(context, item.otpCardColor)
    }
    val clipboard = LocalClipboard.current
    val vaultViewModel = LocalVaultViewModel.current

    var revealed by remember { mutableStateOf(false) }

    val currentTimeSeconds by vaultViewModel.currentTimeSeconds.collectAsState()
    val currentCycle = currentTimeSeconds / item.period

    val otpCode = remember(item, currentCycle, revealed) {
        if (item.secret.isEmpty()) return@remember ""

        if (context.configViewModel.values.requireTapToReveal && !revealed) {
            return@remember "•".repeat(item.digits)
        }

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

    val formattedCode = remember(otpCode) {
        if (otpCode.length >= 6) {
            "${otpCode.substring(0, otpCode.length / 2)} ${otpCode.substring(otpCode.length / 2)}"
        } else otpCode
    }

    val shape = RoundedCornerShape(16.dp)

    val lowerModifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .background(
            color = colors.secondColor,
            shape = shape
        )
        .clickable {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            view.playSoundEffect(SoundEffectConstants.CLICK)

            Logger.i("OtpCardLower", "Copied OTP code to clipboard")
            if (revealed) {
                clipboard.nativeClipboard.setPrimaryClip(ClipData.newPlainText("OTP Code", otpCode))
            }

            revealed = !revealed
        }

    Box(
        contentAlignment = Alignment.Center,
        modifier = lowerModifier
    ) {
        Text(
            text = formattedCode,
            fontFamily = InterVariable,
            color = colors.firstColor,
            fontWeight = FontWeight.W700,
            fontSize = if (!revealed && context.configViewModel.values.requireTapToReveal) 56.sp else 48.sp,
            maxLines = 1
        )
    }
}