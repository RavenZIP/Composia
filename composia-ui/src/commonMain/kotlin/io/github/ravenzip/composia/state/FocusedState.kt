package io.github.ravenzip.composia.state

import io.github.ravenzip.composia.shared.BooleanState

class FocusedState : BooleanState() {
    fun focus() = setValue(true)

    fun unfocus() = setValue(false)
}
