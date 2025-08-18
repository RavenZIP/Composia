package io.github.ravenzip.composia

sealed class ControlStatus {
    object Disabled : ControlStatus()

    data class Invalid(val message: String) : ControlStatus()

    object Valid : ControlStatus()
}
