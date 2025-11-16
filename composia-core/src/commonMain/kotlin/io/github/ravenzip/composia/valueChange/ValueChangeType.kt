package io.github.ravenzip.composia.valueChange

/** Типы изменений значения в контролах */
sealed class ValueChangeType {
    /** Значение при инициализации */
    data object Initialize : ValueChangeType()

    /** Значение при установке */
    data object Set : ValueChangeType()

    /** Значение при сбросе */
    data object Reset : ValueChangeType()
}

/** Вернет true, если текущий тип изменений [ValueChangeType.Initialize] */
fun ValueChangeType.isInitialize(): Boolean = this is ValueChangeType.Initialize

/** Вернет true, если текущий тип изменений [ValueChangeType.Set] */
fun ValueChangeType.isSet(): Boolean = this is ValueChangeType.Set

/** Вернет true, если текущий тип изменений [ValueChangeType.Reset] */
fun ValueChangeType.isReset(): Boolean = this is ValueChangeType.Reset
