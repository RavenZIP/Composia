package io.github.ravenzip.composia.control.shared

/** Типы изменений значения в контролах */
sealed class ValueChangeType {
    /** Значение при инициализации */
    data object Initialize : ValueChangeType()

    /** Значение при установке */
    data object Set : ValueChangeType()

    /** Значение при сбросе */
    data object Reset : ValueChangeType()
}

fun ValueChangeType.isInitialize() = this is ValueChangeType.Initialize

fun ValueChangeType.isSet() = this is ValueChangeType.Set

fun ValueChangeType.isReset() = this is ValueChangeType.Reset
