package io.github.ravenzip.composia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ravenzip.composia.components.button.SimpleButton
import io.github.ravenzip.composia.components.textField.base.SingleLineTextField
import io.github.ravenzip.composia.components.textField.outlined.OutlinedSingleLineTextField
import io.github.ravenzip.composia.components.textField.simple.SimpleTextField
import io.github.ravenzip.composia.control.shared.Validator
import io.github.ravenzip.composia.control.statusControl.StatusControl
import io.github.ravenzip.composia.control.validatableControl.ValidatableControl
import io.github.ravenzip.composia.control.valueControl.ValueControl

class TextFieldScreenViewModel : ViewModel() {
    val simpleTextFieldControl = ValueControl("", coroutineScope = viewModelScope)

    val singlenessTextFieldControl =
        ValidatableControl(
            initialValue = "",
            validators = listOf { x -> Validator.required(x) },
            coroutineScope = viewModelScope,
        )

    val outlinedSinglenessTextFieldControl =
        ValidatableControl(
            initialValue = "",
            validators = listOf { x -> Validator.required(x) },
            coroutineScope = viewModelScope,
        )

    val simpleButtonControl = StatusControl(coroutineScope = viewModelScope)

    val simpleButtonControl2 = StatusControl(coroutineScope = viewModelScope)
}

@Composable
fun TextFieldScreen(
    viewModel: TextFieldScreenViewModel = remember { TextFieldScreenViewModel() },
    padding: PaddingValues,
    navigateToDropDownTextFieldScreen: () -> Unit,
    backToMenu: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
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
                label = { Text("OutlinedSinglenessTextField") },
            )

            SimpleButton(
                control = viewModel.simpleButtonControl2,
                onClick = { navigateToDropDownTextFieldScreen() },
                text = "DropDownTextFieldScreen",
            )

            SimpleButton(
                control = viewModel.simpleButtonControl,
                onClick = { backToMenu() },
                text = "Back to menu",
            )
        }
    }
}
