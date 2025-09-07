package io.github.ravenzip.composia.screen.checkbox

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.components.button.SimpleButton
import io.github.ravenzip.composia.components.checkbox.CheckboxGroup
import io.github.ravenzip.composia.control.validatableControl.mutableValidatableControlOf

@Composable
fun CheckboxScreen(padding: PaddingValues, backToMenu: () -> Unit) {
    val source = remember { listOf("Рыба", "Рак", "Щука") }
    val scope = rememberCoroutineScope()
    val control = remember {
        mutableValidatableControlOf(emptyList<String>(), coroutineScope = scope)
    }

    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CheckboxGroup(
                control = control,
                source = source,
                sourceItemToString = { it },
                keySelector = { it },
            )

            SimpleButton(onClick = { backToMenu() }, text = "Back to menu")
        }
    }
}
