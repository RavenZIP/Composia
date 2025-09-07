package io.github.ravenzip.composia.components.textField.outlined

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Shape
import io.github.ravenzip.composia.components.textField.shared.resetReadonlyStateOnResetValue
import io.github.ravenzip.composia.control.validatable.MutableValidatableControl
import io.github.ravenzip.composia.state.TextFieldState
import io.github.ravenzip.composia.style.DefaultComponentShape

@Composable
fun OutlinedMultiLineTextField(
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
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
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
fun OutlinedMultiLineTextField(
    control: MutableValidatableControl<String>,
    state: TextFieldState? = null,
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    shape: Shape = DefaultComponentShape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    showTextLengthCounter: Boolean = false,
) {
    val initializedState = state ?: remember { TextFieldState() }

    resetReadonlyStateOnResetValue(control = control, state = initializedState)

    val controlSnapshot = control.snapshotState.collectAsState().value
    val errorMessage = remember(controlSnapshot) { controlSnapshot.errorMessage ?: "" }
    val isReadonly = initializedState.readonlyState.valueChanges.collectAsState().value
    val isFocused = initializedState.focusedState.valueChanges.collectAsState().value

    OutlinedMultiLineTextField(
        value = controlSnapshot.value,
        onValueChange = { x -> control.setValue(x) },
        modifier = modifier,
        isEnabled = controlSnapshot.isEnabled,
        isReadonly = isReadonly,
        isFocused = isFocused,
        onFocusChange = { x -> initializedState.focusedState.setValue(x.isFocused) },
        errorMessage = errorMessage,
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
