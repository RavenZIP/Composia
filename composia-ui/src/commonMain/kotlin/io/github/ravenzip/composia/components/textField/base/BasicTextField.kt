package io.github.ravenzip.composia.components.textField.base

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.ravenzip.composia.components.textField.shared.ErrorMessageWithSymbolsCounter
import io.github.ravenzip.composia.components.textField.shared.acceptInput
import io.github.ravenzip.composia.style.DefaultComponentShape

@Composable
internal fun BasicTextField(
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
    singleLine: Boolean = false,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isHiddenText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = DefaultComponentShape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    showTextLengthCounter: Boolean = false,
) {
    TextField(
        value = value,
        onValueChange = { x ->
            if (acceptInput(currentLength = x.length, maxLength = maxLength)) {
                onValueChange(x)
            }
        },
        modifier = modifier.onFocusChanged(onFocusChange),
        enabled = isEnabled,
        readOnly = isReadonly,
        maxLines = maxLines,
        minLines = minLines,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = {
            ErrorMessageWithSymbolsCounter(
                errorMessage = errorMessage,
                isFocused = isFocused,
                showTextLengthCounter = showTextLengthCounter,
                maxLength = maxLength,
                currentLength = value.length,
                colors = colors,
            )
        },
        isError = isInvalid,
        visualTransformation =
            if (isHiddenText) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        shape = shape,
        colors = colors,
    )
}
