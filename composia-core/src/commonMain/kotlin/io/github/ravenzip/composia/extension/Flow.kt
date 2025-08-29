package io.github.ravenzip.composia.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

internal fun <T> Flow<T>.stateInDefault(scope: CoroutineScope, initialValue: T) =
    this.stateIn(scope, SharingStarted.WhileSubscribed(5000), initialValue)
