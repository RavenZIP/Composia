package io.github.ravenzip.composia.state

class ExpandedState : BooleanState() {
    fun expand() = setValue(true)

    fun collapse() = setValue(false)
}
