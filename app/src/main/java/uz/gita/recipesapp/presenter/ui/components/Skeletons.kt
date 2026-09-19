package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.gita.recipesapp.presenter.ui.theme.Radius
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.cardShadow
import uz.gita.recipesapp.presenter.ui.theme.oshxona

@Stable
class Shimmer internal constructor(
    val progress: State<Float>,
    val travel: Float,
)

@Composable
fun rememberShimmer(): Shimmer {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )
    val travel = LocalWindowInfo.current.containerSize.width.toFloat().coerceAtLeast(1f)
    return remember(progress, travel) { Shimmer(progress, travel) }
}

fun Modifier.shimmer(shimmer: Shimmer, base: Color, highlight: Color): Modifier = drawBehind {
    val band = SHIMMER_BAND
    val x = -band + shimmer.progress.value * (shimmer.travel + band * 2)
    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(base, highlight, base),
            start = Offset(x - band, 0f),
            end = Offset(x + band, band * 0.25f)
        )
    )
}

private const val SHIMMER_BAND = 320f

@Composable
fun SkeletonBlock(
    modifier: Modifier = Modifier,
    radius: Dp = Radius.badge,
    soft: Boolean = false,
    shimmer: Shimmer = rememberShimmer(),
) {
    val colors = MaterialTheme.oshxona
    val base = if (soft) colors.skeletonSoft else colors.skeleton
    val highlight = lerp(base, Color.White, if (colors.isDark) 0.07f else 0.55f)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius))
            .shimmer(shimmer, base, highlight)
    )
}

@Composable
fun RecipeListCardSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberShimmer(),
) {
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
            radius = Radius.image,
            shimmer = shimmer
        )
        Spacer(Modifier.size(Spacing.sm))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            SkeletonBlock(modifier = Modifier.fillMaxWidth().height(16.dp), shimmer = shimmer)
            SkeletonBlock(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp), shimmer = shimmer)
            SkeletonBlock(modifier = Modifier.fillMaxWidth(0.3f).height(12.dp), soft = true, shimmer = shimmer)
        }
    }
}

@Composable
fun RecipeGridCardSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberShimmer(),
) {
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
                .height(Sizes.gridImageHeight),
            shimmer = shimmer
        )
        Column(
            modifier = Modifier.padding(Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            SkeletonBlock(modifier = Modifier.fillMaxWidth().height(14.dp), shimmer = shimmer)
            SkeletonBlock(modifier = Modifier.fillMaxWidth(0.5f).height(12.dp), soft = true, shimmer = shimmer)
        }
    }
}

@Composable
fun CategoryCardSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberShimmer(),
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .fillMaxWidth()
            .cardShadow()
            .clip(Shapes.card)
            .background(colors.surface)
            .padding(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        SkeletonBlock(
            modifier = Modifier.size(Sizes.categoryIconBox),
            radius = Radius.image,
            shimmer = shimmer
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            SkeletonBlock(modifier = Modifier.fillMaxWidth(0.8f).height(14.dp), shimmer = shimmer)
            SkeletonBlock(modifier = Modifier.fillMaxWidth(0.4f).height(10.dp), soft = true, shimmer = shimmer)
        }
    }
}

@Composable
fun HomeSkeleton(modifier: Modifier = Modifier) {
    val shimmer = rememberShimmer()
    Column(modifier = modifier) {
        SkeletonBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(Sizes.heroHeight),
            radius = Radius.hero,
            shimmer = shimmer
        )
        Spacer(Modifier.size(Spacing.sm))
        SkeletonBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(Sizes.buttonHeight),
            radius = Radius.button,
            soft = true,
            shimmer = shimmer
        )
        Spacer(Modifier.size(Spacing.xl))
        SkeletonBlock(modifier = Modifier.fillMaxWidth(0.4f).height(20.dp), shimmer = shimmer)
        Spacer(Modifier.size(Spacing.sm))
        repeat(2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                CategoryCardSkeleton(modifier = Modifier.weight(1f), shimmer = shimmer)
                CategoryCardSkeleton(modifier = Modifier.weight(1f), shimmer = shimmer)
            }
        }
        Spacer(Modifier.size(Spacing.lg))
        SkeletonBlock(modifier = Modifier.fillMaxWidth(0.35f).height(20.dp), shimmer = shimmer)
        Spacer(Modifier.size(Spacing.sm))
        repeat(3) {
            RecipeListCardSkeleton(
                modifier = Modifier.padding(bottom = Spacing.sm),
                shimmer = shimmer
            )
        }
    }
}

@Composable
fun RecipeDetailSkeleton(modifier: Modifier = Modifier) {
    val shimmer = rememberShimmer()
    Column(
        modifier = modifier.padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        SkeletonBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(Sizes.heroHeight),
            radius = Radius.hero,
            shimmer = shimmer
        )
        Spacer(Modifier.size(Spacing.xs))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            SkeletonBlock(modifier = Modifier.size(Sizes.iconButton), radius = Radius.pill, shimmer = shimmer)
            SkeletonBlock(modifier = Modifier.fillMaxWidth(0.4f).height(14.dp), shimmer = shimmer)
        }
        SkeletonBlock(modifier = Modifier.fillMaxWidth(0.9f).height(26.dp), shimmer = shimmer)
        SkeletonBlock(modifier = Modifier.fillMaxWidth(0.6f).height(26.dp), shimmer = shimmer)
        Spacer(Modifier.size(Spacing.xs))
        repeat(3) {
            SkeletonBlock(modifier = Modifier.fillMaxWidth().height(12.dp), soft = true, shimmer = shimmer)
        }
        Spacer(Modifier.size(Spacing.sm))
        SkeletonBlock(modifier = Modifier.fillMaxWidth().height(56.dp), radius = Radius.card, shimmer = shimmer)
        Spacer(Modifier.size(Spacing.sm))
        repeat(4) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                SkeletonBlock(modifier = Modifier.size(Sizes.checkbox), radius = Radius.badge, shimmer = shimmer)
                SkeletonBlock(modifier = Modifier.fillMaxWidth(0.7f).height(14.dp), shimmer = shimmer)
            }
        }
    }
}
