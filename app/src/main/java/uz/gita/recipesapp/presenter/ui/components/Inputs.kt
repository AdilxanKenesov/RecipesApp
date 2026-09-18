package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt
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
    position: Float? = null,
) {
    val colors = MaterialTheme.oshxona
    val animated by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = tween(220),
        label = "segmentIndicator"
    )
    val current = position ?: animated
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.chipHeight + Spacing.xs)
            .clip(Shapes.pill)
            .background(colors.surfaceAlt)
            .padding(Spacing.xxs)
    ) {
        val segmentWidth = maxWidth / options.size
        Box(
            modifier = Modifier
                .offset { IntOffset((segmentWidth.toPx() * current).roundToInt(), 0) }
                .width(segmentWidth)
                .fillMaxHeight()
                .cardShadow(radius = Radius.pill, elevation = 3.dp)
                .clip(Shapes.pill)
                .background(colors.surfaceRaised)
                .border(BorderStroke(1.dp, colors.hairline), Shapes.pill)
        )
        Row(modifier = Modifier.fillMaxSize()) {
            options.forEachIndexed { index, option ->
                val closeness = (1f - abs(current - index)).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(Shapes.pill)
                        .scaleClickable(role = Role.Tab) { onSelect(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (closeness > 0.5f) FontWeight.Bold else FontWeight.SemiBold
                        ),
                        color = lerp(colors.inkMuted, colors.ink, closeness)
                    )
                }
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
