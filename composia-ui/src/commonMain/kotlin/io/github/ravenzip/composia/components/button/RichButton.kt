package io.github.ravenzip.composia.components.button

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.config.IconConfig
import io.github.ravenzip.composia.control.statusControl.StatusControl
import io.github.ravenzip.composia.extension.S18Medium
import io.github.ravenzip.composia.extension.S20

@Composable
fun RichButton(
    control: StatusControl,
    modifier: Modifier = Modifier,
    label: String,
    labelStyle: TextStyle = TextStyle.S18Medium,
    description: String,
    descriptionStyle: TextStyle = TextStyle.S20,
    icon: Painter,
    iconConfig: IconConfig = IconConfig.Default,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(10.dp),
    contentPadding: PaddingValues = PaddingValues(18.dp),
    onClick: () -> Unit = {},
) {
    val isEnabled = control.isEnabledFlow.collectAsState().value

    val labelColor =
        if (labelStyle.color != Color.Unspecified) labelStyle.color else colors.contentColor
    val descriptionColor =
        if (descriptionStyle.color != Color.Unspecified) descriptionStyle.color
        else colors.contentColor

    val mergedLabelStyle = labelStyle.merge(color = labelColor)
    val mergedDescriptionStyle = descriptionStyle.merge(color = descriptionColor)

    Button(
        onClick = { onClick() },
        modifier = modifier,
        enabled = isEnabled,
        colors = colors,
        shape = shape,
        contentPadding = contentPadding,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Icon(
                painter = icon,
                contentDescription = iconConfig.description,
                modifier = Modifier.size(iconConfig.size.dp),
                tint = iconConfig.color ?: colors.contentColor,
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(text = label, style = mergedLabelStyle)
                Text(text = description, style = mergedDescriptionStyle)
            }
        }
    }
}

@Composable
fun RichButton(
    control: StatusControl,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    description: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(10.dp),
    contentPadding: PaddingValues = PaddingValues(18.dp),
    onClick: () -> Unit = {},
) {
    val isEnabled = control.isEnabledFlow.collectAsState().value

    Button(
        onClick = { onClick() },
        modifier = modifier,
        enabled = isEnabled,
        colors = colors,
        shape = shape,
        contentPadding = contentPadding,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            icon()

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                label()

                description()
            }
        }
    }
}
