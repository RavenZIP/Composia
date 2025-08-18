package io.github.ravenzip.composia.components.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.components.textField.shared.ErrorMessageWithSymbolsCounter
import io.github.ravenzip.composia.components.textField.shared.TextFieldWrapper
import io.github.ravenzip.composia.control.Control
import io.github.ravenzip.composia.state.TextFieldState
import io.github.ravenzip.composia.utils.numberInRange

@Composable
fun SinglenessTextField(
    control: Control<String>,
    state: TextFieldState = remember { TextFieldState() },
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    maxLength: Int = 0,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isHiddenText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors =
        TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
    showTextLengthCounter: Boolean = false,
) {
    TextFieldWrapper(control, state) {
        Column(modifier = modifier) {
            val value = control.value.collectAsState().value
            val isReadonly = state.isReadonly.collectAsState().value
            val isEnabled = control.isEnabled.collectAsState().value
            val isInvalid = control.isInvalid.collectAsState().value

            TextField(
                value = value,
                onValueChange = { newValue ->
                    if (maxLength == 0 || numberInRange(value = newValue.length, max = maxLength)) {
                        control.setValue(newValue)
                    }
                },
                modifier =
                    Modifier.fillMaxWidth().onFocusChanged { focusState ->
                        state.setFocus(focusState.isFocused)
                    },
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
                controlStatusFlow = control.status,
                isFocusedFlow = state.isFocused,
                showTextLengthCounter = showTextLengthCounter,
                maxLength = maxLength,
                currentLength = value.length,
                colors = colors,
            )
        }
    }
}
