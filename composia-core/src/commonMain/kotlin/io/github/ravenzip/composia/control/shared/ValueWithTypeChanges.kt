package io.github.ravenzip.composia.control.shared

data class ValueWithTypeChanges<T>(val value: T, val typeChange: ValueChangeType) {
    companion object {
        fun <T> createInitializeChange(value: T) =
            ValueWithTypeChanges(value, ValueChangeType.Initialize)

        fun <T> createSetChange(value: T) = ValueWithTypeChanges(value, ValueChangeType.Set)

        fun <T> createResetChange(value: T) = ValueWithTypeChanges(value, ValueChangeType.Reset)
    }
}
