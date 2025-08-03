package io.ravenzip.composia.components.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
private fun PagerIndicator(
    pagerState: PagerState,
    selectedIndicatorConfig: PagerIndicatorConfig,
    unselectedIndicatorConfig: PagerIndicatorConfig,
) {
    repeat(pagerState.pageCount) { page ->
        val config =
            if (pagerState.currentPage == page) selectedIndicatorConfig
            else unselectedIndicatorConfig

        Box(
            modifier =
                Modifier.padding(2.dp)
                    .clip(config.shape)
                    .background(config.color)
                    .size(height = config.height.dp, width = config.width.dp)
        )
    }
}

@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    selectedIndicatorConfig: PagerIndicatorConfig =
        PagerIndicatorConfig.SelectedHorizontalRectangle,
    unselectedIndicatorConfig: PagerIndicatorConfig =
        PagerIndicatorConfig.UnselectedHorizontalRectangle,
    padding: PaddingValues = PaddingValues(bottom = 10.dp),
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(padding),
        horizontalArrangement = Arrangement.Center,
    ) {
        PagerIndicator(pagerState, selectedIndicatorConfig, unselectedIndicatorConfig)
    }
}

@Composable
fun VerticalPagerIndicator(
    pagerState: PagerState,
    selectedIndicatorConfig: PagerIndicatorConfig = PagerIndicatorConfig.SelectedVerticalRectangle,
    unselectedIndicatorConfig: PagerIndicatorConfig =
        PagerIndicatorConfig.UnselectedVerticalRectangle,
    padding: PaddingValues = PaddingValues(end = 10.dp),
) {
    Column(
        modifier = Modifier.fillMaxHeight().padding(padding),
        verticalArrangement = Arrangement.Center,
    ) {
        PagerIndicator(pagerState, selectedIndicatorConfig, unselectedIndicatorConfig)
    }
}
