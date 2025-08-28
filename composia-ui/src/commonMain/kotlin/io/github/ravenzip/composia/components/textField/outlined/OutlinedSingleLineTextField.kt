package io.github.ravenzip.composia.components.textField.outlined

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Shape
import io.github.ravenzip.composia.components.textField.shared.ResetReadonlyStateOnResetValue
import io.github.ravenzip.composia.control.compositeControl.CompositeControl
import io.github.ravenzip.composia.state.TextFieldState
import io.github.ravenzip.composia.style.DefaultComponentShape

@Composable
fun OutlinedSingleLineTextField(
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
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isHiddenText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = DefaultComponentShape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    showTextLengthCounter: Boolean = false,
) {
    BasicOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        isEnabled = isEnabled,
        isReadonly = isReadonly,
        errorMessage = errorMessage,
        isFocused = isFocused,
        onFocusChange = onFocusChange,
        maxLength = maxLength,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isInvalid = isInvalid,
        isHiddenText = isHiddenText,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        showTextLengthCounter = showTextLengthCounter,
        shape = shape,
        colors = colors,
    )
}

@Composable
fun OutlinedSingleLineTextField(
    control: CompositeControl<String>,
    state: TextFieldState? = null,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    maxLength: Int? = null,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isHiddenText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = DefaultComponentShape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    showTextLengthCounter: Boolean = false,
) {
    val initializedState = state ?: remember { TextFieldState() }

    ResetReadonlyStateOnResetValue(control = control, state = initializedState)

    val controlSnapshot = control.snapshotFlow.collectAsState().value
    val isReadonly = initializedState.readonlyState.valueFlow.collectAsState().value
    val isFocused = initializedState.focusedState.valueFlow.collectAsState().value

    OutlinedSingleLineTextField(
        value = controlSnapshot.value,
        onValueChange = { x -> control.setValue(x) },
        isEnabled = controlSnapshot.isEnabled,
        isReadonly = isReadonly,
        isInvalid = controlSnapshot.isInvalid,
        errorMessage = controlSnapshot.errorMessage,
        isFocused = isFocused,
        onFocusChange = { focusState ->
            initializedState.focusedState.setValue(focusState.isFocused)
        },
        modifier = modifier,
        maxLength = maxLength,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isHiddenText = isHiddenText,
        keyboardOptions = keyboardOptions,
        showTextLengthCounter = showTextLengthCounter,
        shape = shape,
        colors = colors,
    )
}
