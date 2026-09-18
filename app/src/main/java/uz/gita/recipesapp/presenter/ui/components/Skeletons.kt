package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.gita.recipesapp.presenter.ui.theme.Radius
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.cardShadow
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun SkeletonBlock(
    modifier: Modifier = Modifier,
    radius: Dp = Radius.badge,
    soft: Boolean = false,
) {
    val colors = MaterialTheme.oshxona
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius))
            .alpha(alpha)
            .background(if (soft) colors.skeletonSoft else colors.skeleton)
    )
}

@Composable
fun RecipeListCardSkeleton(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .fillMaxWidth()
            .cardShadow()
            .clip(Shapes.card)
            .background(colors.surface)
            .padding(Spacing.md)
    ) {
        SkeletonBlock(
            modifier = Modifier.size(Sizes.listThumb),
            radius = Radius.image
        )
        Spacer(Modifier.size(Spacing.sm))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            SkeletonBlock(modifier = Modifier.fillMaxWidth().height(16.dp))
            SkeletonBlock(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp))
            SkeletonBlock(modifier = Modifier.fillMaxWidth(0.3f).height(12.dp), soft = true)
        }
    }
}

@Composable
fun RecipeGridCardSkeleton(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.oshxona
    Column(
        modifier = modifier
            .fillMaxWidth()
            .cardShadow()
            .clip(Shapes.card)
            .background(colors.surface)
    ) {
        SkeletonBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(Sizes.gridImageHeight)
        )
        Column(
            modifier = Modifier.padding(Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            SkeletonBlock(modifier = Modifier.fillMaxWidth().height(14.dp))
            SkeletonBlock(modifier = Modifier.fillMaxWidth(0.5f).height(12.dp), soft = true)
        }
    }
}
