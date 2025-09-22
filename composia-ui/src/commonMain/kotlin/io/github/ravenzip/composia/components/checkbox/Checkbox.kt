package io.github.ravenzip.composia.components.checkbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.control.validatable.MutableValidatableControl
import io.github.ravenzip.composia.extension.S18
import io.github.ravenzip.composia.style.DefaultComponentShape

@Composable
fun Checkbox(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextStyle.S18,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
) {
    Row(
        modifier =
            modifier
                .clip(DefaultComponentShape)
                .clickable(enabled = enabled, onClick = onClick)
                .padding(top = 5.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() },
            enabled = enabled,
            colors = colors,
        )
        Text(text = text, style = textStyle)
    }
}

@Composable
fun Checkbox(
    control: MutableValidatableControl<Boolean>,
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextStyle.S18,
    colors: CheckboxColors = CheckboxDefaults.colors(),
) {
    val isSelected = control.valueFlow.collectAsState().value
    val isEnabled = control.isEnabledFlow.collectAsState().value

    Checkbox(
        isSelected = isSelected,
        onClick = { control.setValue(!control.value) },
        modifier = modifier,
        text = text,
        textStyle = textStyle,
        enabled = isEnabled,
        colors = colors,
    )
}
