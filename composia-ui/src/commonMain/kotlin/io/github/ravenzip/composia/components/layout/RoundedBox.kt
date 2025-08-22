package io.github.ravenzip.composia.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun RoundedBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    shape: Shape = RoundedCornerShape(10.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = Modifier.clip(shape).background(backgroundColor).then(modifier)) { content() }
}
