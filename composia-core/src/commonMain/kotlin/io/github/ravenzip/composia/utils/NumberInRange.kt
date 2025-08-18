package io.github.ravenzip.composia.utils

/** Проверка на то, что число входит в указанный диапазон */
fun numberInRange(value: Int, min: Int = 0, max: Int = 0): Boolean {
    if (min > max) {
        throw IllegalArgumentException(
            "Указанное минимальное число превышает указанное максимально допустимое"
        )
    }

    if (min == max) {
        throw IllegalArgumentException(
            "Указанное минимальное число равно указанному максимально допустимому"
        )
    }

    return value in min..max
}
