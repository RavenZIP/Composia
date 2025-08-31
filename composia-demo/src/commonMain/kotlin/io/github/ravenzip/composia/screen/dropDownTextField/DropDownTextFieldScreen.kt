package io.github.ravenzip.composia.screen.dropDownTextField

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.components.button.SimpleButton
import io.github.ravenzip.composia.components.model.DataSource
import io.github.ravenzip.composia.components.textField.dropdown.DropDownTextField
import io.github.ravenzip.composia.control.validatableControl.ValidatableSingleControl
import io.github.ravenzip.composia.sample.Item
import io.github.ravenzip.composia.state.DropDownTextFieldState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

@Composable
fun DropDownTextFieldScreen(padding: PaddingValues, backToMenu: () -> Unit) {
    val scope = rememberCoroutineScope()
    val control = remember {
        ValidatableSingleControl(initialValue = Item.createEmptyModel(), coroutineScope = scope)
    }
    val state = remember { DropDownTextFieldState() }
    val source = remember {
        DataSource.ByQuery { x ->
            flow {
                println("Started... $x")
                delay(3000)
                println("Complete...")

                val response =
                    Item.createItems().filter { item -> item.name.contains(x) }.toMutableStateList()

                emit(response)
            }
        }
    }

    val source2 = remember { DataSource.Predefined(Item.createItems()) }

    val currentSource = remember { mutableStateOf<DataSource<Item>>(source2) }

    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SimpleButton(
                onClick = {
                    if (currentSource.value is DataSource.Predefined) {
                        currentSource.value = source
                    } else {
                        currentSource.value = source2
                    }
                },
                text = "Сменить источник",
            )

            Text("Текущий источник: ${currentSource.value}")

            DropDownTextField(
                control = control,
                state = state,
                source = currentSource.value,
                sourceItemToString = { x -> x.name },
            )

            DropDownTextFieldInfo(control = control, state = state)

            SimpleButton(onClick = { backToMenu() }, text = "Back to menu")
        }
    }
}

@Composable
fun DropDownTextFieldInfo(control: ValidatableSingleControl<Item>, state: DropDownTextFieldState) {
    val controlSnapshot = control.snapshotFlow.collectAsState().value
    val isReadonly = state.readonlyState.valueFlow.collectAsState().value
    val isFucused = state.focusedState.valueFlow.collectAsState().value
    val isExpanded = state.expandedState.valueFlow.collectAsState().value

    Card(modifier = Modifier.fillMaxWidth(0.9f)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Выбранное значение: ${controlSnapshot.value.name.ifEmpty { "Пусто" }}")
            Text("Последний тип изменений: ${controlSnapshot.typeChange}")
            Text("Были изменения: ${ if(controlSnapshot.hasChanges) "Да" else "Нет"}")
            Text("Включено: ${if (controlSnapshot.isEnabled) "Да" else "Нет"}")
            Text("Валидно: ${if (controlSnapshot.isValid) "Да" else "Нет"}")

            if (controlSnapshot.isValid) {
                Text("Текст ошибки: ${controlSnapshot.errorMessage}")
            }

            Text("Только для чтения: ${if (isReadonly) "Да" else "Нет"}")
            Text("Открыто: ${if (isExpanded) "Да" else "Нет"}")
            Text("В фокусе: ${if (isFucused) "Да" else "Нет"}")
        }
    }
}
