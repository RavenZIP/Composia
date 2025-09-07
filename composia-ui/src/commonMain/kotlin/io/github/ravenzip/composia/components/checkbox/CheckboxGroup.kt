package io.github.ravenzip.composia.components.checkbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.control.validatable.MutableValidatableControl
import io.github.ravenzip.composia.control.validatable.toggle
import io.github.ravenzip.composia.extension.S18
import io.github.ravenzip.composia.extension.collectAsSnapshotStateList

@Composable
fun <T, K> CheckboxGroup(
    selectedItems: SnapshotStateList<T>,
    source: List<T>,
    onClick: (T) -> Unit,
    keySelector: (T) -> K,
    sourceItemToString: (T) -> String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    textStyle: TextStyle = TextStyle.S18,
    contentPadding: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(10.dp),
    colors: CheckboxColors = CheckboxDefaults.colors(),
) {
    Column(modifier = modifier, verticalArrangement = contentPadding) {
        source.forEach { item ->
            val isSelected = selectedItems.any { v -> keySelector(v) == keySelector(item) }

            Checkbox(
                isSelected = isSelected,
                onClick = { onClick(item) },
                text = sourceItemToString(item),
                textStyle = textStyle,
                enabled = isEnabled,
                colors = colors,
            )
        }
    }
}

@Composable
fun <T, K> CheckboxGroup(
    control: MutableValidatableControl<List<T>>,
    source: List<T>,
    sourceItemToString: (T) -> String,
    keySelector: (T) -> K,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.S18,
    contentPadding: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(10.dp),
    colors: CheckboxColors = CheckboxDefaults.colors(),
) {
    val value = control.valueFlow.collectAsSnapshotStateList()
    val isEnabled = control.isEnabledFlow.collectAsState().value

    CheckboxGroup(
        selectedItems = value,
        source = source,
        onClick = { x -> control.toggle(x, keySelector) },
        keySelector = keySelector,
        sourceItemToString = sourceItemToString,
        modifier = modifier,
        isEnabled = isEnabled,
        textStyle = textStyle,
        contentPadding = contentPadding,
        colors = colors,
    )
}
