package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.presenter.ui.theme.Overlay
import uz.gita.recipesapp.presenter.ui.theme.Overline
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.cardShadow
import uz.gita.recipesapp.presenter.ui.theme.heroShadow
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.cleanRecipeTitle
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

@Composable
fun BookmarkButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    background: Color? = null,
) {
    OshxonaIconButton(
        icon = if (isFavorite) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
        contentDescription = stringResource(
            if (isFavorite) R.string.cd_bookmark_remove else R.string.cd_bookmark_add
        ),
        onClick = onClick,
        modifier = modifier,
        tint = tint ?: if (isFavorite) MaterialTheme.oshxona.primary else MaterialTheme.oshxona.inkMuted,
        background = background
    )
}

@Composable
fun RecipeListCard(
    recipe: RecipeUiData,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .fillMaxWidth()
            .cardShadow()
            .clip(Shapes.card)
            .background(colors.surface)
            .scaleClickable(onClick = onClick)
            .padding(Spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            RecipeImage(
                url = recipe.imageUrl,
                toneSeed = recipe.id,
                modifier = Modifier
                    .size(Sizes.listThumb)
                    .clip(Shapes.image)
            )
            if (recipe.hasVideo) {
                VideoBadge(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(Spacing.xxs)
                )
            }
        }

        Spacer(Modifier.size(Spacing.sm))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = recipe.title.cleanRecipeTitle(),
                style = MaterialTheme.typography.titleSmall,
                color = colors.ink,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.size(Spacing.xxs))
            Text(
                text = recipe.categoryName,
                style = MaterialTheme.typography.labelSmall,
                color = colors.inkFaint
            )
        }

        Spacer(Modifier.size(Spacing.xs))

        BookmarkButton(
            isFavorite = recipe.isFavorite,
            onClick = onBookmarkClick
        )
    }
}

@Composable
fun RecipeGridCard(
    recipe: RecipeUiData,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Column(
        modifier = modifier
            .fillMaxWidth()
            .cardShadow()
            .clip(Shapes.card)
            .background(colors.surface)
            .scaleClickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            RecipeImage(
                url = recipe.imageUrl,
                toneSeed = recipe.id,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Sizes.gridImageHeight)
            )
            if (recipe.hasVideo) {
                VideoBadge(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(Spacing.xs)
                )
            }
            BookmarkButton(
                isFavorite = recipe.isFavorite,
                onClick = onBookmarkClick,
                modifier = Modifier.align(Alignment.TopEnd),
                tint = if (recipe.isFavorite) colors.accent else Color.White,
                background = Overlay.copy(alpha = 0.4f)
            )
        }
        Column(modifier = Modifier.padding(Spacing.sm)) {
            Text(
                text = recipe.title.cleanRecipeTitle(),
                style = MaterialTheme.typography.titleSmall,
                color = colors.ink,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.size(Spacing.xxs))
            Text(
                text = recipe.categoryName,
                style = MaterialTheme.typography.labelSmall,
                color = colors.inkFaint
            )
        }
    }
}

@Composable
fun HeroCard(
    recipe: RecipeUiData,
    overline: String,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.heroHeight)
            .heroShadow()
            .clip(Shapes.hero)
            .scaleClickable(onClick = onClick)
    ) {
        RecipeImage(
            url = recipe.imageUrl,
            toneSeed = recipe.id,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Overlay.copy(alpha = 0.2f), Overlay.copy(alpha = 0.88f)),
                        startY = 0f
                    )
                )
        )

        if (recipe.hasVideo) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Overlay.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(Sizes.icon)
                )
            }
        }

        BookmarkButton(
            isFavorite = recipe.isFavorite,
            onClick = onBookmarkClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(Spacing.xs),
            tint = if (recipe.isFavorite) colors.accent else Color.White,
            background = Overlay.copy(alpha = 0.4f)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Spacing.md)
        ) {
            Text(
                text = overline.uppercase(),
                style = Overline,
                color = colors.accent
            )
            Spacer(Modifier.size(Spacing.xxs))
            Text(
                text = recipe.title.cleanRecipeTitle(),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CategoryCard(
    category: CategoryUiData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    countLabel: String? = null,
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .fillMaxWidth()
            .cardShadow()
            .clip(Shapes.card)
            .background(colors.surface)
            .scaleClickable(enabled = enabled, onClick = onClick)
            .padding(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(Sizes.categoryIconBox)
                .clip(Shapes.image)
                .background(if (enabled) colors.primaryTint else colors.surfaceAlt),
            contentAlignment = Alignment.Center
        ) {
            Text(text = category.emoji, style = MaterialTheme.typography.titleLarge)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleSmall,
                color = if (enabled) colors.ink else colors.inkDisabled,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (countLabel != null) {
                Text(
                    text = countLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (enabled) colors.inkFaint else colors.inkDisabled
                )
            }
        }
    }
}
