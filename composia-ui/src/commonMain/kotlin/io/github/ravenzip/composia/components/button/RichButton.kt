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
import io.github.ravenzip.composia.control.statusControl.StatusControl
import io.github.ravenzip.composia.extension.S18Medium
import io.github.ravenzip.composia.extension.S20
import io.github.ravenzip.composia.style.IconStyle

@Composable
fun RichButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    description: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    isEnabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(10.dp),
    contentPadding: PaddingValues = PaddingValues(18.dp),
) {
    Button(
        onClick = onClick,
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

@Composable
fun RichButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    label: String,
    labelStyle: TextStyle = TextStyle.S18Medium,
    description: String,
    descriptionStyle: TextStyle = TextStyle.S20,
    icon: Painter,
    iconDescription: String? = null,
    iconStyle: IconStyle = IconStyle.Default,
    isEnabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(10.dp),
    contentPadding: PaddingValues = PaddingValues(18.dp),
) {
    val labelColor =
        if (labelStyle.color != Color.Unspecified) labelStyle.color else colors.contentColor
    val descriptionColor =
        if (descriptionStyle.color != Color.Unspecified) descriptionStyle.color
        else colors.contentColor

    val mergedLabelStyle = labelStyle.merge(color = labelColor)
    val mergedDescriptionStyle = descriptionStyle.merge(color = descriptionColor)

    RichButton(
        onClick = onClick,
        modifier = modifier,
        label = { Text(text = label, style = mergedLabelStyle) },
        description = { Text(text = description, style = mergedDescriptionStyle) },
        icon = {
            Icon(
                painter = icon,
                contentDescription = iconDescription,
                modifier = Modifier.size(iconStyle.size.dp),
                tint = iconStyle.color ?: colors.contentColor,
            )
        },
        isEnabled = isEnabled,
        colors = colors,
        shape = shape,
        contentPadding = contentPadding,
    )
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

    RichButton(
        onClick = onClick,
        modifier = modifier,
        label = label,
        description = description,
        icon = icon,
        isEnabled = isEnabled,
        colors = colors,
        shape = shape,
        contentPadding = contentPadding,
    )
}

@Composable
fun RichButton(
    control: StatusControl,
    modifier: Modifier = Modifier,
    label: String,
    labelStyle: TextStyle = TextStyle.S18Medium,
    description: String,
    descriptionStyle: TextStyle = TextStyle.S20,
    icon: Painter,
    iconDescription: String? = null,
    iconStyle: IconStyle = IconStyle.Default,
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

    RichButton(
        onClick = onClick,
        modifier = modifier,
        label = label,
        labelStyle = mergedLabelStyle,
        description = description,
        descriptionStyle = mergedDescriptionStyle,
        icon = icon,
        iconDescription = iconDescription,
        iconStyle = iconStyle,
        isEnabled = isEnabled,
        colors = colors,
        shape = shape,
        contentPadding = contentPadding,
    )
}
