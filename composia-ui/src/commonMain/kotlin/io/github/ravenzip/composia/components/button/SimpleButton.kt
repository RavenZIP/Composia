package io.github.ravenzip.composia.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.control.statusControl.StatusControl
import io.github.ravenzip.composia.extension.S16Medium

@Composable
fun SimpleButton(
    control: StatusControl,
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextStyle.S16Medium,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(14.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
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
        // Игнорируем textStyle.color, потому что цветом будет управлять сама кнопка при помощи
        // contentColor
        Text(text = text, style = textStyle.merge(color = colors.contentColor))
    }
}
