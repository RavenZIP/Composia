package io.github.ravenzip.composia.components.textField.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import io.github.ravenzip.composia.control.formControl.CompositeControl
import io.github.ravenzip.composia.extension.update
import io.github.ravenzip.composia.function.searchElementsByQuery
import io.github.ravenzip.composia.state.DropDownTextFieldState
import kotlinx.coroutines.flow.*

@Composable
internal fun <T> LoadSearchResultOnExpand(
    control: CompositeControl<T>,
    state: DropDownTextFieldState,
    source: SnapshotStateList<T>,
    sourceItemToString: (T) -> String,
    searchQuery: String,
    results: SnapshotStateList<T>,
) {
    LaunchedEffect(control, state, searchQuery) {
        state.expandedState.valueFlow
            .filter { expanded -> expanded }
            .onEach {
                if (searchQuery.isEmpty()) {
                    results.update(source)
                } else {
                    results.update(searchElementsByQuery(source, sourceItemToString, searchQuery))
                }
            }
            .launchIn(this)
    }
}

@Composable
internal fun <T> UpdateSearchQueryOnControlOrExpandChange(
    control: CompositeControl<T>,
    state: DropDownTextFieldState,
    sourceItemToString: (T) -> String,
    onSearchQueryChange: (String) -> Unit,
) {
    LaunchedEffect(control, state) {
        merge(
                control.valueFlow
                    .map { value -> sourceItemToString(value) }
                    .filter { value -> value.isNotEmpty() },
                state.expandedState.valueFlow
                    .filter { expanded -> !expanded && control.isInvalid }
                    .map { sourceItemToString(control.initialValue) },
            )
            .onEach { value -> onSearchQueryChange(value) }
            .launchIn(this)
    }
}

internal fun calculateLabelColor() {}
