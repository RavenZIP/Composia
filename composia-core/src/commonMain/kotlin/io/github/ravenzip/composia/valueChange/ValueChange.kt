package io.github.ravenzip.composia.valueChange

data class ValueChange<T>(val value: T, val typeChange: ValueChangeType) {
    companion object {
        fun <T> create(value: T, typeChange: ValueChangeType): ValueChange<T> =
            ValueChange(value, typeChange)

        fun <T> createInitializeChange(value: T): ValueChange<T> =
            ValueChange(value, ValueChangeType.Initialize)

        fun <T> createSetChange(value: T): ValueChange<T> = ValueChange(value, ValueChangeType.Set)

        fun <T> createResetChange(value: T): ValueChange<T> =
            ValueChange(value, ValueChangeType.Reset)
    }
}
