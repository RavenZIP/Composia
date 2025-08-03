package io.ravenzip.composia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ravenzip.composia.components.pager.HorizontalPagerWithIndicator
import io.ravenzip.composia.components.pager.VerticalHPagerWithIndicator

@Composable
fun PagerScreen(padding: PaddingValues, backToMenu: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val isHorizontalPager = remember { mutableStateOf(true) }
    val nextPagerName =
        remember(isHorizontalPager.value) {
            if (isHorizontalPager.value) "VerticalPager" else "HorizontalPager"
        }

    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        if (isHorizontalPager.value) {
            HorizontalPagerWithIndicator(pagerState) { screen ->
                PagerContent(
                    screen,
                    nextPagerName = nextPagerName,
                    changePager = { isHorizontalPager.value = !isHorizontalPager.value },
                    backToMenu = backToMenu,
                )
            }
        } else {
            VerticalHPagerWithIndicator(pagerState) { screen ->
                PagerContent(
                    screen,
                    nextPagerName = nextPagerName,
                    changePager = { isHorizontalPager.value = !isHorizontalPager.value },
                    backToMenu = backToMenu,
                )
            }
        }
    }
}

@Composable
fun PagerContent(
    screen: Int,
    nextPagerName: String,
    changePager: () -> Unit,
    backToMenu: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(text = "Current screen number is: ${screen + 1}")

            Button({ changePager() }) { Text(text = "Change to $nextPagerName") }

            Button({ backToMenu() }) { Text(text = "Back to menu") }
        }
    }
}
