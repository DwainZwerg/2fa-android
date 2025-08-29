package app.ninesevennine.twofactorauthenticator.ui.elements.textfields

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable
import app.ninesevennine.twofactorauthenticator.themeViewModel

@Composable
fun TextField2fa(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "",
    isError: Boolean = false
) {
    val context = LocalContext.current
    val colors = context.themeViewModel.colors

    var revealed by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(64.dp),
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.W700,
            fontFamily = InterVariable,
            color = colors.onBackground
        ),
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = InterVariable,
                color = colors.onBackground
            )
        },
        trailingIcon = {
            IconButton(onClick = { revealed = !revealed }) {
                Icon(
                    imageVector = if (revealed) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null,
                    tint = colors.onBackground
                )
            }
        },
        isError = isError,
        visualTransformation = if (revealed) VisualTransformation.None else HiddenTextTransformation(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.primaryContainer,
            unfocusedBorderColor = colors.primaryContainer,
            focusedTextColor = colors.onBackground,
            unfocusedTextColor = colors.onBackground
        )
    )
}

private class HiddenTextTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return minOf(offset, 16)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return minOf(offset, text.length)
            }
        }

        return TransformedText(
            text = AnnotatedString("•".repeat(16)),
            offsetMapping = offsetMapping
        )
    }
}