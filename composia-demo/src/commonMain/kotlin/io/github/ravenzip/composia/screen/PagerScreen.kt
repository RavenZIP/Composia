package io.github.ravenzip.composia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ravenzip.composia.components.button.SimpleButton
import io.github.ravenzip.composia.components.pager.HorizontalPagerWithIndicator
import io.github.ravenzip.composia.components.pager.VerticalHPagerWithIndicator
import kotlinx.coroutines.launch

@Composable
fun PagerScreen(padding: PaddingValues, backToMenu: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val isHorizontalPager = remember { mutableStateOf(true) }
    val nextPagerName =
        remember(isHorizontalPager.value) {
            if (isHorizontalPager.value) "VerticalPager" else "HorizontalPager"
        }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        if (isHorizontalPager.value) {
            HorizontalPagerWithIndicator(pagerState) { screen ->
                PagerContent(
                    screen,
                    nextPagerName = nextPagerName,
                    nextPage = {
                        scope.launch { pagerState.scrollToPage(pagerState.currentPage + 1) }
                    },
                    previousPage = {
                        scope.launch { pagerState.scrollToPage(pagerState.currentPage - 1) }
                    },
                    changePager = { isHorizontalPager.value = !isHorizontalPager.value },
                    backToMenu = backToMenu,
                )
            }
        } else {
            VerticalHPagerWithIndicator(pagerState) { screen ->
                PagerContent(
                    screen,
                    nextPagerName = nextPagerName,
                    nextPage = {
                        scope.launch { pagerState.scrollToPage(pagerState.currentPage + 1) }
                    },
                    previousPage = {
                        scope.launch { pagerState.scrollToPage(pagerState.currentPage - 1) }
                    },
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
    nextPage: () -> Unit,
    previousPage: () -> Unit,
    changePager: () -> Unit,
    backToMenu: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "Current screen number is: ${screen + 1}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.height(10.dp))

            SimpleButton(onClick = nextPage, text = "Next page")

            SimpleButton(onClick = previousPage, text = "Previous page")

            SimpleButton(onClick = changePager, text = "Change to $nextPagerName")

            SimpleButton(onClick = backToMenu, text = "Back to menu")
        }
    }
}
