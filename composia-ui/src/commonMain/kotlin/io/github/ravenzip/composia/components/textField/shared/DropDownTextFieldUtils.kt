package io.github.ravenzip.composia.components.textField.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import io.github.ravenzip.composia.components.model.DataSource
import io.github.ravenzip.composia.control.validatable.ValidatableControl
import io.github.ravenzip.composia.extension.update
import io.github.ravenzip.composia.function.searchElementsByQuery
import io.github.ravenzip.composia.state.DropDownTextFieldState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
private fun combineExpandedStateWithSearchQuery(
    expandedStateFlow: Flow<Boolean>,
    searchQueryFlow: Flow<String>,
): Flow<Pair<Boolean, String>> {
    return combine(expandedStateFlow, searchQueryFlow.debounce { 500 }.distinctUntilChanged()) {
            expanded,
            searchQuery ->
            Pair(expanded, searchQuery)
        }
        .filter { x -> x.first }
}

@Composable
internal fun <T> loadSearchResult(
    state: DropDownTextFieldState,
    source: DataSource.Predefined<T>,
    sourceItemToString: (T) -> String,
    searchQueryFlow: Flow<String>,
    results: SnapshotStateList<T>,
) {
    LaunchedEffect(state) {
        combineExpandedStateWithSearchQuery(state.expandedState.valueChanges, searchQueryFlow)
            .map { x ->
                if (x.second.isEmpty()) source.items
                else searchElementsByQuery(source.items, sourceItemToString, x.second)
            }
            .onEach { items -> results.update(items) }
            .launchIn(this)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
internal fun <T> loadSearchResult(
    state: DropDownTextFieldState,
    source: DataSource.ByQuery<T>,
    searchQueryFlow: Flow<String>,
    changeIsLoadingTo: (Boolean) -> Unit,
    results: SnapshotStateList<T>,
) {
    LaunchedEffect(state) {
        combineExpandedStateWithSearchQuery(state.expandedState.valueChanges, searchQueryFlow)
            .onEach {
                changeIsLoadingTo(true)
                results.clear()
            }
            .flatMapLatest { x -> source.query(x.second) }
            .onEach { response ->
                results.addAll(response)
                changeIsLoadingTo(false)
            }
            .launchIn(this)
    }
}

@Composable
internal fun <T> updateSearchQueryOnControlOrExpandChange(
    control: ValidatableControl<T>,
    state: DropDownTextFieldState,
    sourceItemToString: (T) -> String,
    onSearchQueryChange: (String) -> Unit,
) {
    LaunchedEffect(control, state) {
        merge(
                control.valueEvents
                    .map { value -> sourceItemToString(value) }
                    .filter { value -> value.isNotEmpty() },
                state.expandedState.valueChanges
                    .filter { expanded -> !expanded && control.isInvalid }
                    .map { sourceItemToString(control.defaultResetValue) },
            )
            .onEach { value -> onSearchQueryChange(value) }
            .launchIn(this)
    }
}

@Composable
internal fun <T> turnOffProgressIndicatorStateOnSourceChange(
    source: DataSource<T>,
    isLoading: Boolean,
    turnOffProgressIndicator: () -> Unit,
) {
    LaunchedEffect(source) { if (isLoading) turnOffProgressIndicator() }
}
