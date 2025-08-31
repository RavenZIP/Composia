package io.github.ravenzip.composia.function

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> Flow<List<T>>.collectAsSnapshotListState(
    initialValue: List<T> = emptyList()
): SnapshotStateList<T> {
    val result = remember { mutableStateListOf<T>().apply { addAll(initialValue) } }

    LaunchedEffect(initialValue, this) {
        this@collectAsSnapshotListState.collect { x ->
            result.clear()
            result.addAll(x)
        }
    }

    return result
}
