package io.ravenzip.composia.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.ravenzip.composia.screen.MenuScreen
import io.ravenzip.composia.screen.PagerScreen

@Composable
fun NavigationGraph(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        route = NavigationPath.ROOT,
        startDestination = NavigationPath.MENU,
    ) {
        composable(route = NavigationPath.MENU) {
            MenuScreen(
                padding = padding,
                navigateToPagerScreen = { navController.navigate(NavigationPath.PAGER) },
            )
        }

        composable(route = NavigationPath.PAGER) {
            PagerScreen(padding = padding, backToMenu = { navController.popBackStack() })
        }

        composable(route = NavigationPath.BUTTON) {
            // TODO
        }

        composable(route = NavigationPath.TEXT_FIELD) {
            // TODO
        }
    }
}
