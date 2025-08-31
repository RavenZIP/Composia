package io.github.ravenzip.composia.components.textField.dropdown

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.components.icon.ConditionalIcon
import io.github.ravenzip.composia.components.model.DataSource
import io.github.ravenzip.composia.components.textField.outlined.OutlinedSingleLineTextField
import io.github.ravenzip.composia.components.textField.shared.*
import io.github.ravenzip.composia.control.validatableControl.ValidatableSingleControl
import io.github.ravenzip.composia.state.DropDownTextFieldState
import io.github.ravenzip.composia.style.DefaultComponentShape
import io.github.ravenzip.composia.style.IconStyle
import io.github.ravenzip.composia.style.ProgressIndicatorStyle
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
    dropDownMenuItem: @Composable (String) -> Unit = { text -> Text(text = text) },
    dropDownMenuPlaceholder: @Composable () -> Unit = { Text(text = "Нет результатов") },
    shape: Shape = DefaultComponentShape,
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
                label = label,
                trailingIcon = dropDownIcon,
                shape = shape,
                colors = colors,
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier =
                    Modifier.border(
                        2.dp,
                        color = colors.focusedLabelColor,
                        shape = DefaultComponentShape,
                    ),
                shape = DefaultComponentShape,
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                if (searchResults.isNotEmpty()) {
                    searchResults.forEach { item ->
                        DropdownMenuItem(
                            text = { dropDownMenuItem(sourceItemToString(item)) },
                            onClick = { onSelectItem(item) },
                        )
                    }
                } else {
                    DropdownMenuItem(
                        text = { dropDownMenuPlaceholder() },
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
    dropDownMenuItemStyle: TextStyle = LocalTextStyle.current,
    dropDownMenuPlaceholder: String = "Нет результатов",
    dropDownMenuPlaceholderStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = DefaultComponentShape,
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
                size = dropDownIconStyle.size,
                color = color,
            )
        },
        dropDownMenuItem = { text -> Text(text = text, style = dropDownMenuItemStyle) },
        dropDownMenuPlaceholder = {
            Text(text = dropDownMenuPlaceholder, style = dropDownMenuPlaceholderStyle)
        },
        shape = shape,
        colors = colors,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownTextField(
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    control: ValidatableSingleControl<T>,
    state: DropDownTextFieldState? = null,
    source: DataSource<T>,
    sourceItemToString: (T) -> String,
    label: @Composable () -> Unit,
    dropDownIcon: @Composable () -> Unit,
    dropDownIconWithProgressIndicator:
        (@Composable
        (generatedDropDownIcon: @Composable () -> Unit, isLoading: Boolean) -> Unit)? =
        null,
    dropDownMenuItem: @Composable (String) -> Unit = { text -> Text(text = text) },
    dropDownMenuPlaceholder: (@Composable () -> Unit)? = null,
    shape: Shape = DefaultComponentShape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    val searchQuery = remember { mutableStateOf("") }
    val results = remember { mutableStateListOf<T>() }
    val initializedState = state ?: remember { DropDownTextFieldState() }
    val isLoading = remember { mutableStateOf(false) }

    val searchQueryFlow = remember { snapshotFlow { searchQuery.value } }
    val controlSnapshot = control.snapshotFlow.collectAsState().value
    val isReadonly = initializedState.readonlyState.valueFlow.collectAsState().value
    val isExpanded = initializedState.expandedState.valueFlow.collectAsState().value
    val isFocused = initializedState.focusedState.valueFlow.collectAsState().value

    ResetReadonlyStateOnResetValue(control = control, state = initializedState)

    when (source) {
        is DataSource.Predefined -> {
            LoadSearchResult(
                state = initializedState,
                source = source,
                sourceItemToString = sourceItemToString,
                searchQueryFlow = searchQueryFlow,
                results = results,
            )
        }

        is DataSource.ByQuery -> {
            LoadSearchResult(
                state = initializedState,
                source = source,
                searchQueryFlow = searchQueryFlow,
                changeIsLoadingTo = { x -> isLoading.value = x },
                results = results,
            )
        }
    }

    UpdateSearchQueryOnControlOrExpandChange(
        control = control,
        state = initializedState,
        sourceItemToString = sourceItemToString,
        onSearchQueryChange = { x -> searchQuery.value = x },
    )

    TurnOffProgressIndicatorStateOnSourceChange(
        source = source,
        isLoading = isLoading.value,
        turnOffProgressIndicator = { isLoading.value = false },
    )

    DropDownTextField(
        modifier = modifier,
        onSelectItem = { item ->
            control.setValue(item)
            initializedState.expandedState.collapse()
        },
        searchQuery = searchQuery.value,
        onSearchQueryChange = { query ->
            control.setValue(control.resetValue)
            searchQuery.value = query
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
        dropDownIcon = {
            if (source is DataSource.ByQuery && dropDownIconWithProgressIndicator != null)
                dropDownIconWithProgressIndicator(dropDownIcon, isLoading.value)
            else dropDownIcon()
        },
        dropDownMenuItem = dropDownMenuItem,
        dropDownMenuPlaceholder = {
            if (dropDownMenuPlaceholder == null) {
                val dropDownMenuPlaceholderText =
                    if (isLoading.value) "Загрузка..." else "Нет результатов"

                Text(text = dropDownMenuPlaceholderText)
            } else {
                dropDownMenuPlaceholder()
            }
        },
        shape = shape,
        colors = colors,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownTextField(
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    control: ValidatableSingleControl<T>,
    state: DropDownTextFieldState? = null,
    source: DataSource<T>,
    sourceItemToString: (T) -> String,
    label: String = "DropDownTextField",
    expandedIcon: Painter = painterResource(Res.drawable.i_angle_up),
    collapsedIcon: Painter = painterResource(Res.drawable.i_angle_down),
    dropDownIconDescription: String? = null,
    dropDownIconStyle: IconStyle = IconStyle.S20,
    progressIndicatorStyle: ProgressIndicatorStyle = ProgressIndicatorStyle.Default,
    trailingContainerSpacing: Dp = 10.dp,
    dropDownMenuItemStyle: TextStyle = LocalTextStyle.current,
    dropDownMenuPlaceholder: String? = null,
    dropDownMenuPlaceholderStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = DefaultComponentShape,
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
                size = dropDownIconStyle.size,
                color = color,
            )
        },
        dropDownIconWithProgressIndicator = { generatedDropDownIcon, isLoading ->
            val containerWidth =
                dropDownIconStyle.size +
                    progressIndicatorStyle.size +
                    progressIndicatorStyle.strokeWith * 2 +
                    trailingContainerSpacing * 2

            FlowRow(
                modifier = Modifier.width(containerWidth).padding(end = 14.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(progressIndicatorStyle.size),
                        strokeWidth = progressIndicatorStyle.strokeWith,
                    )

                    Spacer(modifier = Modifier.width(trailingContainerSpacing))
                }

                generatedDropDownIcon()
            }
        },
        dropDownMenuItem = { text -> Text(text = text, style = dropDownMenuItemStyle) },
        dropDownMenuPlaceholder =
            dropDownMenuPlaceholder?.let {
                { Text(text = dropDownMenuPlaceholder, style = dropDownMenuPlaceholderStyle) }
            },
        shape = shape,
        colors = colors,
    )
}
