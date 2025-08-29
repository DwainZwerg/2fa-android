package app.ninesevennine.twofactorauthenticator.ui.elements.bottomappbar

import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.LocalNavController
import app.ninesevennine.twofactorauthenticator.R
import app.ninesevennine.twofactorauthenticator.features.locale.localizedString
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable
import app.ninesevennine.twofactorauthenticator.themeViewModel

@Composable
fun SettingsAppBar() {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    val colors = context.themeViewModel.colors
    val navController = LocalNavController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                WindowInsets.navigationBars.asPaddingValues().let { insets ->
                    PaddingValues(bottom = insets.calculateBottomPadding() + 8.dp)
                }),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(32.dp),
                clip = false
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(64.dp)
                    .clip(RoundedCornerShape(
                        topStart = 32.dp, bottomStart = 32.dp,
                        topEnd = 8.dp, bottomEnd = 8.dp
                    ))
                    .background(colors.primaryContainer)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        view.playSoundEffect(SoundEffectConstants.CLICK)

                        navController.popBackStack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 16.dp, end = 12.dp).size(32.dp),
                    tint = colors.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier = Modifier
                    .height(64.dp)
                    .clip(RoundedCornerShape(
                        topStart = 8.dp, bottomStart = 8.dp,
                        topEnd = 32.dp, bottomEnd = 32.dp
                    ))
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = localizedString(R.string.settings_title),
                    modifier = Modifier.padding(start = 16.dp, end = 32.dp),
                    fontFamily = InterVariable,
                    color = colors.onPrimaryContainer,
                    fontWeight = FontWeight.W700,
                    fontSize = 18.sp,
                    maxLines = 1
                )
            }
        }
    }
}