package io.github.ravenzip.composia.validation

import kotlinx.coroutines.flow.StateFlow

/** Интерфейс, описывающий базовый контракт для сущностей, которым необходима проверка значения. */
interface Validatable<T> {
    /** Является ли текущее значение валидным. */
    val isValid: Boolean

    /** Является ли текущее значение невалидным. */
    val isInvalid: Boolean

    /** Текст ошибки, если значение невалидно, иначе `null`. */
    val errorMessage: String?

    /** Поток, эмитирующий состояние валидности (`true`, если значение валидно). */
    val isValidFlow: StateFlow<Boolean>

    /** Поток, эмитирующий состояние невалидности (`true`, если значение невалидно). */
    val isInvalidFlow: StateFlow<Boolean>

    /** Поток сообщений об ошибке. Эмитирует `null`, если значение валидно, либо текст ошибки. */
    val errorMessageFlow: StateFlow<String?>
}
