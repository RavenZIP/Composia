package io.github.ravenzip.composia.components.switch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.control.validatableControl.ValidatableSingleControl
import io.github.ravenzip.composia.extension.S14
import io.github.ravenzip.composia.extension.S16Medium
import io.github.ravenzip.composia.style.DefaultComponentShape

@Composable
fun Switch(
    isSelected: Boolean,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    text: String,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: SwitchColors = SwitchDefaults.colors(),
) {
    Row(
        modifier = modifier.clip(DefaultComponentShape).clickable { onClick() }.padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = text, style = textStyle)

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = isSelected,
            onCheckedChange = { onClick() },
            enabled = isEnabled,
            colors = colors,
        )
    }
}

@Composable
fun Switch(
    control: ValidatableSingleControl<Boolean>,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    text: String,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: SwitchColors = SwitchDefaults.colors(),
) {
    val isSelected = control.valueFlow.collectAsState().value
    val isEnabled = control.isEnabledFlow.collectAsState().value

    Switch(
        isSelected = isSelected,
        isEnabled = isEnabled,
        onClick = { control.setValue(!control.value) },
        modifier = modifier,
        text = text,
        textStyle = textStyle,
        colors = colors,
    )
}

@Composable
fun Switch(
    isSelected: Boolean,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    label: String,
    labelStyle: TextStyle = TextStyle.S16Medium,
    description: String,
    descriptionStyle: TextStyle = TextStyle.S14,
    colors: SwitchColors = SwitchDefaults.colors(),
) {
    Row(
        modifier = modifier.clip(DefaultComponentShape).clickable { onClick() }.padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(text = label, style = labelStyle)

            Text(text = description, style = descriptionStyle)
        }

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = isSelected,
            onCheckedChange = { onClick() },
            enabled = isEnabled,
            colors = colors,
        )
    }
}

@Composable
fun Switch(
    control: ValidatableSingleControl<Boolean>,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    label: String,
    labelStyle: TextStyle = TextStyle.S16Medium,
    description: String,
    descriptionStyle: TextStyle = TextStyle.S14,
    colors: SwitchColors = SwitchDefaults.colors(),
) {
    val isSelected = control.valueFlow.collectAsState().value
    val isEnabled = control.isEnabledFlow.collectAsState().value

    Switch(
        isSelected = isSelected,
        isEnabled = isEnabled,
        onClick = { control.setValue(!control.value) },
        modifier = modifier,
        label = label,
        labelStyle = labelStyle,
        description = description,
        descriptionStyle = descriptionStyle,
        colors = colors,
    )
}
