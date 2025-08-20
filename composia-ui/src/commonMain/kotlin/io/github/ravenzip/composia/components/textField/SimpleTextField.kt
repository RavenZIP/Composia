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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ravenzip.composia.components.textField.shared.TextFieldWrapper
import io.github.ravenzip.composia.control.valueControl.ValueControl
import io.github.ravenzip.composia.state.TextFieldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean = true,
    isReadonly: Boolean = false,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    interactionSource: InteractionSource = MutableInteractionSource(),
    colors: TextFieldColors = TextFieldDefaults.colors(),
    textStyle: TextStyle = TextStyle(color = colors.unfocusedTextColor, fontSize = 16.sp),
    showLine: Boolean = false,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = isEnabled,
        readOnly = isReadonly,
        textStyle = textStyle,
    ) {
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = it,
            enabled = isEnabled,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            placeholder = placeholder,
            colors = colors,
            contentPadding = PaddingValues(3.dp),
            container = {
                if (showLine) {
                    Line(
                        isEnabled = isEnabled,
                        interactionSource = interactionSource,
                        colors = colors,
                    )
                }
            },
        )
    }
}

@Composable
fun SimpleTextFieldWithControl(
    control: ValueControl<String>,
    state: TextFieldState? = null,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    interactionSource: InteractionSource = MutableInteractionSource(),
    colors: TextFieldColors = TextFieldDefaults.colors(),
    textStyle: TextStyle = TextStyle(color = colors.unfocusedTextColor, fontSize = 16.sp),
    showLine: Boolean = false,
) {
    val initializedState = state ?: remember { TextFieldState() }

    TextFieldWrapper(control, initializedState) {
        val controlSnapshot = control.snapshotFlow.collectAsState().value
        val isReadonly = initializedState.isReadonly.collectAsState().value

        SimpleTextField(
            value = controlSnapshot.value,
            onValueChange = { value -> control.setValue(value) },
            modifier = modifier,
            isEnabled = controlSnapshot.isEnabled,
            isReadonly = isReadonly,
            placeholder = placeholder,
            interactionSource = interactionSource,
            colors = colors,
            textStyle = textStyle,
            showLine = showLine,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Line(
    isEnabled: Boolean,
    interactionSource: InteractionSource,
    colors: TextFieldColors,
) {
    Box(
        Modifier.indicatorLine(
            enabled = isEnabled,
            isError = false,
            interactionSource = interactionSource,
            colors = colors,
        )
    )
}
