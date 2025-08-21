package app.ninesevennine.twofactorauthenticator.features.vault

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.ninesevennine.twofactorauthenticator.LocalVaultViewModel
import app.ninesevennine.twofactorauthenticator.ui.elements.otpcard.OtpCard
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun VaultView() {
    val vaultViewModel = LocalVaultViewModel.current
    val items = vaultViewModel.items

    val lazyGridState = rememberLazyGridState()

    val reorderState = rememberReorderableLazyGridState(
        lazyGridState = lazyGridState,
        onMove = { from, to -> vaultViewModel.moveItem(from.index, to.index) }
    )

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(1),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
            bottom = 192.dp
        ),
        horizontalArrangement = Arrangement.Center
    ) {
        @OptIn(ExperimentalUuidApi::class)
        itemsIndexed(items, key = { _, item -> item.uuid }) { index, item ->
            ReorderableItem(reorderState, key = item.uuid) { dragging ->
                OtpCard(
                    modifier = Modifier.longPressDraggableHandle(),
                    item = item,
                    dragging = dragging
                )
            }
        }
    }
}