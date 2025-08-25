package io.github.ravenzip.composia.components.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp

@Composable
fun ConditionalIcon(
    condition: Boolean,
    trueIcon: Painter,
    falseIcon: Painter,
    iconDescription: String?,
    size: Dp,
    color: Color,
) {
    val icon = if (condition) trueIcon else falseIcon

    Icon(
        painter = icon,
        contentDescription = iconDescription,
        modifier = Modifier.size(size),
        tint = color,
    )
}
