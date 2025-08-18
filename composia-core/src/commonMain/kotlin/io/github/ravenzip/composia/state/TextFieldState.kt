package io.github.ravenzip.composia.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TextFieldState(val readonly: Boolean = false) {
    private val _isFocused = MutableStateFlow(false)
    private val _isReadonly = MutableStateFlow(readonly)

    val isReadonly = _isReadonly.asStateFlow()
    val isFocused = _isFocused.asStateFlow()

    fun changeToReadonly() {
        _isReadonly.update { true }
    }

    fun changeToEditable() {
        _isReadonly.update { false }
    }

    fun setReadonly(value: Boolean) {
        _isReadonly.update { value }
    }

    fun changeToFocused() {
        _isFocused.update { true }
    }

    fun changeToUnfocused() {
        _isFocused.update { false }
    }

    fun setFocus(value: Boolean) {
        _isFocused.update { value }
    }
}
