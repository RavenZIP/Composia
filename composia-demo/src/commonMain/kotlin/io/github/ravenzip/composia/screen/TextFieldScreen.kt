package io.github.ravenzip.composia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ravenzip.composia.Validator
import io.github.ravenzip.composia.components.button.SimpleButton
import io.github.ravenzip.composia.components.textField.base.SingleLineTextField
import io.github.ravenzip.composia.components.textField.outlined.OutlinedSingleLineTextField
import io.github.ravenzip.composia.components.textField.simple.SimpleTextField
import io.github.ravenzip.composia.control.formControl.CompositeControl
import io.github.ravenzip.composia.control.statusControl.StatusControl
import io.github.ravenzip.composia.control.valueControl.ValueControl
import io.github.ravenzip.composia.state.TextFieldState

class TextFieldScreenViewModel : ViewModel() {
    val simpleTextFieldControl = ValueControl("", coroutineScope = viewModelScope)

    val singlenessTextFieldControl =
        CompositeControl(
            initialValue = "",
            validators = listOf { x -> Validator.required(x) },
            coroutineScope = viewModelScope,
        )

    val outlinedSinglenessTextFieldControl =
        CompositeControl(
            initialValue = "",
            validators = listOf { x -> Validator.required(x) },
            coroutineScope = viewModelScope,
        )

    val outlinedSinglenessTextFieldControl2 =
        CompositeControl(
            initialValue = "",
            validators = listOf { x -> Validator.required(x) },
            coroutineScope = viewModelScope,
        )

    val textFieldState = TextFieldState()

    val simpleButtonControl = StatusControl(coroutineScope = viewModelScope)
}

@Composable
fun TextFieldScreen(
    viewModel: TextFieldScreenViewModel = remember { TextFieldScreenViewModel() },
    padding: PaddingValues,
    backToMenu: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val value =
                viewModel.outlinedSinglenessTextFieldControl2.valueFlow.collectAsState().value
            val isEnabled =
                viewModel.outlinedSinglenessTextFieldControl2.isEnabledFlow.collectAsState().value
            val isInvalid =
                viewModel.outlinedSinglenessTextFieldControl2.isInvalidFlow.collectAsState().value
            val errorMessage =
                viewModel.outlinedSinglenessTextFieldControl2.errorMessageFlow
                    .collectAsState()
                    .value
            val isFocused = viewModel.textFieldState.isFocused.collectAsState().value
            val isReadonly = viewModel.textFieldState.isReadonly.collectAsState().value

            SimpleTextField(control = viewModel.simpleTextFieldControl)

            SingleLineTextField(
                control = viewModel.singlenessTextFieldControl,
                maxLength = 25,
                showTextLengthCounter = true,
            )

            OutlinedSingleLineTextField(
                control = viewModel.outlinedSinglenessTextFieldControl,
                maxLength = 25,
                showTextLengthCounter = true,
                label = { Text("OutlinedSinglenessTextField 1") },
            )

            OutlinedSingleLineTextField(
                value = value,
                onValueChange = { x -> viewModel.outlinedSinglenessTextFieldControl2.setValue(x) },
                isEnabled = isEnabled,
                isInvalid = isInvalid,
                errorMessage = errorMessage,
                isFocused = isFocused,
                isReadonly = isReadonly,
                onFocusChange = { x -> viewModel.textFieldState.setFocus(x.isFocused) },
                showTextLengthCounter = true,
                label = { Text("OutlinedSinglenessTextField 2") },
            )

            SimpleButton(control = viewModel.simpleButtonControl, text = "Back to menu") {
                backToMenu()
            }
        }
    }
}
