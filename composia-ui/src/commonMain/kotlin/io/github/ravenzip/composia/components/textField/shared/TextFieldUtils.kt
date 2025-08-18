package io.github.ravenzip.composia.components.textField.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ravenzip.composia.ControlStatus
import io.github.ravenzip.composia.ValueChangeType
import io.github.ravenzip.composia.control.Control
import io.github.ravenzip.composia.state.TextFieldState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter

/** Обертка над текстовыми полями */
@Composable
internal fun <T> TextFieldWrapper(
    control: Control<T>,
    state: TextFieldState,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(control, state) {
        control.valueWithTypeChanges
            .filter { x -> x.typeChange is ValueChangeType.Reset }
            .collect { state.setReadonly(state.readonly) }
    }

    content()
}

/** Сообщение с описанием ошибки + счетчик введенных символов */
@Composable
internal fun ErrorMessageWithSymbolsCounter(
    controlStatusFlow: StateFlow<ControlStatus>,
    isFocusedFlow: StateFlow<Boolean>,
    showTextLengthCounter: Boolean,
    maxLength: Int,
    currentLength: Int,
    colors: TextFieldColors,
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        val controlStatus = controlStatusFlow.collectAsState().value
        val isInvalid = controlStatus is ControlStatus.Invalid

        if (isInvalid) {
            // TODO переименовать, содержимое функции никак не ограничивает нас использовать ее
            // TODO только для вывода ошибки, здесь может быть любой текст
            ErrorMessage(
                modifier = Modifier.weight(1f),
                errorMessage = controlStatus.message,
                color = colors.errorLabelColor,
            )
        }

        if (showTextLengthCounter) {
            val isFocused = isFocusedFlow.collectAsState().value
            val color =
                remember(isInvalid, isFocused) { colors.calculateLabelColor(isInvalid, isFocused) }

            SymbolsCounter(
                modifier = Modifier.weight(1f),
                maxLength = maxLength,
                currentLength = currentLength,
                color = color,
            )
        }
    }
}

/** Сообщение с описанием ошибки */
@Composable
internal fun ErrorMessage(modifier: Modifier = Modifier, errorMessage: String, color: Color) {
    Text(
        text = errorMessage,
        modifier = modifier.padding(start = 10.dp),
        color = color,
        fontSize = 12.sp,
    )
}

/** Счетчик введенных символов */
@Composable
internal fun SymbolsCounter(
    modifier: Modifier = Modifier,
    maxLength: Int,
    currentLength: Int,
    color: Color,
) {
    Text(
        text = if (maxLength > 0) "$currentLength / $maxLength" else "$currentLength",
        modifier = modifier.padding(end = 10.dp),
        color = color,
        fontSize = 12.sp,
        textAlign = TextAlign.End,
    )
}

internal fun TextFieldColors.calculateLabelColor(isError: Boolean, isFocused: Boolean): Color =
    when {
        isError -> this.errorLabelColor
        isFocused -> this.focusedIndicatorColor
        else -> this.unfocusedIndicatorColor
    }
