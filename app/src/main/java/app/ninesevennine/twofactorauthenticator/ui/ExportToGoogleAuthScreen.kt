package app.ninesevennine.twofactorauthenticator.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.LocalNavController
import app.ninesevennine.twofactorauthenticator.LocalThemeViewModel
import app.ninesevennine.twofactorauthenticator.LocalVaultViewModel
import app.ninesevennine.twofactorauthenticator.R
import app.ninesevennine.twofactorauthenticator.features.locale.localizedString
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable
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
    val vaultViewModel = LocalVaultViewModel.current

    val qrBitmaps by remember { mutableStateOf(vaultViewModel.exportToGoogleAuth()) }
    val qrPages by remember { mutableIntStateOf(qrBitmaps.size) }
    var qrIndex by remember { mutableIntStateOf(0) }

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
            Icon(
                painter = painterResource(R.drawable.icon_google_authenticator),
                contentDescription = null,
                modifier = Modifier.size(192.dp),
                tint = Color.Unspecified
            )

            if (qrBitmaps.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 32.dp)
                            .aspectRatio(1f)
                    ) {
                        drawRect(
                            color = colors.error,
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.width)
                        )
                    }

                    Text(
                        text = "?",
                        fontFamily = InterVariable,
                        color = colors.onError,
                        fontWeight = FontWeight.W700,
                        fontSize = 96.sp,
                    )
                }
            } else {
                Image(
                    painter = BitmapPainter(
                        qrBitmaps[qrIndex].asImageBitmap(),
                        filterQuality = FilterQuality.None
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 32.dp)
                        .aspectRatio(1f)
                )
            }

            WideText(
                text = "Only TOTP and HOTP tokens that generate 6-digit or 8-digit codes with 30-second intervals can be exported to Google Authenticator",
                textAlign = TextAlign.Center
            )

            if (qrBitmaps.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clickable {
                                qrIndex = (qrIndex - 1).coerceAtLeast(0)
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
                        text = "${qrIndex + 1} / $qrPages",
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clickable {
                                qrIndex = (qrIndex + 1).coerceAtMost(qrPages - 1)
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
        }

        WideButton(
            label = localizedString(R.string.common_cancel),
            onClick = { navController.popBackStack() }
        )
    }
}