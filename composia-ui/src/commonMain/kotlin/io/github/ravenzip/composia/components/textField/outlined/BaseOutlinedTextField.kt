package io.github.ravenzip.composia.components.textField.outlined

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.components.textField.shared.ErrorMessageWithSymbolsCounter
import io.github.ravenzip.composia.components.textField.shared.acceptInput

@Composable
internal fun BasicOutlinedTextField(
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
    singleLine: Boolean = false,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isHiddenText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    showTextLengthCounter: Boolean = false,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { x ->
                if (acceptInput(currentLength = x.length, maxLength = maxLength)) {
                    onValueChange(x)
                }
            },
            modifier = Modifier.fillMaxWidth().onFocusChanged(onFocusChange),
            enabled = isEnabled,
            readOnly = isReadonly,
            maxLines = maxLines,
            minLines = minLines,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isInvalid,
            visualTransformation =
                if (isHiddenText) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
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
