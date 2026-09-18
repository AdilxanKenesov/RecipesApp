package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.theme.Overlay
import uz.gita.recipesapp.presenter.ui.theme.BadgeLabel
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

@Composable
fun MetaChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .clip(Shapes.pill)
            .background(colors.surface)
            .border(BorderStroke(1.dp, colors.hairline), Shapes.pill)
            .padding(horizontal = Spacing.sm, vertical = Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = BadgeLabel,
            color = colors.inkMuted
        )
    }
}

@Composable
fun CategoryChip(
    emoji: String,
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .height(Sizes.chipHeight)
            .clip(Shapes.pill)
            .background(if (selected) colors.primaryTint else colors.surfaceAlt)
            .then(
                if (selected) Modifier.border(BorderStroke(1.5.dp, colors.primary), Shapes.pill)
                else Modifier
            )
            .scaleClickable(onClick = onClick)
            .padding(horizontal = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
    ) {
        Text(text = emoji, style = MaterialTheme.typography.labelMedium)
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) colors.primaryInk else colors.ink
        )
    }
}

@Composable
fun IngredientChip(
    name: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .height(Sizes.chipHeight)
            .clip(Shapes.pill)
            .background(colors.primaryTint)
            .border(BorderStroke(1.dp, colors.primary), Shapes.pill)
            .padding(start = Spacing.sm, end = Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            color = colors.primaryInk
        )
        OshxonaIconButton(
            icon = Icons.Rounded.Close,
            contentDescription = stringResource(R.string.cd_remove),
            onClick = onRemove,
            modifier = Modifier.size(Sizes.chipHeight),
            tint = colors.primaryInk
        )
    }
}

@Composable
fun VideoBadge(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(Shapes.pill)
            .background(Overlay.copy(alpha = 0.76f))
            .padding(horizontal = Spacing.xs, vertical = Spacing.xxs)
            .defaultMinSize(minHeight = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.PlayArrow,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = stringResource(R.string.badge_video),
            style = BadgeLabel,
            color = Color.White
        )
    }
}
