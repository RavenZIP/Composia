package io.github.ravenzip.composia.extension

fun <T, K> MutableList<T>.addOrRemove(value: T, keySelector: (T) -> K): MutableList<T> {
    val valueKey = keySelector(value)
    val existingIndex = this.indexOfFirst { keySelector(it) == valueKey }
    val isExists = existingIndex >= 0

    if (isExists) {
        this.removeAt(existingIndex)
    } else {
        this.add(value)
    }

    return this
}
