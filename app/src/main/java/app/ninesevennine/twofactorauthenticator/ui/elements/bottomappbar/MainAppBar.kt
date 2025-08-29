package app.ninesevennine.twofactorauthenticator.ui.elements.bottomappbar

import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.R
import app.ninesevennine.twofactorauthenticator.configViewModel
import app.ninesevennine.twofactorauthenticator.features.locale.localizedString
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable
import app.ninesevennine.twofactorauthenticator.themeViewModel

@Composable
fun MainAppBar(
    onSettings: () -> Unit,
    onAdd: () -> Unit,
    onAddLongPress: () -> Unit,
    onSearch: (String) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    val colors = context.themeViewModel.colors
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var query by remember { mutableStateOf("") }

    val navBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val imeBottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()

    val isKeyboardOpen = imeBottom > 80.dp
    val bottomPadding = if (isKeyboardOpen) imeBottom else navBottom + 8.dp

    val focusRequester = remember { FocusRequester() }

    if (context.configViewModel.values.enableFocusSearch) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingValues(bottom = bottomPadding)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isKeyboardOpen) Modifier
                    else Modifier.padding(horizontal = 32.dp)
                )
                .widthIn(max = 384.dp)
                .height(64.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(32.dp),
                    clip = false
                )
                .then(
                    if (isKeyboardOpen) Modifier.clip(
                        RoundedCornerShape(
                            topStart = 32.dp,
                            topEnd = 32.dp
                        )
                    )
                    else Modifier.clip(RoundedCornerShape(32.dp))
                )
                .background(colors.primaryContainer),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = query,
                onValueChange = { new ->
                    query = new
                    onSearch(new)
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = InterVariable,
                    fontWeight = FontWeight.W700,
                    fontSize = 18.sp,
                    color = colors.onPrimaryContainer
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = colors.onPrimaryContainer
                    )
                },
                placeholder = {
                    Text(
                        text = localizedString(R.string.common_search_hint),
                        fontFamily = InterVariable,
                        fontWeight = FontWeight.W700,
                        fontSize = 18.sp,
                        color = colors.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colors.onPrimaryContainer,
                    unfocusedTextColor = colors.onPrimaryContainer,
                    disabledTextColor = colors.onPrimaryContainer.copy(alpha = 0.6f),
                    cursorColor = colors.onPrimaryContainer,

                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,

                    focusedLeadingIconColor = colors.onPrimaryContainer,
                    unfocusedLeadingIconColor = colors.onPrimaryContainer,
                    disabledLeadingIconColor = colors.onPrimaryContainer.copy(alpha = 0.6f),

                    focusedPlaceholderColor = colors.onPrimaryContainer.copy(alpha = 0.7f),
                    unfocusedPlaceholderColor = colors.onPrimaryContainer.copy(alpha = 0.7f),
                    disabledPlaceholderColor = colors.onPrimaryContainer.copy(alpha = 0.5f)
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onSearch(query)
                    }
                )
            )

            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(colors.onPrimaryContainer.copy(alpha = 0.1f))
            )

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
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

            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(colors.onPrimaryContainer.copy(alpha = 0.1f))
            )

            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(64.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                onAdd()
                            },
                            onLongPress = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                onAddLongPress()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = colors.onPrimaryContainer
                )
            }
        }
    }
}