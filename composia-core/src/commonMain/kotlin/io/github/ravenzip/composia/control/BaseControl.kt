package io.github.ravenzip.composia.control

import kotlinx.coroutines.CoroutineScope

class BaseControl(disabled: Boolean = false, coroutineScope: CoroutineScope) :
    AbstractControl(disabled, coroutineScope)
