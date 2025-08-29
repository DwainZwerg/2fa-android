package app.ninesevennine.twofactorauthenticator.ui.elements.textfields

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable
import app.ninesevennine.twofactorauthenticator.themeViewModel

@Composable
fun SingleLineTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "",
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Words,
        autoCorrectEnabled = true,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    )
) {
    val context = LocalContext.current
    val colors = context.themeViewModel.colors

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
        isError = isError,
        keyboardOptions = keyboardOptions,
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