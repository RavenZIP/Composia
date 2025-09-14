package io.github.ravenzip.composia.validation

import kotlinx.coroutines.flow.StateFlow

interface Validatable<T> {
    val isValid: Boolean
    val isInvalid: Boolean
    val errorMessage: String?
    val isValidFlow: StateFlow<Boolean>
    val isInvalidFlow: StateFlow<Boolean>
    val errorMessageFlow: StateFlow<String?>
}
