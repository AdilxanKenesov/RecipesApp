package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import uz.gita.recipesapp.domain.module.IngredientUiData
import uz.gita.recipesapp.domain.module.StepUiData
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

@Composable
fun IngredientGroupHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = colors.primaryInk,
        modifier = modifier.padding(top = Spacing.sm, bottom = Spacing.xxs)
    )
}

@Composable
fun IngredientRow(
    item: IngredientUiData.Item,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    strikeWhenChecked: Boolean = true,
) {
    val colors = MaterialTheme.oshxona
    val struck = item.isChecked && strikeWhenChecked
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = Sizes.buttonHeight)
            .scaleClickable(role = null) { onCheckedChange(!item.isChecked) }
            .padding(vertical = Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = colors.primary,
                uncheckedColor = colors.hairline,
                checkmarkColor = colors.onPrimary
            )
        )
        Spacer(Modifier.size(Spacing.xxs))
        Text(
            text = item.amount,
            style = MaterialTheme.typography.labelMedium,
            color = if (struck) colors.inkDisabled else colors.ink,
            textDecoration = if (struck) TextDecoration.LineThrough else null
        )
        Spacer(Modifier.size(Spacing.xs))
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (struck) colors.inkDisabled else colors.inkMuted,
            textDecoration = if (struck) TextDecoration.LineThrough else null
        )
    }
}

@Composable
fun StepItem(
    step: StepUiData,
    timerLabel: String?,
    onTimerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(Sizes.stepBadge)
                .clip(CircleShape)
                .background(colors.accentTint),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step.number.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = colors.accentInk
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = step.text,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.ink
            )

            if (timerLabel != null) {
                Spacer(Modifier.size(Spacing.xs))
                Row(
                    modifier = Modifier
                        .defaultMinSize(minHeight = Sizes.touchTarget)
                        .clip(Shapes.pill)
                        .background(colors.accentTint)
                        .scaleClickable(onClick = onTimerClick)
                        .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Rounded.Timer,
                        contentDescription = null,
                        tint = colors.accentInk,
                        modifier = Modifier.size(Sizes.iconSm)
                    )
                    Text(
                        text = timerLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.accentInk
                    )
                }
            }

            if (step.images.isNotEmpty()) {
                Spacer(Modifier.size(Spacing.sm))
                if (step.images.size == 1) {
                    RecipeImage(
                        url = step.images.first(),
                        toneSeed = step.number,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Sizes.heroHeight)
                            .clip(Shapes.image)
                    )
                } else {
                    val pagerState = rememberPagerState { step.images.size }
                    Column {
                        HorizontalPager(
                            state = pagerState,
                            pageSpacing = Spacing.xs,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Sizes.heroHeight)
                        ) { page ->
                            RecipeImage(
                                url = step.images[page],
                                toneSeed = step.number + page,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(Shapes.image)
                            )
                        }
                        Spacer(Modifier.size(Spacing.xs))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(step.images.size) { index ->
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 3.dp)
                                        .size(if (index == pagerState.currentPage) 8.dp else 6.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (index == pagerState.currentPage) colors.primary
                                            else colors.hairline
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
