package io.github.ravenzip.composia.validation

import io.github.ravenzip.composia.status.ControlStatus

/** Класс, описывающий состояние валидации */
sealed class ValidationState {
    /** Значение валидно */
    object Valid : ValidationState()

    /** Значение невалидно. Содержит текст ошибки */
    class Invalid(val errorMessage: String) : ValidationState()
}

/** Вернет true, если текущее состояние - это [ValidationState.Valid] */
fun ValidationState.isValid(): Boolean = this is ValidationState.Valid

/** Вернет true, если текущее состояние - это [ValidationState.Invalid] */
fun ValidationState.isInvalid(): Boolean = this is ValidationState.Invalid

/**
 * Получить текст ошибки, если состояние [ValidationState.Invalid] или null, если
 * [ValidationState.Valid]
 */
fun ValidationState.getErrorMessage(): String? =
    if (this is ValidationState.Invalid) this.errorMessage else null

/** Мапинг состояния валидации в статус контрола */
fun ValidationState.toControlStatus() =
    when (this) {
        ValidationState.Valid -> ControlStatus.Valid
        is ValidationState.Invalid -> ControlStatus.Invalid(this.errorMessage)
    }
