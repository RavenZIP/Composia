package io.ravenzip.composia.components.pager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VerticalHPagerWithIndicator(
    pagerState: PagerState,
    pageContent: @Composable (PagerScope.(Int) -> Unit),
) {
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { content ->
            pageContent(content)
        }

        Box(modifier = Modifier.fillMaxSize()) { VerticalPagerIndicator(pagerState) }
    }
}

@Composable
fun HorizontalPagerWithIndicator(
    pagerState: PagerState,
    pageContent: @Composable (PagerScope.(Int) -> Unit),
) {
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { content ->
            pageContent(content)
        }

        Box(modifier = Modifier.fillMaxSize()) { HorizontalPagerIndicator(pagerState) }
    }
}
