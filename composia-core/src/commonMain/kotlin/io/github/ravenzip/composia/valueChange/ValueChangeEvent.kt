package io.github.ravenzip.composia.valueChange

data class ValueChangeEvent<T>(val value: T, val typeChange: ValueChangeType) {
    companion object {
        fun <T> create(value: T, typeChange: ValueChangeType) = ValueChangeEvent(value, typeChange)

        fun <T> createInitializeChange(value: T) =
            ValueChangeEvent(value, ValueChangeType.Initialize)

        fun <T> createSetChange(value: T) = ValueChangeEvent(value, ValueChangeType.Set)

        fun <T> createResetChange(value: T) = ValueChangeEvent(value, ValueChangeType.Reset)
    }
}
