package io.github.ravenzip.composia.components.radio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.control.validatable.MutableValidatableControl
import io.github.ravenzip.composia.style.DefaultComponentShape

@Composable
fun <T, K> RadioGroup(
    control: MutableValidatableControl<T>,
    source: List<T>,
    view: (T) -> String,
    keySelector: (T) -> K,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    contentPadding: Arrangement.Vertical = Arrangement.spacedBy(10.dp),
    colors: RadioButtonColors = RadioButtonDefaults.colors(),
) {
    val value = control.valueEvents.collectAsState().value
    val selectedKey = remember(value) { keySelector(value) }

    Column(modifier = modifier, verticalArrangement = contentPadding) {
        source.forEach { item ->
            Row(
                modifier =
                    Modifier.fillMaxWidth()
                        .clip(DefaultComponentShape)
                        .clickable { control.setValue(item) }
                        .padding(top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val text = remember(item) { view(item) }
                val key = remember(item) { keySelector(item) }

                RadioButton(
                    selected = selectedKey == key,
                    onClick = { control.setValue(item) },
                    enabled = control.isEnabled,
                    colors = colors,
                )

                Text(text = text, style = textStyle)
            }
        }
    }
}
