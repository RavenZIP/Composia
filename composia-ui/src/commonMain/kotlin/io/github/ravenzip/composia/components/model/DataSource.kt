package io.github.ravenzip.composia.components.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.Flow

sealed class DataSource<T> {
    class Predefined<T>(val items: SnapshotStateList<T>) : DataSource<T>()

    class ByQuery<T>(val query: (String) -> Flow<SnapshotStateList<T>>) : DataSource<T>()
}
