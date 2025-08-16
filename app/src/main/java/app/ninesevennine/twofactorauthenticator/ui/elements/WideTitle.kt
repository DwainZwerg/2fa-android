package app.ninesevennine.twofactorauthenticator.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.LocalThemeViewModel
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable

@Composable
fun WideTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    val themeColors = LocalThemeViewModel.current.colors

    Text(
        modifier = modifier.padding(vertical = 8.dp, horizontal = 32.dp),
        text = text,
        fontFamily = InterVariable,
        color = themeColors.onBackground,
        fontWeight = FontWeight.W700,
        fontSize = 20.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}