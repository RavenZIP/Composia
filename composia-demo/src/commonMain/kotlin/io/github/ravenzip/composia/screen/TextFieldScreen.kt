package io.github.ravenzip.composia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ravenzip.composia.Validator
import io.github.ravenzip.composia.components.button.SimpleButton
import io.github.ravenzip.composia.components.textField.OutlinedSinglenessTextField
import io.github.ravenzip.composia.components.textField.SimpleTextField
import io.github.ravenzip.composia.components.textField.SinglenessTextField
import io.github.ravenzip.composia.control.BaseControl
import io.github.ravenzip.composia.control.Control

class TextFieldScreenViewModel : ViewModel() {
    val simpleTextFieldControl = Control("", coroutineScope = viewModelScope)

    val singlenessTextFieldControl =
        Control(
            initialValue = "",
            validators = listOf { x -> Validator.required(x) },
            coroutineScope = viewModelScope,
        )

    val outlinedSinglenessTextFieldControl =
        Control(
            initialValue = "",
            validators = listOf { x -> Validator.required(x) },
            coroutineScope = viewModelScope,
        )

    val simpleButtonControl = BaseControl(coroutineScope = viewModelScope)
}

@Composable
fun TextFieldScreen(
    viewModel: TextFieldScreenViewModel = TextFieldScreenViewModel(),
    padding: PaddingValues,
    backToMenu: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SimpleTextField(control = viewModel.simpleTextFieldControl)

            SinglenessTextField(
                control = viewModel.singlenessTextFieldControl,
                maxLength = 25,
                showTextLengthCounter = true,
            )

            OutlinedSinglenessTextField(
                control = viewModel.outlinedSinglenessTextFieldControl,
                maxLength = 25,
                showTextLengthCounter = true,
                label = { Text("OutlinedSinglenessTextField") },
            )

            SimpleButton(control = viewModel.simpleButtonControl, text = "Back to menu") {
                backToMenu()
            }
        }
    }
}
