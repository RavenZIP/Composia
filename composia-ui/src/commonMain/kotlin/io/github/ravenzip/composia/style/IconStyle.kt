package io.github.ravenzip.composia.style

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import io.github.ravenzip.composia.style.IconStyle.Companion.Default
import io.github.ravenzip.composia.style.IconStyle.Companion.Primary
import io.github.ravenzip.composia.style.IconStyle.Companion.PrimaryS20
import io.github.ravenzip.composia.style.IconStyle.Companion.PrimaryS22
import io.github.ravenzip.composia.style.IconStyle.Companion.S20
import io.github.ravenzip.composia.style.IconStyle.Companion.S22

@Immutable
class IconStyle(val size: Int = 25, val color: Color? = null) {
    companion object {
        /**
         * [S22]
         *
         * [size] - 22
         *
         * [color] - null
         */
        @Stable val S22 = IconStyle(size = 22)

        /**
         * [PrimaryS22]
         *
         * [size] - 22
         *
         * [color] - MaterialTheme.colorScheme.primary
         */
        @Stable
        val PrimaryS22
            @Composable get() = IconStyle(color = MaterialTheme.colorScheme.primary, size = 22)

        /**
         * [Default]
         *
         * [size] - 25
         *
         * [color] - null
         */
        @Stable val Default = IconStyle()

        /**
         * [S20]
         *
         * [size] - 22
         *
         * [color] - null
         */
        @Stable val S20 = IconStyle(size = 20)

        /**
         * [Primary]
         *
         * [size] - 25
         *
         * [color] - MaterialTheme.colorScheme.primary
         */
        @Stable
        val Primary
            @Composable get() = IconStyle(color = MaterialTheme.colorScheme.primary)

        /**
         * [PrimaryS20]
         *
         * [size] - 20
         *
         * [color] - MaterialTheme.colorScheme.primary
         */
        @Stable
        val PrimaryS20
            @Composable get() = IconStyle(color = MaterialTheme.colorScheme.primary, size = 20)
    }
}
