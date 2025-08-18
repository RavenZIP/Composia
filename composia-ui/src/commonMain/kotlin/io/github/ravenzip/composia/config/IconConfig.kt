package io.github.ravenzip.composia.config

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import io.github.ravenzip.composia.config.IconConfig.Companion.Default
import io.github.ravenzip.composia.config.IconConfig.Companion.Primary
import io.github.ravenzip.composia.config.IconConfig.Companion.PrimaryS20
import io.github.ravenzip.composia.config.IconConfig.Companion.PrimaryS22
import io.github.ravenzip.composia.config.IconConfig.Companion.S20
import io.github.ravenzip.composia.config.IconConfig.Companion.S22

@Immutable
class IconConfig(val description: String = "", val size: Int = 25, val color: Color? = null) {
    companion object {
        /**
         * [S22]
         *
         * [description] - ""
         *
         * [size] - 22
         *
         * [color] - null
         */
        @Stable val S22 = IconConfig(size = 22)

        /**
         * [PrimaryS22]
         *
         * [description] - ""
         *
         * [size] - 22
         *
         * [color] - MaterialTheme.colorScheme.primary
         */
        @Stable
        val PrimaryS22
            @Composable get() = IconConfig(color = MaterialTheme.colorScheme.primary, size = 22)

        /**
         * [Default]
         *
         * [description] - ""
         *
         * [size] - 25
         *
         * [color] - null
         */
        @Stable val Default = IconConfig()

        /**
         * [S20]
         *
         * [description] - ""
         *
         * [size] - 22
         *
         * [color] - null
         */
        @Stable val S20 = IconConfig(size = 20)

        /**
         * [Primary]
         *
         * [description] - ""
         *
         * [size] - 25
         *
         * [color] - MaterialTheme.colorScheme.primary
         */
        @Stable
        val Primary
            @Composable get() = IconConfig(color = MaterialTheme.colorScheme.primary)

        /**
         * [PrimaryS20]
         *
         * [description] - ""
         *
         * [size] - 20
         *
         * [color] - MaterialTheme.colorScheme.primary
         */
        @Stable
        val PrimaryS20
            @Composable get() = IconConfig(color = MaterialTheme.colorScheme.primary, size = 20)
    }
}
