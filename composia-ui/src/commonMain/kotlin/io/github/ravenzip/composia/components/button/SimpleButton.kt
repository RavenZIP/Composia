package io.github.ravenzip.composia.components.button

import androidx.compose.foundation.layout.PaddingValues
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
import io.github.ravenzip.composia.control.activation.ActivationControl
import io.github.ravenzip.composia.extension.S16Medium
import io.github.ravenzip.composia.style.DefaultComponentShape

@Composable
fun SimpleButton(
    onClick: () -> Unit = {},
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.S16Medium,
    isEnabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = DefaultComponentShape,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
) {
    Button(
        onClick = onClick,
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

@Composable
fun SimpleButton(
    control: ActivationControl,
    onClick: () -> Unit = {},
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.S16Medium,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = DefaultComponentShape,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
) {
    val isEnabled = control.enabledState.collectAsState().value

    SimpleButton(
        onClick = onClick,
        text = text,
        modifier = modifier,
        textStyle = textStyle,
        isEnabled = isEnabled,
        colors = colors,
        shape = shape,
        contentPadding = contentPadding,
    )
}
