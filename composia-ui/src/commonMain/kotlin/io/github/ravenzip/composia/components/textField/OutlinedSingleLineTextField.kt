package io.github.ravenzip.composia.components.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.components.textField.shared.ErrorMessageWithSymbolsCounter
import io.github.ravenzip.composia.components.textField.shared.TextFieldWrapper
import io.github.ravenzip.composia.components.textField.shared.acceptInput
import io.github.ravenzip.composia.control.formControl.FormControl
import io.github.ravenzip.composia.state.TextFieldState

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
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors =
        TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
        ),
    showTextLengthCounter: Boolean = false,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().onFocusChanged(onFocusChange),
            enabled = isEnabled,
            readOnly = isReadonly,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isInvalid,
            visualTransformation =
                if (isHiddenText) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            shape = shape,
            colors = colors,
        )

        ErrorMessageWithSymbolsCounter(
            errorMessage = errorMessage,
            isFocused = isFocused,
            showTextLengthCounter = showTextLengthCounter,
            maxLength = maxLength,
            currentLength = value.length,
            colors = colors,
        )
    }
}

@Composable
fun OutlinedSingleLineTextFieldWithControl(
    control: FormControl<String>,
    state: TextFieldState? = null,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    maxLength: Int? = null,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isHiddenText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors =
        TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
        ),
    showTextLengthCounter: Boolean = false,
) {
    val initializedState = state ?: remember { TextFieldState() }

    TextFieldWrapper(control, initializedState) {
        val controlSnapshot = control.snapshotFlow.collectAsState().value
        val isReadonly = initializedState.isReadonly.collectAsState().value
        val isFocused = initializedState.isFocused.collectAsState().value

        OutlinedSingleLineTextField(
            value = controlSnapshot.value,
            onValueChange = { newValue ->
                if (acceptInput(currentLength = newValue.length, maxLength = maxLength)) {
                    control.setValue(newValue)
                }
            },
            isEnabled = controlSnapshot.isEnabled,
            isReadonly = isReadonly,
            isInvalid = controlSnapshot.isInvalid,
            errorMessage = controlSnapshot.errorMessage,
            isFocused = isFocused,
            onFocusChange = { focusState -> initializedState.setFocus(focusState.isFocused) },
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
}
