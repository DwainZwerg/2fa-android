package app.ninesevennine.twofactorauthenticator.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.ninesevennine.twofactorauthenticator.LocalNavController
import app.ninesevennine.twofactorauthenticator.LocalThemeViewModel
import app.ninesevennine.twofactorauthenticator.R
import app.ninesevennine.twofactorauthenticator.features.locale.localizedString
import app.ninesevennine.twofactorauthenticator.ui.elements.WideText
import app.ninesevennine.twofactorauthenticator.ui.elements.WideTitle
import app.ninesevennine.twofactorauthenticator.ui.elements.widebutton.WideButton
import kotlinx.serialization.Serializable

@Serializable
object ExportToGoogleAuthScreenRoute

@Composable
fun ExportToGoogleAuthScreen() {
    val colors = LocalThemeViewModel.current.colors
    val navController = LocalNavController.current

    var qrPage by remember { mutableIntStateOf(1) }

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WideText(
                text = "Only TOTP and HOTP tokens that generate 6-digit or 8-digit codes can be exported to Google Authenticator",
                textAlign = TextAlign.Center
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 32.dp)
                    .aspectRatio(1f)
            ) {
                drawRect(
                    color = Color(0xFF4CAF50),
                    topLeft = Offset(0f, 0f),
                    size = Size(size.width, size.width)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clickable {
                            qrPage--
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = colors.onBackground
                    )
                }

                WideTitle(
                    text = "$qrPage / 2",
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clickable {
                            qrPage++
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = colors.onBackground
                    )
                }
            }
        }

        WideButton(
            label = localizedString(R.string.common_cancel),
            onClick = { navController.popBackStack() }
        )
    }
}