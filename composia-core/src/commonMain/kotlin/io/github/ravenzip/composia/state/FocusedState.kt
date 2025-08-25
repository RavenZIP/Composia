package io.github.ravenzip.composia.state

class FocusedState : BooleanState() {
    fun focus() = setValue(true)

    fun unfocus() = setValue(false)
}
