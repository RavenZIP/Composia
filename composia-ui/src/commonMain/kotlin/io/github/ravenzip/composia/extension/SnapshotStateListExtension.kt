package io.github.ravenzip.composia.extension

import androidx.compose.runtime.snapshots.SnapshotStateList

internal fun <T> SnapshotStateList<T>.update(elements: Collection<T>) {
    this.clear()
    this.addAll(elements)
}
