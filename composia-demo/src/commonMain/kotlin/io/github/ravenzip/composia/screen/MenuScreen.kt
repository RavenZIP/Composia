package io.github.ravenzip.composia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ravenzip.composia.components.button.SimpleButton

@Composable
fun MenuScreen(
    padding: PaddingValues,
    navigateToPagerScreen: () -> Unit,
    navigateToButtonScreen: () -> Unit,
    navigateToTextFieldScreen: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            Text(text = "Composia menu", fontSize = 25.sp, fontWeight = FontWeight.W500)

            Spacer(modifier = Modifier.height(20.dp))

            SimpleButton(onClick = { navigateToPagerScreen() }, text = "Pager")

            SimpleButton(onClick = { navigateToButtonScreen() }, text = "Button")

            SimpleButton(onClick = { navigateToTextFieldScreen() }, text = "TextField")
        }
    }
}
