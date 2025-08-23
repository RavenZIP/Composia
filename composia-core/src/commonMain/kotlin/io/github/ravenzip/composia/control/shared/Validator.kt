package io.github.ravenzip.composia.control.shared

private val emailRegex =
    Regex(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    )

private val phoneRegex =
    Regex("(\\+[0-9]+[\\- .]*)?" + "(\\([0-9]+\\)[\\- .]*)?" + "([0-9][0-9\\- .]+[0-9])")

/** Возможные валидаторы для компонентов */
class Validator {
    companion object {
        val required = { value: String ->
            if (value.isEmpty()) "Поле обязательно для заполнения" else null
        }

        val minLength = { value: String, min: Int ->
            if (value.length < min) "Минимальная длина $min символа" else null
        }

        val maxLength = { value: String, max: Int ->
            if (value.length > max) "Максимальная длина $max символа" else null
        }

        val min = { value: Int, min: Int ->
            if (value < min) "Минимальное допустимое значение $min" else null
        }

        val max = { value: Int, max: Int ->
            if (value > max) "Максимальное допустимое значение $max" else null
        }

        val email = { value: String ->
            if (!emailRegex.matches(value)) "Введен некорректный email" else null
        }

        val phone = { value: String ->
            if (!phoneRegex.matches(value)) "Введен некорректный номер телефона" else null
        }
    }
}
