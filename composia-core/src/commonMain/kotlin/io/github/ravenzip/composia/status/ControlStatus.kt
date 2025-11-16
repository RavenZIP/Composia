package io.github.ravenzip.composia.status

/** Предоставляет возможные статусы контролов */
sealed class ControlStatus {
    /** Сообщает, что элемент управления выключен и не участвует в валидации */
    object Disabled : ControlStatus()

    /** Сообщает, что элемент управления включен и не прошел валидацию */
    data class Invalid(val message: String) : ControlStatus()

    /** Сообщает, что элемент управления включен и прошел валидацию */
    object Valid : ControlStatus()
}

/** Вернет true, если текущий статус [ControlStatus.Valid] или [ControlStatus.Invalid] */
fun ControlStatus.isEnabled(): Boolean = this !is ControlStatus.Disabled

/** Вернет true, если текущий статус [ControlStatus.Disabled] */
fun ControlStatus.isDisabled(): Boolean = this is ControlStatus.Disabled

/** Вернет true, если текущий статус [ControlStatus.Invalid] */
fun ControlStatus.isInvalid(): Boolean = this is ControlStatus.Invalid

/** Вернет true, если текущий статус [ControlStatus.Valid] */
fun ControlStatus.isValid(): Boolean = this is ControlStatus.Valid
