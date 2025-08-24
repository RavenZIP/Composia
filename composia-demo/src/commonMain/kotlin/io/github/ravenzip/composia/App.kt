package io.github.ravenzip.composia

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import io.github.ravenzip.composia.navigation.NavigationGraph

@Composable
fun App() {
    val navController = rememberNavController()

    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavigationGraph(navController, innerPadding)
        }
    }
}
