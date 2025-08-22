package io.github.ravenzip.composia.components.switch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.control.formControl.FormControl

@Composable
fun Switch(
    isSelected: Boolean,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    text: String,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: SwitchColors = SwitchDefaults.colors(),
    onCheckedChange: () -> Unit,
) {
    Row(
        modifier =
            modifier.clip(RoundedCornerShape(10.dp)).clickable { onCheckedChange() }.padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = text, style = textStyle)

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = isSelected,
            onCheckedChange = { onCheckedChange() },
            enabled = isEnabled,
            colors = colors,
        )
    }
}

@Composable
fun Switch(
    control: FormControl<Boolean>,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    text: String,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: SwitchColors = SwitchDefaults.colors(),
) {
    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { control.setValue(!control.value) }
                .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = text, style = textStyle)

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = control.valueFlow.collectAsState().value,
            onCheckedChange = { control.setValue(!control.value) },
            enabled = control.isEnabledFlow.collectAsState().value,
            colors = colors,
        )
    }
}

@Composable
fun Switch(
    isSelected: Boolean,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    label: String,
    labelStyle: TextStyle = LocalTextStyle.current,
    description: String,
    descriptionStyle: TextStyle = LocalTextStyle.current,
    colors: SwitchColors = SwitchDefaults.colors(),
    onCheckedChange: () -> Unit,
) {
    Row(
        modifier =
            modifier.clip(RoundedCornerShape(10.dp)).clickable { onCheckedChange() }.padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(text = label, style = labelStyle)

            Text(text = description, style = descriptionStyle)
        }

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = isSelected,
            onCheckedChange = { onCheckedChange() },
            enabled = isEnabled,
            colors = colors,
        )
    }
}

@Composable
fun Switch(
    control: FormControl<Boolean>,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    label: String,
    labelStyle: TextStyle = LocalTextStyle.current,
    description: String,
    descriptionStyle: TextStyle = LocalTextStyle.current,
    colors: SwitchColors = SwitchDefaults.colors(),
) {
    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { control.setValue(!control.value) }
                .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(text = label, style = labelStyle)

            Text(text = description, style = descriptionStyle)
        }

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = control.valueFlow.collectAsState().value,
            onCheckedChange = { control.setValue(!control.value) },
            enabled = control.isEnabledFlow.collectAsState().value,
            colors = colors,
        )
    }
}
