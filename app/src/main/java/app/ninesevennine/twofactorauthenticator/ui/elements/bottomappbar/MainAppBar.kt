package app.ninesevennine.twofactorauthenticator.ui.elements.bottomappbar

import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.LocalThemeViewModel
import app.ninesevennine.twofactorauthenticator.R
import app.ninesevennine.twofactorauthenticator.features.locale.localizedString
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable

@Composable
fun MainAppBar(
    onSettings: () -> Unit,
    onAdd: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    val colors = LocalThemeViewModel.current.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .widthIn(max = 384.dp)
                .shadow(
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
                    .weight(1f)
                    .clip(RoundedCornerShape(
                        topStart = 32.dp, bottomStart = 32.dp,
                        topEnd = 8.dp, bottomEnd = 8.dp
                    ))
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 16.dp).size(32.dp),
                        tint = colors.onPrimaryContainer
                    )

                    Text(
                        text = localizedString(R.string.main_search),
                        fontFamily = InterVariable,
                        color = colors.onPrimaryContainer,
                        fontWeight = FontWeight.W700,
                        fontSize = 18.sp,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier = Modifier
                    .height(64.dp)
                    .width(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.primaryContainer)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        view.playSoundEffect(SoundEffectConstants.CLICK)

                        onSettings()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
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
                    .background(colors.primaryContainer)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        view.playSoundEffect(SoundEffectConstants.CLICK)

                        onAdd()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 12.dp, end = 16.dp).size(40.dp),
                    tint = colors.onPrimaryContainer
                )
            }
        }
    }
}