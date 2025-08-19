package app.ninesevennine.twofactorauthenticator.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.LocalThemeViewModel
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable

@Composable
fun WideText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Unspecified
) {
    val colors = LocalThemeViewModel.current.colors

    Text(
        modifier = modifier.padding(vertical = 8.dp, horizontal = 32.dp),
        text = text,
        fontFamily = InterVariable,
        color = if (color == Color.Unspecified) colors.onBackground else color,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
}