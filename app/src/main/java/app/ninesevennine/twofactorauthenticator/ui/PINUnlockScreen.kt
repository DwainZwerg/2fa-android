package app.ninesevennine.twofactorauthenticator.ui

import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.KeyboardTab
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.LocalNavController
import app.ninesevennine.twofactorauthenticator.configViewModel
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable
import app.ninesevennine.twofactorauthenticator.themeViewModel
import kotlinx.serialization.Serializable

@Serializable
object PINUnlockScreenRoute

@Composable
fun PINUnlockScreen() {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val navController = LocalNavController.current

    var enteredPin by remember { mutableStateOf("") }
    var isConfirmingPin by remember { mutableStateOf(false) }

    val isDisablingPin = context.configViewModel.values.requirePINToUnlock

    if (isDisablingPin) {
        PINInterface("Enter PIN") {
            context.configViewModel.updatePINToUnlock(false)
            context.configViewModel.pinUnlock = ""

            navController.popBackStack()
        }
    } else {
        if (!isConfirmingPin) {
            PINInterface("Enter PIN") { pin ->
                enteredPin = pin
                isConfirmingPin = true
            }
        } else {
            PINInterface("Confirm PIN") { confirmedPin ->
                if (confirmedPin == enteredPin) {
                    context.configViewModel.pinUnlock = confirmedPin
                    context.configViewModel.updatePINToUnlock(true)
                    navController.popBackStack()
                } else {
                    enteredPin = ""
                    isConfirmingPin = false

                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                }
            }
        }
    }
}

@Composable
fun PINInterface(
    label: String,
    onEnter: (String) -> Unit
) {
    val context = LocalContext.current
    val colors = context.themeViewModel.colors
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current

    var secret by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 64.dp
            ),
            text = label,
            fontFamily = InterVariable,
            color = colors.onBackground,
            fontWeight = FontWeight.W700,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = "•".repeat(secret.length),
            fontFamily = InterVariable,
            color = colors.onBackground,
            fontWeight = FontWeight.W700,
            fontSize = 48.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        Column(
            modifier = Modifier.padding(bottom = 112.dp),
        ) {
            for (row in 0 until 4) {
                Row {
                    for (column in 0 until 3) {
                        val key = when {
                            row < 3 -> row * 3 + column + 1
                            else -> 0
                        }

                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(86.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    when {
                                        row == 3 && column == 0 -> {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            view.playSoundEffect(SoundEffectConstants.CLICK)

                                            if (secret.isNotEmpty()) {
                                                secret = secret.dropLast(1)
                                            }
                                        }
                                        row == 3 && column == 2 -> {
                                            view.playSoundEffect(SoundEffectConstants.CLICK)

                                            if (secret.length >= 4) {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                                onEnter(secret)
                                            } else {
                                                haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                                secret = ""
                                            }
                                        }
                                        else -> {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            view.playSoundEffect(SoundEffectConstants.CLICK)

                                            secret += key.toString()
                                        }
                                    }
                                }
                                .clip(RoundedCornerShape(48.dp))
                                .background(colors.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                row == 3 && column == 0 -> {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp),
                                        tint = colors.onBackground
                                    )
                                }

                                row == 3 && column == 2 -> {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.KeyboardTab,
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp),
                                        tint = colors.onBackground
                                    )
                                }

                                else -> {
                                    Text(
                                        text = key.toString(),
                                        fontFamily = InterVariable,
                                        color = colors.onBackground,
                                        fontWeight = FontWeight.W700,
                                        fontSize = 28.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}