package io.github.ravenzip.composia.state

open class TextFieldState(readonly: Boolean = false) {
    val focusedState = FocusedState()

    val readonlyState = ReadonlyState(readonly)
}
