package io.github.ravenzip.composia.config

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.github.ravenzip.composia.config.TextConfig.Companion.S12
import io.github.ravenzip.composia.config.TextConfig.Companion.S14
import io.github.ravenzip.composia.config.TextConfig.Companion.S16
import io.github.ravenzip.composia.config.TextConfig.Companion.S18
import io.github.ravenzip.composia.config.TextConfig.Companion.S20
import io.github.ravenzip.composia.config.TextConfig.Companion.S22
import io.github.ravenzip.composia.config.TextConfig.Companion.S24

@Immutable
class TextConfig(
    val size: TextUnit = TextUnit.Unspecified,
    val color: Color? = null,
    val align: TextAlign = TextAlign.Unspecified,
    val weight: FontWeight = FontWeight.Normal,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
) {
    companion object {
        /**
         * [S12]
         *
         * [size] - 12.sp
         *
         * [color] - null
         *
         * [align] - TextAlign.Unspecified
         *
         * [weight] - FontWeight.Normal
         *
         * [letterSpacing] - TextUnit.Unspecified
         */
        @Stable val S12 = TextConfig(size = 12.sp)

        /**
         * [S14]
         *
         * [size] - 14.sp
         *
         * [color] - null
         *
         * [align] - TextAlign.Unspecified
         *
         * [weight] - FontWeight.Normal
         *
         * [letterSpacing] - TextUnit.Unspecified
         */
        @Stable val S14 = TextConfig(size = 14.sp)

        /**
         * [S16]
         *
         * [size] - 16.sp
         *
         * [color] - null
         *
         * [align] - TextAlign.Unspecified
         *
         * [weight] - FontWeight.Normal
         *
         * [letterSpacing] - TextUnit.Unspecified
         */
        @Stable val S16 = TextConfig(size = 16.sp)

        /**
         * [S18]
         *
         * [size] - 18.sp
         *
         * [color] - null
         *
         * [align] - TextAlign.Unspecified
         *
         * [weight] - FontWeight.Normal
         *
         * [letterSpacing] - TextUnit.Unspecified
         */
        @Stable val S18 = TextConfig(size = 18.sp)

        /**
         * [S20]
         *
         * [size] - 20.sp
         *
         * [color] - null
         *
         * [align] - TextAlign.Unspecified
         *
         * [weight] - FontWeight.Normal
         *
         * [letterSpacing] - TextUnit.Unspecified
         */
        @Stable val S20 = TextConfig(size = 20.sp)

        /**
         * [S22]
         *
         * [size] - 22.sp
         *
         * [color] - null
         *
         * [align] - TextAlign.Unspecified
         *
         * [weight] - FontWeight.Normal
         *
         * [letterSpacing] - TextUnit.Unspecified
         */
        @Stable val S22 = TextConfig(size = 22.sp)

        /**
         * [S24]
         *
         * [size] - 24.sp
         *
         * [color] - null
         *
         * [align] - TextAlign.Unspecified
         *
         * [weight] - FontWeight.Normal
         *
         * [letterSpacing] - TextUnit.Unspecified
         */
        @Stable val S24 = TextConfig(size = 24.sp)

        /**
         * [size] - 16.sp
         *
         * [color] - null
         *
         * [align] - TextAlign.Center
         *
         * [weight] - FontWeight.Medium
         *
         * [letterSpacing] - TextUnit.Unspecified
         */
        @Stable
        val S16Medium =
            TextConfig(size = 16.sp, align = TextAlign.Center, weight = FontWeight.Medium)

        /**
         * [size] - 18.sp
         *
         * [color] - null
         *
         * [align] - TextAlign.Center
         *
         * [weight] - FontWeight.Medium
         *
         * [letterSpacing] - TextUnit.Unspecified
         */
        @Stable
        val S18Medium =
            TextConfig(size = 18.sp, align = TextAlign.Center, weight = FontWeight.Medium)
    }
}
