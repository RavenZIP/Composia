package io.github.ravenzip.composia.components.textField.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import io.github.ravenzip.composia.components.text.CounterLabel
import io.github.ravenzip.composia.components.text.HintText
import io.github.ravenzip.composia.control.validatable.ValidatableControl
import io.github.ravenzip.composia.control.value.ValueControl
import io.github.ravenzip.composia.state.TextFieldState
import io.github.ravenzip.composia.valueChange.ValueChangeType
import kotlinx.coroutines.flow.filter

/** Обертка над текстовыми полями, которые используют контролы напрямую */
@Composable
internal fun <T> resetReadonlyStateOnResetValue(
    control: ValidatableControl<T>,
    state: TextFieldState,
) {
    LaunchedEffect(control, state) {
        control.typeChangeFlow
            .filter { type -> type is ValueChangeType.Reset }
            .collect { state.readonlyState.setValue(state.readonlyState.initialValue) }
    }
}

@Composable
internal fun <T> resetReadonlyStateOnResetValue(control: ValueControl<T>, state: TextFieldState) {
    LaunchedEffect(control, state) {
        control.typeChangeFlow
            .filter { type -> type is ValueChangeType.Reset }
            .collect { state.readonlyState.setValue(state.readonlyState.initialValue) }
    }
}

/** Сообщение с описанием ошибки + счетчик введенных символов */
@Composable
internal fun ErrorMessageWithSymbolsCounter(
    errorMessage: String,
    isFocused: Boolean,
    showTextLengthCounter: Boolean,
    maxLength: Int?,
    currentLength: Int,
    colors: TextFieldColors,
) {
    val isInvalid = errorMessage.isNotBlank()

    if (isInvalid || showTextLengthCounter) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            ErrorMessage(
                modifier = Modifier.weight(1f),
                isInvalid = isInvalid,
                errorMessage = errorMessage,
                color = colors.errorLabelColor,
            )

            if (showTextLengthCounter) {
                CounterLabel(
                    modifier = Modifier.weight(1f),
                    current = currentLength,
                    max = maxLength,
                    textAlign = TextAlign.End,
                    color = colors.calculateLabelColor(isInvalid = isInvalid, isFocused = isFocused),
                )
            }
        }
    }
}

/** Сообщение с описанием ошибки */
@Composable
internal fun ErrorMessage(
    modifier: Modifier = Modifier,
    isInvalid: Boolean,
    errorMessage: String,
    color: Color,
) {
    if (isInvalid) {
        HintText(text = errorMessage, modifier = modifier, color = color)
    }
}

internal fun TextFieldColors.calculateLabelColor(
    customIndicatorColor: Color? = null,
    isInvalid: Boolean,
    isFocused: Boolean,
): Color =
    when {
        isInvalid -> this.errorLabelColor
        isFocused -> customIndicatorColor ?: this.focusedIndicatorColor
        else -> this.unfocusedIndicatorColor
    }

internal fun acceptInput(currentLength: Int, maxLength: Int?): Boolean {
    return maxLength == null || currentLength < maxLength
}
