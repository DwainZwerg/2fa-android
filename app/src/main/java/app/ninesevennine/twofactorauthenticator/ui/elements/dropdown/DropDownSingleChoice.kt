package app.ninesevennine.twofactorauthenticator.ui.elements.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable
import app.ninesevennine.twofactorauthenticator.themeViewModel

@Composable
fun <T> DropDownSingleChoice(
    modifier: Modifier = Modifier,
    options: Array<T>,
    selectedOption: T,
    onSelectionChange: (T) -> Unit,
    getDisplayText: (T) -> String
) {
    val context = LocalContext.current
    val colors = context.themeViewModel.colors

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(64.dp)
            .border(
                width = 1.dp,
                color = colors.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                expanded = !expanded
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getDisplayText(selectedOption),
                modifier = Modifier.weight(1f),
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                fontFamily = InterVariable,
                color = colors.onBackground,
                maxLines = 1
            )

            Spacer(Modifier.width(8.dp))

            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = colors.onBackground
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(color = colors.background)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = getDisplayText(option),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W700,
                            fontFamily = InterVariable,
                            color = colors.onBackground
                        )
                    },
                    onClick = {
                        onSelectionChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}