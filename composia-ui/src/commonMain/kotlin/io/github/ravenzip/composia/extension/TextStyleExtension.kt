package io.github.ravenzip.composia.extension

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// S14
@Stable
val TextStyle.Companion.S14: TextStyle
    @Composable get() = LocalTextStyle.current.merge(fontSize = 14.sp)

fun TextStyle.s14(): TextStyle = this.merge(fontSize = 14.sp)

// S16Medium
@Stable
val TextStyle.Companion.S16Medium: TextStyle
    @Composable
    get() = LocalTextStyle.current.merge(fontSize = 16.sp, fontWeight = FontWeight.Medium)

fun TextStyle.s16Medium(): TextStyle = this.merge(fontSize = 16.sp, fontWeight = FontWeight.Medium)

// S18Medium
@Stable
val TextStyle.Companion.S18Medium: TextStyle
    @Composable
    get() = LocalTextStyle.current.merge(fontSize = 18.sp, fontWeight = FontWeight.Medium)

fun TextStyle.s18Medium(): TextStyle = this.merge(fontSize = 18.sp, fontWeight = FontWeight.Medium)

// S20
@Stable
val TextStyle.Companion.S20: TextStyle
    @Composable get() = LocalTextStyle.current.merge(fontSize = 20.sp)

fun TextStyle.s20(): TextStyle = this.merge(fontSize = 20.sp)

// WithoutLetterSpacing
@Stable
val TextStyle.Companion.WithoutLetterSpacing: TextStyle
    @Composable get() = LocalTextStyle.current.merge(letterSpacing = 0.sp)

fun TextStyle.withoutLetterSpacing(): TextStyle = this.merge(letterSpacing = 0.sp)
