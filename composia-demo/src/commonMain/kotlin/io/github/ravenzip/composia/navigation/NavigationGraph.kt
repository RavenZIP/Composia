package io.github.ravenzip.composia.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.ravenzip.composia.screen.ButtonScreen
import io.github.ravenzip.composia.screen.MenuScreen
import io.github.ravenzip.composia.screen.PagerScreen
import io.github.ravenzip.composia.screen.TextFieldScreen
import io.github.ravenzip.composia.screen.checkbox.CheckboxScreen
import io.github.ravenzip.composia.screen.dropDownTextField.DropDownTextFieldScreen

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
                navigateToButtonScreen = { navController.navigate(NavigationPath.BUTTON) },
                navigateToTextFieldScreen = { navController.navigate(NavigationPath.TEXT_FIELD) },
                navigateToCheckboxScreen = { navController.navigate(NavigationPath.CHECKBOX) },
            )
        }

        composable(route = NavigationPath.PAGER) {
            PagerScreen(padding = padding, backToMenu = { navController.popBackStack() })
        }

        composable(route = NavigationPath.BUTTON) {
            ButtonScreen(padding = padding, backToMenu = { navController.popBackStack() })
        }

        composable(route = NavigationPath.TEXT_FIELD) {
            TextFieldScreen(
                padding = padding,
                navigateToDropDownTextFieldScreen = {
                    navController.navigate(NavigationPath.DROP_DOWN_TEXT_FIELD)
                },
                backToMenu = { navController.popBackStack() },
            )
        }

        composable(route = NavigationPath.DROP_DOWN_TEXT_FIELD) {
            DropDownTextFieldScreen(
                padding = padding,
                backToMenu = { navController.popBackStack() },
            )
        }

        composable(route = NavigationPath.CHECKBOX) {
            CheckboxScreen(padding = padding, backToMenu = { navController.popBackStack() })
        }
    }
}
