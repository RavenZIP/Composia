package io.github.ravenzip.composia.components.switch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.ravenzip.composia.control.multiValueControl.MultiValueControl
import io.github.ravenzip.composia.extension.S18
import io.github.ravenzip.composia.function.collectAsSnapshotListState

@Composable
fun <T, K> SwitchGroup(
    selectedItems: SnapshotStateList<T>,
    source: List<T>,
    onClick: (T) -> Unit,
    keySelector: (T) -> K,
    sourceItemToString: (T) -> String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    textStyle: TextStyle = TextStyle.S18,
    contentPadding: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(10.dp),
    colors: SwitchColors = SwitchDefaults.colors(),
) {
    Column(modifier = modifier, verticalArrangement = contentPadding) {
        source.forEach { item ->
            val isSelected = selectedItems.any { v -> keySelector(v) == keySelector(item) }

            Switch(
                isSelected = isSelected,
                isEnabled = isEnabled,
                onClick = { onClick(item) },
                text = sourceItemToString(item),
                textStyle = textStyle,
                colors = colors,
            )
        }
    }
}

@Composable
fun <T, K> SwitchGroup(
    control: MultiValueControl<T, K>,
    source: List<T>,
    sourceItemToString: (T) -> String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    contentPadding: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(10.dp),
    colors: SwitchColors = SwitchDefaults.colors(),
) {
    val value = control.valueFlow.collectAsSnapshotListState()
    val isEnabled = control.isEnabledFlow.collectAsState().value

    SwitchGroup(
        selectedItems = value,
        source = source,
        sourceItemToString = sourceItemToString,
        onClick = { x -> control.toggle(x) },
        keySelector = { x -> control.keySelector(x) },
        modifier = modifier,
        isEnabled = isEnabled,
        textStyle = textStyle,
        contentPadding = contentPadding,
        colors = colors,
    )
}
