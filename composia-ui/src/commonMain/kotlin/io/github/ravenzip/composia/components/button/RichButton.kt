package io.github.ravenzip.composia.components.button

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.config.IconConfig
import io.github.ravenzip.composia.config.TextConfig
import io.github.ravenzip.composia.control.BaseControl

@Composable
fun RichButton(
    control: BaseControl,
    modifier: Modifier = Modifier,
    label: String,
    labelConfig: TextConfig = TextConfig.S18Medium,
    description: String,
    descriptionConfig: TextConfig = TextConfig.S20,
    icon: Painter,
    iconConfig: IconConfig = IconConfig.Default,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(10.dp),
    contentPadding: PaddingValues = PaddingValues(18.dp),
    onClick: () -> Unit = {},
) {
    val isEnabled = control.isEnabled.collectAsState().value

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
                Text(
                    text = label,
                    color = labelConfig.color ?: colors.contentColor,
                    fontSize = labelConfig.size,
                    fontWeight = labelConfig.weight,
                    letterSpacing = labelConfig.letterSpacing,
                )

                Text(
                    text = description,
                    color = descriptionConfig.color ?: colors.contentColor,
                    fontSize = descriptionConfig.size,
                    fontWeight = descriptionConfig.weight,
                    letterSpacing = descriptionConfig.letterSpacing,
                )
            }
        }
    }
}

@Composable
fun RichButton(
    control: BaseControl,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    description: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(10.dp),
    contentPadding: PaddingValues = PaddingValues(18.dp),
    onClick: () -> Unit = {},
) {
    val isEnabled = control.isEnabled.collectAsState().value

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
