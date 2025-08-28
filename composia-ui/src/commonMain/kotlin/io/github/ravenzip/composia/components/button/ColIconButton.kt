package io.github.ravenzip.composia.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.control.statusControl.StatusControl
import io.github.ravenzip.composia.extension.S18Medium
import io.github.ravenzip.composia.style.DefaultComponentShape
import io.github.ravenzip.composia.style.IconStyle

@Composable
fun ColIconButton(
    text: String,
    textStyle: TextStyle = TextStyle.S18Medium,
    icon: Painter,
    iconDescription: String? = null,
    iconStyle: IconStyle = IconStyle.Default,
    isEnabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = DefaultComponentShape,
    interactionSource: MutableInteractionSource? = null,
    contentPadding: PaddingValues = PaddingValues(10.dp),
    onClick: () -> Unit = {},
) {
    val iconColor = iconStyle.color ?: colors.contentColor
    val textColor =
        if (textStyle.color != Color.Unspecified) textStyle.color else colors.contentColor
    val containerColor = if (isEnabled) colors.containerColor else colors.disabledContainerColor
    val currentIconColor = if (isEnabled) iconColor else colors.disabledContentColor
    val currentTextColor = if (isEnabled) textColor else colors.disabledContentColor

    Column(
        modifier =
            Modifier.clip(shape)
                .background(containerColor)
                .clickable(
                    onClick = onClick,
                    enabled = isEnabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                )
                .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            painter = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(iconStyle.size),
            tint = currentIconColor,
        )

        Text(text = text, style = textStyle.merge(color = currentTextColor))
    }
}

@Composable
fun ColIconButton(
    control: StatusControl,
    text: String,
    textStyle: TextStyle = TextStyle.S18Medium,
    icon: Painter,
    iconDescription: String? = null,
    iconStyle: IconStyle = IconStyle.Default,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = DefaultComponentShape,
    interactionSource: MutableInteractionSource? = null,
    contentPadding: PaddingValues = PaddingValues(10.dp),
    onClick: () -> Unit = {},
) {
    val isEnabled = control.isEnabledFlow.collectAsState().value

    ColIconButton(
        text = text,
        textStyle = textStyle,
        icon = icon,
        iconDescription = iconDescription,
        iconStyle = iconStyle,
        isEnabled = isEnabled,
        colors = colors,
        shape = shape,
        interactionSource = interactionSource,
        contentPadding = contentPadding,
        onClick = onClick,
    )
}
