package app.ninesevennine.twofactorauthenticator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.ninesevennine.twofactorauthenticator.LocalNavController
import app.ninesevennine.twofactorauthenticator.ui.elements.bottomappbar.MainAppBar
import app.ninesevennine.twofactorauthenticator.ui.elements.otpcard.OtpCard
import app.ninesevennine.twofactorauthenticator.utils.Constants
import app.ninesevennine.twofactorauthenticator.vaultViewModel
import kotlinx.serialization.Serializable
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState
import kotlin.uuid.ExperimentalUuidApi

@Serializable
object MainScreenRoute

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val vaultViewModel = context.vaultViewModel

    val items = vaultViewModel.items

    var query by remember { mutableStateOf("") }

    val filteredItems by remember(items, query) {
        derivedStateOf {
            val q = query.trim()
            if (q.isEmpty()) {
                items
            } else {
                items.filter {
                    it.name.lowercase().contains(q) || it.issuer.lowercase().contains(q)
                }
            }
        }
    }

    val lazyGridState = rememberLazyGridState()

    val reorderState = rememberReorderableLazyGridState(
        lazyGridState = lazyGridState,
        onMove = { from, to -> vaultViewModel.moveItem(from.index, to.index) }
    )

    val isFiltering = query.trim().isNotEmpty()

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(1),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
            bottom = 448.dp
        ),
        horizontalArrangement = Arrangement.Center
    ) {
        @OptIn(ExperimentalUuidApi::class)
        itemsIndexed(filteredItems, key = { _, item -> item.uuid }) { index, item ->
            ReorderableItem(reorderState, key = item.uuid) { dragging ->
                OtpCard(
                    modifier = if (isFiltering) Modifier else Modifier.longPressDraggableHandle(),
                    item = item,
                    dragging = dragging
                )
            }
        }
    }

    MainAppBar(
        onSettings = {
            navController.navigate(SettingsScreenRoute)
        },
        onAdd = {
            navController.navigate(EditScreenRoute(Constants.NILUUIDSTR))
        },
        onAddLongPress = {
            navController.navigate(EditScreenRoute(Constants.ONEUUIDSTR))
        },
        onSearch = { q ->
            query = q.lowercase()
        }
    )
}