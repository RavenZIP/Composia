package io.github.ravenzip.composia.components.textField.dropdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.components.icon.ConditionalIcon
import io.github.ravenzip.composia.components.textField.outlined.OutlinedSingleLineTextField
import io.github.ravenzip.composia.components.textField.shared.LoadSearchResultOnExpand
import io.github.ravenzip.composia.components.textField.shared.ResetReadonlyStateOnResetValue
import io.github.ravenzip.composia.components.textField.shared.UpdateSearchQueryOnControlOrExpandChange
import io.github.ravenzip.composia.components.textField.shared.calculateLabelColor
import io.github.ravenzip.composia.control.compositeControl.CompositeControl
import io.github.ravenzip.composia.extension.update
import io.github.ravenzip.composia.function.searchElementsByQuery
import io.github.ravenzip.composia.state.DropDownTextFieldState
import io.github.ravenzip.composia.style.IconStyle
import io.github.ravenzip.composia_ui.composia_ui.generated.resources.Res
import io.github.ravenzip.composia_ui.composia_ui.generated.resources.i_angle_down
import io.github.ravenzip.composia_ui.composia_ui.generated.resources.i_angle_up
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownTextField(
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    onSelectItem: (T) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchResults: SnapshotStateList<T>,
    sourceItemToString: (T) -> String,
    isEnabled: Boolean = true,
    isReadonly: Boolean = false,
    isInvalid: Boolean = false,
    errorMessage: String = "",
    isFocused: Boolean,
    onFocusChange: (FocusState) -> Unit = {},
    isExpanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit,
    label: @Composable () -> Unit,
    dropDownIcon: @Composable () -> Unit,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    Column(modifier = modifier) {
        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = onExpandedChange) {
            OutlinedSingleLineTextField(
                value = searchQuery,
                onValueChange = { x -> onSearchQueryChange(x) },
                modifier =
                    Modifier.fillMaxWidth()
                        .menuAnchor(
                            if (isReadonly) MenuAnchorType.PrimaryNotEditable
                            else MenuAnchorType.PrimaryEditable
                        ),
                isEnabled = isEnabled,
                isReadonly = isReadonly,
                isInvalid = isInvalid,
                errorMessage = errorMessage,
                isFocused = isFocused,
                onFocusChange = onFocusChange,
                label = { label() },
                trailingIcon = { dropDownIcon() },
                shape = shape,
                colors = colors,
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onExpandedChange(false) },
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                if (searchResults.isNotEmpty()) {
                    searchResults.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = sourceItemToString(item)) },
                            onClick = { onSelectItem(item) },
                        )
                    }
                } else {
                    DropdownMenuItem(
                        text = { Text(text = "Нет результатов") },
                        onClick = {},
                        enabled = false,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownTextField(
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    onSelectItem: (T) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchResults: SnapshotStateList<T>,
    sourceItemToString: (T) -> String,
    isEnabled: Boolean = true,
    isReadonly: Boolean = false,
    isInvalid: Boolean = false,
    errorMessage: String = "",
    isFocused: Boolean,
    onFocusChange: (FocusState) -> Unit = {},
    isExpanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit,
    label: String,
    expandedIcon: Painter = painterResource(Res.drawable.i_angle_up),
    collapsedIcon: Painter = painterResource(Res.drawable.i_angle_down),
    dropDownIconDescription: String? = null,
    dropDownIconStyle: IconStyle = IconStyle.S20,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    DropDownTextField(
        modifier = modifier,
        onSelectItem = onSelectItem,
        searchQuery = searchQuery,
        onSearchQueryChange = onSearchQueryChange,
        searchResults = searchResults,
        sourceItemToString = sourceItemToString,
        isEnabled = isEnabled,
        isReadonly = isReadonly,
        isInvalid = isInvalid,
        errorMessage = errorMessage,
        isFocused = isFocused,
        onFocusChange = onFocusChange,
        isExpanded = isExpanded,
        onExpandedChange = onExpandedChange,
        label = { Text(text = label) },
        dropDownIcon = {
            val color =
                colors.calculateLabelColor(
                    customIndicatorColor = dropDownIconStyle.color,
                    isInvalid = isInvalid,
                    isFocused = isFocused,
                )

            ConditionalIcon(
                condition = isExpanded,
                trueIcon = expandedIcon,
                falseIcon = collapsedIcon,
                iconDescription = dropDownIconDescription,
                size = dropDownIconStyle.size.dp,
                color = color,
            )
        },
        shape = shape,
        colors = colors,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownTextField(
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    control: CompositeControl<T>,
    state: DropDownTextFieldState? = null,
    source: SnapshotStateList<T>,
    sourceItemToString: (T) -> String,
    label: @Composable () -> Unit,
    dropDownIcon: @Composable () -> Unit,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    val searchQuery = remember { mutableStateOf("") }
    val results = remember { mutableStateListOf<T>() }
    val initializedState = state ?: remember { DropDownTextFieldState() }

    val controlSnapshot = control.snapshotFlow.collectAsState().value
    val isReadonly = initializedState.readonlyState.valueFlow.collectAsState().value
    val isExpanded = initializedState.expandedState.valueFlow.collectAsState().value
    val isFocused = initializedState.focusedState.valueFlow.collectAsState().value

    ResetReadonlyStateOnResetValue(control = control, state = initializedState)

    LoadSearchResultOnExpand(
        control = control,
        state = initializedState,
        source = source,
        sourceItemToString = sourceItemToString,
        searchQuery = searchQuery.value,
        results = results,
    )

    UpdateSearchQueryOnControlOrExpandChange(
        control = control,
        state = initializedState,
        sourceItemToString = sourceItemToString,
        onSearchQueryChange = { x -> searchQuery.value = x },
    )

    DropDownTextField(
        modifier = modifier,
        onSelectItem = { item ->
            control.setValue(item)
            initializedState.expandedState.collapse()
        },
        searchQuery = searchQuery.value,
        onSearchQueryChange = { query ->
            control.setValue(control.initialValue)
            searchQuery.value = query
            initializedState.expandedState.expand()

            results.update(searchElementsByQuery(source, sourceItemToString, searchQuery.value))
        },
        searchResults = results,
        sourceItemToString = sourceItemToString,
        isEnabled = controlSnapshot.isEnabled,
        isReadonly = isReadonly,
        isInvalid = controlSnapshot.isInvalid,
        errorMessage = controlSnapshot.errorMessage,
        isFocused = isFocused,
        onFocusChange = { x -> initializedState.focusedState.setValue(x.isFocused) },
        isExpanded = isExpanded,
        onExpandedChange = { x -> initializedState.expandedState.setValue(x) },
        label = label,
        dropDownIcon = dropDownIcon,
        shape = shape,
        colors = colors,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownTextField(
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    control: CompositeControl<T>,
    state: DropDownTextFieldState? = null,
    source: SnapshotStateList<T>,
    sourceItemToString: (T) -> String,
    label: String = "DropDownTextField",
    collapsedIcon: Painter = painterResource(Res.drawable.i_angle_up),
    expandedIcon: Painter = painterResource(Res.drawable.i_angle_down),
    dropDownIconDescription: String? = null,
    dropDownIconStyle: IconStyle = IconStyle.S20,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    val initializedState = state ?: remember { DropDownTextFieldState() }

    DropDownTextField(
        modifier = modifier,
        control = control,
        state = initializedState,
        source = source,
        sourceItemToString = sourceItemToString,
        label = { Text(label) },
        dropDownIcon = {
            val isInvalid = control.isInvalidFlow.collectAsState().value
            val isExpanded = initializedState.expandedState.valueFlow.collectAsState().value
            val isFocused = initializedState.focusedState.valueFlow.collectAsState().value

            val color =
                colors.calculateLabelColor(
                    customIndicatorColor = dropDownIconStyle.color,
                    isInvalid = isInvalid,
                    isFocused = isFocused,
                )

            ConditionalIcon(
                condition = isExpanded,
                trueIcon = expandedIcon,
                falseIcon = collapsedIcon,
                iconDescription = dropDownIconDescription,
                size = dropDownIconStyle.size.dp,
                color = color,
            )
        },
        shape = shape,
        colors = colors,
    )
}
