package io.github.ravenzip.composia.components.textField.base

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Shape
import io.github.ravenzip.composia.components.textField.shared.acceptInput
import io.github.ravenzip.composia.components.textField.shared.resetReadonlyStateOnResetValue
import io.github.ravenzip.composia.control.validatableControl.MutableValidatableControl
import io.github.ravenzip.composia.state.TextFieldState
import io.github.ravenzip.composia.style.DefaultComponentShape

@Composable
fun SingleLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean = true,
    isReadonly: Boolean = false,
    isInvalid: Boolean = false,
    errorMessage: String = "",
    isFocused: Boolean = false,
    onFocusChange: (FocusState) -> Unit = {},
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isHiddenText: Boolean = false,
    showTextLengthCounter: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = DefaultComponentShape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    BasicTextField(
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
fun SingleLineTextField(
    control: MutableValidatableControl<String>,
    state: TextFieldState? = null,
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isHiddenText: Boolean = false,
    showTextLengthCounter: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = DefaultComponentShape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    val initializedState = state ?: remember { TextFieldState() }

    resetReadonlyStateOnResetValue(control = control, state = initializedState)

    val controlSnapshot = control.snapshotEvents.collectAsState().value
    val errorMessage = remember(controlSnapshot) { controlSnapshot.errorMessage ?: "" }
    val isReadonly = initializedState.readonlyState.valueFlow.collectAsState().value
    val isFocused = initializedState.focusedState.valueFlow.collectAsState().value

    SingleLineTextField(
        value = controlSnapshot.value,
        onValueChange = { newValue ->
            if (acceptInput(currentLength = newValue.length, maxLength = maxLength)) {
                control.setValue(newValue)
            }
        },
        isEnabled = controlSnapshot.isEnabled,
        isReadonly = isReadonly,
        isInvalid = controlSnapshot.isInvalid,
        errorMessage = errorMessage,
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
