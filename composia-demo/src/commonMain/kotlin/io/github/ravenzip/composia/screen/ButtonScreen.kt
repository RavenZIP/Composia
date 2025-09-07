package io.github.ravenzip.composia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ravenzip.composia.components.button.IconButton
import io.github.ravenzip.composia.components.button.RichButton
import io.github.ravenzip.composia.components.button.SimpleButton
import io.github.ravenzip.composia.control.activation.mutableActivationControlOf
import io.github.ravenzip.composia_demo.composia_demo.generated.resources.Res
import io.github.ravenzip.composia_demo.composia_demo.generated.resources.outline_settings_24
import org.jetbrains.compose.resources.painterResource

class ButtonScreenViewModel : ViewModel() {
    val simpleButtonControl = mutableActivationControlOf(coroutineScope = viewModelScope)

    val richButtonControl = mutableActivationControlOf(coroutineScope = viewModelScope)

    val iconButtonControl = mutableActivationControlOf(coroutineScope = viewModelScope)
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
            SimpleButton(control = viewModel.simpleButtonControl, text = "SimpleButton")

            RichButton(
                control = viewModel.richButtonControl,
                label = "Заголовок",
                description =
                    "Это описание кнопки. Здесь может быть размещен уточняющий текст к заголовку",
                icon = painterResource(Res.drawable.outline_settings_24),
            )

            IconButton(
                control = viewModel.iconButtonControl,
                icon = painterResource(Res.drawable.outline_settings_24),
            )

            SimpleButton(
                control = viewModel.simpleButtonControl,
                onClick = { backToMenu() },
                text = "Back to menu",
            )
        }
    }
}
