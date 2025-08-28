package io.github.ravenzip.composia.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class ProgressIndicatorStyle(val size: Dp, val strokeWith: Dp) {
    companion object {
        @Stable val Default = ProgressIndicatorStyle(20.dp, 2.dp)
    }
}
