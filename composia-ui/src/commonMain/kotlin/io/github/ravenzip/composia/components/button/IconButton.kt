package io.github.ravenzip.composia.components.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
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
    onClick: () -> Unit,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val color = iconStyle.color ?: MaterialTheme.colorScheme.primary
    val containerColor = if (isEnabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (isEnabled) colors.contentColor else colors.disabledContentColor

    Surface(
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        enabled = isEnabled,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        interactionSource = interactionSource,
    ) {
        Icon(
            painter = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(iconStyle.size.dp),
            tint = color,
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
        onClick = onClick,
    )
}
