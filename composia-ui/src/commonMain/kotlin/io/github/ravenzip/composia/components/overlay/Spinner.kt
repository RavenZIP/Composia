package io.github.ravenzip.composia.components.overlay

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.ravenzip.composia.extension.S16Medium
import io.github.ravenzip.composia.extension.withoutLetterSpacing

@Composable
fun Spinner(
    text: String? = null,
    textStyle: TextStyle = TextStyle.S16Medium.withoutLetterSpacing(),
    indicatorSize: Dp = 50.dp,
    containerColors: CardColors = CardDefaults.cardColors(),
    shape: Shape = RoundedCornerShape(14.dp),
) {
    Dialog(onDismissRequest = {}) {
        Card(shape = shape, colors = containerColors) {
            Column(
                modifier = Modifier.padding(20.dp).widthIn(0.dp, 250.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(modifier = Modifier.size(indicatorSize))

                if (text != null) {
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Text(text = text, style = textStyle)
                }
            }
        }
    }
}
