package io.github.ravenzip.composia.state

import io.github.ravenzip.composia.shared.BooleanState

class ReadonlyState(readonly: Boolean = false) : BooleanState(readonly) {
    fun makeReadonly() = setValue(true)

    fun makeEditable() = setValue(false)
}
