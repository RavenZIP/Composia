package io.github.ravenzip.composia.components.pager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalHPagerWithIndicator(
    pagerState: PagerState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSize: PageSize = PageSize.Fill,
    userScrollEnabled: Boolean = true,
    pageContent: @Composable (PagerScope.(Int) -> Unit),
) {
    Box(modifier = Modifier.fillMaxSize()) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            pageSize = pageSize,
            userScrollEnabled = userScrollEnabled,
        ) { content ->
            pageContent(content)
        }

        Box(modifier = Modifier.align(Alignment.CenterEnd)) { VerticalPagerIndicator(pagerState) }
    }
}

@Composable
fun HorizontalPagerWithIndicator(
    pagerState: PagerState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSize: PageSize = PageSize.Fill,
    userScrollEnabled: Boolean = true,
    pageContent: @Composable (PagerScope.(Int) -> Unit),
) {
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            pageSize = pageSize,
            userScrollEnabled = userScrollEnabled,
        ) { content ->
            pageContent(content)
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            HorizontalPagerIndicator(pagerState)
        }
    }
}
