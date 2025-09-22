package io.github.ravenzip.composia.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal fun <T> Flow<T>.stateInWhileSubscribed(
    scope: CoroutineScope,
    initialValue: T,
    stopTimeoutMillis: Long = 5000,
): StateFlow<T> {
    return stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis),
        initialValue = initialValue,
    )
}

@Composable
fun <T> Flow<List<T>>.collectAsSnapshotStateList(
    initialValue: List<T> = emptyList()
): SnapshotStateList<T> {
    val result = remember { mutableStateListOf<T>().apply { addAll(initialValue) } }

    LaunchedEffect(initialValue, this) {
        this@collectAsSnapshotStateList.collect { x ->
            result.clear()
            result.addAll(x)
        }
    }

    return result
}

@Composable
fun <T> StateFlow<List<T>>.collectAsSnapshotStateList(): SnapshotStateList<T> {
    return collectAsSnapshotStateList(this.value)
}
