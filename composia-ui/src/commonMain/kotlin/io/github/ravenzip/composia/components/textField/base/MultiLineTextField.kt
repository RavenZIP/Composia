package io.github.ravenzip.composia.components.textField.base

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.components.textField.shared.ResetReadonlyStateOnResetValue
import io.github.ravenzip.composia.control.compositeControl.CompositeControl
import io.github.ravenzip.composia.state.TextFieldState

@Composable
fun MultiLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean = true,
    isReadonly: Boolean = false,
    isInvalid: Boolean = false,
    errorMessage: String = "",
    isFocused: Boolean = false,
    onFocusChange: (FocusState) -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    maxLength: Int? = null,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = RoundedCornerShape(14.dp),
    colors: TextFieldColors = TextFieldDefaults.colors(),
    showTextLengthCounter: Boolean = false,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        isEnabled = isEnabled,
        isReadonly = isReadonly,
        isFocused = isFocused,
        onFocusChange = onFocusChange,
        errorMessage = errorMessage,
        maxLength = maxLength,
        maxLines = maxLines,
        minLines = minLines,
        label = label,
        placeholder = placeholder,
        isInvalid = isInvalid,
        showTextLengthCounter = showTextLengthCounter,
        keyboardOptions = keyboardOptions,
        shape = shape,
        colors = colors,
    )
}

@Composable
fun MultiLineTextField(
    control: CompositeControl<String>,
    state: TextFieldState? = null,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    maxLength: Int? = null,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    shape: Shape = RoundedCornerShape(14.dp),
    colors: TextFieldColors = TextFieldDefaults.colors(),
    showTextLengthCounter: Boolean = false,
) {
    val initializedState = state ?: remember { TextFieldState() }

    ResetReadonlyStateOnResetValue(control = control, state = initializedState)

    val controlSnapshot = control.snapshotFlow.collectAsState().value
    val isReadonly = initializedState.readonlyState.valueFlow.collectAsState().value
    val isFocused = initializedState.focusedState.valueFlow.collectAsState().value

    MultiLineTextField(
        value = controlSnapshot.value,
        onValueChange = { x -> control.setValue(x) },
        modifier = modifier,
        isEnabled = controlSnapshot.isEnabled,
        isReadonly = isReadonly,
        isFocused = isFocused,
        onFocusChange = { x -> initializedState.focusedState.setValue(x.isFocused) },
        errorMessage = controlSnapshot.errorMessage,
        maxLength = maxLength,
        maxLines = maxLines,
        minLines = minLines,
        label = label,
        placeholder = placeholder,
        isInvalid = controlSnapshot.isInvalid,
        showTextLengthCounter = showTextLengthCounter,
        keyboardOptions = keyboardOptions,
        shape = shape,
        colors = colors,
    )
}
