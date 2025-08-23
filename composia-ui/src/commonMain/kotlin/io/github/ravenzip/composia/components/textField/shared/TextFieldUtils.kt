package io.github.ravenzip.composia.components.textField.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.ValueChangeType
import io.github.ravenzip.composia.components.text.CounterLabel
import io.github.ravenzip.composia.components.text.HintText
import io.github.ravenzip.composia.control.formControl.CompositeControl
import io.github.ravenzip.composia.control.valueControl.ValueControl
import io.github.ravenzip.composia.state.TextFieldState
import kotlinx.coroutines.flow.filter

/** Обертка над текстовыми полями, которые используют контролы напрямую */
@Composable
internal fun <T> TextFieldWrapper(
    control: CompositeControl<T>,
    state: TextFieldState,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(control, state) {
        control.valueWithTypeChangesFlow
            .filter { x -> x.typeChange is ValueChangeType.Reset }
            .collect { state.setReadonly(state.readonly) }
    }

    content()
}

@Composable
internal fun <T> TextFieldWrapper(
    control: ValueControl<T>,
    state: TextFieldState,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(control, state) {
        control.valueWithTypeChangesFlow
            .filter { x -> x.typeChange is ValueChangeType.Reset }
            .collect { state.setReadonly(state.readonly) }
    }

    content()
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
                modifier = Modifier.weight(1f).padding(start = 10.dp),
                isInvalid = isInvalid,
                errorMessage = errorMessage,
                color = colors.errorLabelColor,
            )

            if (showTextLengthCounter) {
                CounterLabel(
                    modifier = Modifier.weight(1f).padding(end = 10.dp),
                    current = currentLength,
                    max = maxLength,
                    textAlign = TextAlign.End,
                    color = colors.calculateLabelColor(isInvalid, isFocused),
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

internal fun TextFieldColors.calculateLabelColor(isError: Boolean, isFocused: Boolean): Color =
    when {
        isError -> this.errorLabelColor
        isFocused -> this.focusedIndicatorColor
        else -> this.unfocusedIndicatorColor
    }

internal fun acceptInput(currentLength: Int, maxLength: Int?): Boolean {
    return maxLength == null || currentLength < maxLength
}
