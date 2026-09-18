package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.theme.Radius
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.cardShadow
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit = {},
) {
    val colors = MaterialTheme.oshxona
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.buttonHeight)
            .clip(Shapes.input)
            .background(colors.surfaceAlt)
            .border(
                border = BorderStroke(if (focused) 1.5.dp else 1.dp, if (focused) colors.primary else colors.hairline),
                shape = Shapes.input
            )
            .padding(horizontal = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            tint = colors.inkFaint,
            modifier = Modifier.size(Sizes.iconSm)
        )
        Spacer(Modifier.size(Spacing.xs))
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.inkFaint
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.ink),
                cursorBrush = SolidColor(colors.primary),
                interactionSource = interactionSource,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (value.isNotEmpty()) {
            OshxonaIconButton(
                icon = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.cd_clear),
                onClick = { onValueChange("") },
                tint = colors.inkMuted
            )
        }
    }
}

@Composable
fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.chipHeight + Spacing.xs)
            .clip(Shapes.pill)
            .background(colors.surfaceAlt)
            .padding(Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(Sizes.chipHeight)
                    .then(if (selected) Modifier.cardShadow(radius = Radius.pill, elevation = 4.dp) else Modifier)
                    .clip(Shapes.pill)
                    .background(if (selected) colors.surface else colors.surfaceAlt)
                    .scaleClickable { onSelect(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) colors.ink else colors.inkMuted
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientChipInput(
    items: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
    onRemove: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = Sizes.buttonHeight)
            .clip(Shapes.input)
            .background(colors.surfaceAlt)
            .border(
                border = BorderStroke(if (focused) 1.5.dp else 1.dp, if (focused) colors.primary else colors.hairline),
                shape = Shapes.input
            )
            .padding(Spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
        verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
    ) {
        items.forEach { item ->
            IngredientChip(name = item, onRemove = { onRemove(item) })
        }
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = 120.dp, minHeight = Sizes.chipHeight),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.inkFaint
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.ink),
                cursorBrush = SolidColor(colors.primary),
                interactionSource = interactionSource,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onAdd() }),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
