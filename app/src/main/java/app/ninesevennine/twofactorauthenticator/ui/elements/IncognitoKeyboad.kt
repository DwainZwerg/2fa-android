package app.ninesevennine.twofactorauthenticator.ui.elements

import android.view.inputmethod.EditorInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.PlatformTextInputMethodRequest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UseIncognitoKeyboard(content: (@Composable () -> Unit)) {
    InterceptPlatformTextInput(
        interceptor = { request, nextHandler ->
            val modifiedRequest = PlatformTextInputMethodRequest { outAttributes ->
                request.createInputConnection(outAttributes).also {
                    addNoPersonalizedLearning(outAttributes)
                }
            }
            nextHandler.startInputMethod(modifiedRequest)
        }
    ) {
        content()
    }
}

private fun addNoPersonalizedLearning(info: EditorInfo) {
    info.imeOptions = info.imeOptions or EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING
}