package io.github.ravenzip.composia.control.multiValueControl

import kotlinx.coroutines.CoroutineScope

class MultiValueControl<T, K>(
    initialValue: List<T>,
    keySelector: (T) -> K,
    resetValue: List<T> = emptyList(),
    disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) :
    AbstractMultiValueControl<T, K>(
        initialValue = initialValue,
        keySelector = keySelector,
        resetValue = resetValue,
        disabled = disabled,
        coroutineScope = coroutineScope,
    )
