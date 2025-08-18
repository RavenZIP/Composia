package io.github.ravenzip.composia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(
    padding: PaddingValues,
    navigateToPagerScreen: () -> Unit,
    navigateToButtonScreen: () -> Unit,
    navigateToTextFieldScreen: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        Text(text = "ComposiaDemo menu", fontSize = 25.sp, fontWeight = FontWeight.W500)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navigateToPagerScreen() }) { Text("Pager") }

        Button(onClick = { navigateToButtonScreen() }) { Text("Button") }

        Button(onClick = { navigateToTextFieldScreen() }) { Text("TextField") }
    }
}
