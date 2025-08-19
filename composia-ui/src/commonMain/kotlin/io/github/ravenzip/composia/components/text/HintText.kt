package io.github.ravenzip.composia.components.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun HintText(modifier: Modifier = Modifier, text: String, color: Color) {
    Text(text = text, modifier = modifier, color = color, fontSize = 12.sp)
}
