package app.ninesevennine.twofactorauthenticator.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ninesevennine.twofactorauthenticator.LocalThemeViewModel
import app.ninesevennine.twofactorauthenticator.features.theme.InterVariable
import app.ninesevennine.twofactorauthenticator.ui.elements.bottomappbar.LogAppBar
import app.ninesevennine.twofactorauthenticator.utils.Logger
import kotlinx.serialization.Serializable

@Serializable
object LogScreenRoute

@Composable
fun LogScreen() {
    val context = LocalContext.current
    val colors = LocalThemeViewModel.current.colors

    val logLines by remember {
        derivedStateOf {
            Logger.getFullLog()
                .split('\n')
                .takeLast(500)
        }
    }

    val listState = rememberLazyListState()
    val horizontalScrollState = rememberScrollState()

    LaunchedEffect(logLines.size) {
        if (logLines.isNotEmpty()) {
            listState.scrollToItem(logLines.size + 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .horizontalScroll(horizontalScrollState)
            .clipToBounds(),
        contentPadding = PaddingValues(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
            bottom = 192.dp
        ),
        userScrollEnabled = true
    ) {
        items(
            count = logLines.size,
            key = { index -> index }
        ) { index ->
            Text(
                text = logLines[index],
                modifier = Modifier.fillMaxWidth(),
                color = colors.onBackground,
                fontSize = 16.sp,
                fontFamily = InterVariable,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                maxLines = 1
            )
        }

        item {
            Text(
                text = "Save to view the full log",
                modifier = Modifier.fillMaxWidth(),
                color = colors.onBackground,
                fontSize = 16.sp,
                fontFamily = InterVariable,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                maxLines = 1
            )
        }
    }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = CreateDocument("text/plain"),
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val logContent = Logger.getFullLog()
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(logContent.toByteArray())
                }
            } catch (e: Exception) {
                Logger.e("LogScreen", "Error saving log file: ${e.message}")
            }
        }
    }

    LogAppBar {
        Logger.i("LogScreen", "onShare")

        createDocumentLauncher.launch("debug_log")
    }
}