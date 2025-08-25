package io.github.ravenzip.composia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ravenzip.composia.components.button.IconButton
import io.github.ravenzip.composia.components.button.SimpleButton
import io.github.ravenzip.composia.control.statusControl.StatusControl
import io.github.ravenzip.composia_demo.composia_demo.generated.resources.Res
import io.github.ravenzip.composia_demo.composia_demo.generated.resources.outline_settings_24
import org.jetbrains.compose.resources.painterResource

class ButtonScreenViewModel : ViewModel() {
    val simpleButtonControl = StatusControl(coroutineScope = viewModelScope)

    val iconButtonControl = StatusControl(coroutineScope = viewModelScope)
}

@Composable
fun ButtonScreen(
    viewModel: ButtonScreenViewModel = ButtonScreenViewModel(),
    padding: PaddingValues,
    backToMenu: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val t = viewModel.simpleButtonControl.isEnabledFlow.collectAsState().value
            val c = remember { mutableStateOf(0) }

            SimpleButton(
                control = viewModel.simpleButtonControl,
                onClick = {
                    c.value += 1

                    if (t) {
                        viewModel.simpleButtonControl.disable()
                    } else {
                        viewModel.simpleButtonControl.enable()
                    }
                },
                text = "Click me",
                modifier = Modifier.fillMaxWidth(0.9f),
            )

            IconButton(
                control = viewModel.iconButtonControl,
                icon = painterResource(Res.drawable.outline_settings_24),
            ) {}

            OutlinedTextField(value = c.value.toString(), onValueChange = {})
            OutlinedTextField(value = t.toString(), onValueChange = {})

            SimpleButton(
                control = viewModel.simpleButtonControl,
                onClick = { backToMenu() },
                text = "Back to menu",
            )
        }
    }
}
