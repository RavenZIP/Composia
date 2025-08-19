package io.github.ravenzip.composia.control

import kotlinx.coroutines.CoroutineScope

class StatusControl(disabled: Boolean = false, coroutineScope: CoroutineScope) :
    AbstractStatusControl(disabled, coroutineScope)
