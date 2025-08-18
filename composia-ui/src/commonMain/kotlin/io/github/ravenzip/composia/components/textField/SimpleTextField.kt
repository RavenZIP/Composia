package io.github.ravenzip.composia.components.textField

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ravenzip.composia.components.textField.shared.TextFieldWrapper
import io.github.ravenzip.composia.control.Control
import io.github.ravenzip.composia.state.TextFieldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTextField(
    control: Control<String>,
    state: TextFieldState = remember { TextFieldState() },
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    interactionSource: InteractionSource = MutableInteractionSource(),
    colors: TextFieldColors = TextFieldDefaults.colors(),
    textStyle: TextStyle = TextStyle(color = colors.unfocusedTextColor, fontSize = 16.sp),
    showLine: Boolean = true,
) {
    TextFieldWrapper(control, state) {
        val value = control.value.collectAsState().value
        val isReadonly = state.isReadonly.collectAsState().value
        val isEnabled = control.isEnabled.collectAsState().value

        BasicTextField(
            value = value,
            onValueChange = { value -> control.setValue(value) },
            modifier =
                modifier.onFocusChanged { focusState -> state.setFocus(focusState.isFocused) },
            enabled = isEnabled,
            readOnly = isReadonly,
            textStyle = textStyle,
            singleLine = singleLine,
        ) {
            val isError = control.isInvalid.collectAsState().value

            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = it,
                enabled = isEnabled,
                singleLine = singleLine,
                isError = isError,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = placeholder,
                colors = colors,
                contentPadding = PaddingValues(3.dp),
                container = {
                    if (showLine) {
                        Line(
                            isEnabled = isEnabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            colors = colors,
                        )
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Line(
    isEnabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource,
    colors: TextFieldColors,
) {
    Box(
        Modifier.indicatorLine(
            enabled = isEnabled,
            isError = isError,
            interactionSource = interactionSource,
            colors = colors,
        )
    )
}
