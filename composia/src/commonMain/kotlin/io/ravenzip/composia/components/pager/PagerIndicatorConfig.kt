package io.ravenzip.composia.components.pager

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Stable
data class PagerIndicatorConfig(
    val height: Int,
    val width: Int,
    val color: Color,
    val shape: Shape,
) {
    companion object {
        @Stable
        val UnselectedCircle =
            PagerIndicatorConfig(
                height = 10,
                width = 10,
                color = Color.LightGray,
                shape = CircleShape,
            )

        @Stable val SelectedCircle = UnselectedCircle.copy(color = Color.DarkGray)

        @Stable
        val UnselectedSquare =
            PagerIndicatorConfig(
                height = 10,
                width = 10,
                color = Color.LightGray,
                shape = RoundedCornerShape(12.dp),
            )

        @Stable val SelectedSquare = UnselectedSquare.copy(color = Color.DarkGray)

        @Stable
        val UnselectedHorizontalRectangle =
            PagerIndicatorConfig(
                height = 10,
                width = 20,
                color = Color.LightGray,
                shape = CircleShape,
            )

        @Stable
        val SelectedHorizontalRectangle = UnselectedHorizontalRectangle.copy(color = Color.DarkGray)

        @Stable
        val UnselectedVerticalRectangle =
            PagerIndicatorConfig(
                height = 20,
                width = 10,
                color = Color.LightGray,
                shape = CircleShape,
            )

        @Stable
        val SelectedVerticalRectangle = UnselectedVerticalRectangle.copy(color = Color.DarkGray)
    }
}
