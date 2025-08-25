package io.github.ravenzip.composia.state

class ReadonlyState(readonly: Boolean = false) : BooleanState(readonly) {
    fun makeReadonly() = setValue(true)

    fun makeEditable() = setValue(false)
}
