package io.github.ravenzip.composia.components.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.components.layout.RoundedBox
import io.github.ravenzip.composia.control.statusControl.StatusControl
import io.github.ravenzip.composia.style.IconStyle

// TODO реализовать enabled\disabled
@Composable
fun IconButton(
    icon: Painter,
    iconDescription: String? = null,
    iconStyle: IconStyle = IconStyle.Default,
    backgroundColor: Color,
    shape: Shape = RoundedCornerShape(10.dp),
    onClick: () -> Unit,
) {
    val color = iconStyle.color ?: MaterialTheme.colorScheme.primary

    RoundedBox(
        modifier = Modifier.clickable { onClick() },
        backgroundColor = backgroundColor,
        shape = shape,
    ) {
        Icon(
            painter = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(iconStyle.size.dp),
            tint = color,
        )
    }
}

// TODO реализовать enabled\disabled с использованием control
@Composable
fun IconButton(
    control: StatusControl,
    icon: Painter,
    iconDescription: String? = null,
    iconStyle: IconStyle = IconStyle.Default,
    backgroundColor: Color,
    shape: Shape = RoundedCornerShape(10.dp),
    onClick: () -> Unit,
) {
    val color = iconStyle.color ?: MaterialTheme.colorScheme.primary

    RoundedBox(
        modifier = Modifier.clickable { onClick() },
        backgroundColor = backgroundColor,
        shape = shape,
    ) {
        Icon(
            painter = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(iconStyle.size.dp),
            tint = color,
        )
    }
}
