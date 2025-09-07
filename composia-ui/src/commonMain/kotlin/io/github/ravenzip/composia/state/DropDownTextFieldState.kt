package io.github.ravenzip.composia.state

class DropDownTextFieldState(readonly: Boolean = false) : TextFieldState(readonly) {
    val expandedState = ExpandedState()
}
