package io.github.ravenzip.composia.components.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.components.layout.RoundedBox
import io.github.ravenzip.composia.control.statusControl.StatusControl
import io.github.ravenzip.composia.style.IconStyle

@Composable
fun IconButton(
    icon: Painter,
    iconDescription: String? = null,
    iconStyle: IconStyle = IconStyle.Default,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    interactionSource: MutableInteractionSource? = null,
    contentPadding: PaddingValues = PaddingValues(10.dp),
    onClick: () -> Unit,
) {
    val color = iconStyle.color ?: colors.contentColor
    val containerColor = if (isEnabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (isEnabled) color else colors.disabledContentColor

    RoundedBox(
        modifier =
            Modifier.clickable(
                    onClick = onClick,
                    enabled = isEnabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                )
                .padding(contentPadding)
                .then(modifier),
        shape = shape,
        backgroundColor = containerColor,
    ) {
        Icon(
            painter = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(iconStyle.size.dp),
            tint = contentColor,
        )
    }
}

@Composable
fun IconButton(
    control: StatusControl,
    modifier: Modifier = Modifier,
    icon: Painter,
    iconDescription: String? = null,
    iconStyle: IconStyle = IconStyle.Default,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit,
) {
    val modifiedColors = colors.copy(contentColor = iconStyle.color ?: colors.contentColor)
    val isEnabled = control.isEnabledFlow.collectAsState().value

    IconButton(
        icon = icon,
        iconDescription = iconDescription,
        iconStyle = iconStyle,
        modifier = modifier,
        isEnabled = isEnabled,
        shape = shape,
        colors = modifiedColors,
        interactionSource = interactionSource,
        onClick = onClick,
    )
}
