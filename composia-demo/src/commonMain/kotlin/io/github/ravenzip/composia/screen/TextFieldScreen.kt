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
import io.github.ravenzip.composia.components.textField.OutlinedSingleLineTextFieldWithControl
import io.github.ravenzip.composia.components.textField.SimpleTextFieldWithControl
import io.github.ravenzip.composia.components.textField.SingleLineTextFieldWithControl
import io.github.ravenzip.composia.control.FormControl
import io.github.ravenzip.composia.control.StatusControl

class TextFieldScreenViewModel : ViewModel() {
    val simpleTextFieldFormControl = FormControl("", coroutineScope = viewModelScope)

    val singlenessTextFieldFormControl =
        FormControl(
            initialValue = "",
            validators = listOf { x -> Validator.required(x) },
            coroutineScope = viewModelScope,
        )

    val outlinedSinglenessTextFieldFormControl =
        FormControl(
            initialValue = "",
            validators = listOf { x -> Validator.required(x) },
            coroutineScope = viewModelScope,
        )

    val simpleButtonControl = StatusControl(coroutineScope = viewModelScope)
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
            SimpleTextFieldWithControl(formControl = viewModel.simpleTextFieldFormControl)

            SingleLineTextFieldWithControl(
                formControl = viewModel.singlenessTextFieldFormControl,
                maxLength = 25,
                showTextLengthCounter = true,
            )

            OutlinedSingleLineTextFieldWithControl(
                formControl = viewModel.outlinedSinglenessTextFieldFormControl,
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
