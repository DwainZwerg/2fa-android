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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable
import app.ninesevennine.twofactorauthenticator.utils.Logger

@Composable
fun OtpCardLower(
    modifier: Modifier = Modifier,
    colors: OtpCardPalette,
    code: String
) {
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    val clipboard = LocalClipboard.current

    val formattedCode = remember(code) {
        if (code.length >= 6) {
            buildString(code.length + 1) {
                append(code, 0, code.length / 2)
                append(' ')
                append(code, code.length / 2, code.length)
            }
        } else {
            code
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                color = colors.secondColor,
                shape = RoundedCornerShape(16.dp)
            ).clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                view.playSoundEffect(SoundEffectConstants.CLICK)

                Logger.i("OtpCardLower", "Copied OTP code to clipboard")
                clipboard.nativeClipboard.setPrimaryClip(ClipData.newPlainText("OTP Code", code))
            }
    ) {
        Text(
            text = formattedCode,
            fontFamily = InterVariable,
            color = colors.firstColor,
            fontWeight = FontWeight.W700,
            fontSize = 48.sp,
            maxLines = 1
        )
    }
}