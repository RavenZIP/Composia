package io.github.ravenzip.composia.valueChange

/** Класс, представляющий значение и тип изменения */
data class ValueChange<T>(val value: T, val typeChange: ValueChangeType) {
    companion object {
        /** Создание нового объекта с заданным значением и типом изменения */
        fun <T> create(value: T, typeChange: ValueChangeType): ValueChange<T> =
            ValueChange(value, typeChange)

        /**
         * Создание нового объекта с заданным значением и типом изменения
         * [ValueChangeType.Initialize]
         */
        fun <T> createInitializeChange(value: T): ValueChange<T> =
            ValueChange(value, ValueChangeType.Initialize)

        /** Создание нового объекта с заданным значением и типом изменения [ValueChangeType.Set] */
        fun <T> createSetChange(value: T): ValueChange<T> = ValueChange(value, ValueChangeType.Set)

        /**
         * Создание нового объекта с заданным значением и типом изменения [ValueChangeType.Reset]
         */
        fun <T> createResetChange(value: T): ValueChange<T> =
            ValueChange(value, ValueChangeType.Reset)
    }
}
