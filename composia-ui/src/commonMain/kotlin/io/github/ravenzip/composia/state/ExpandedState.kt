package io.github.ravenzip.composia.state

import io.github.ravenzip.composia.shared.BooleanState

class ExpandedState : BooleanState() {
    fun expand() = setValue(true)

    fun collapse() = setValue(false)
}
