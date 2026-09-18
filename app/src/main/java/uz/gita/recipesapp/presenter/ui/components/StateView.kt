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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

@Composable
fun StateView(
    icon: ImageVector,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    tint: androidx.compose.ui.graphics.Color? = null,
    primaryAction: Pair<String, () -> Unit>? = null,
    secondaryAction: Pair<String, () -> Unit>? = null,
) {
    val colors = MaterialTheme.oshxona
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.md, vertical = Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint ?: colors.inkDisabled,
            modifier = Modifier.size(Sizes.emptyIcon)
        )
        Spacer(Modifier.size(Spacing.md))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = colors.ink,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(Spacing.xs))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.inkMuted,
            textAlign = TextAlign.Center
        )
        if (primaryAction != null) {
            Spacer(Modifier.size(Spacing.lg))
            PrimaryButton(text = primaryAction.first, onClick = primaryAction.second)
        }
        if (secondaryAction != null) {
            Spacer(Modifier.size(Spacing.sm))
            SecondaryButton(text = secondaryAction.first, onClick = secondaryAction.second)
        }
    }
}

@Composable
fun ErrorStateView(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    secondaryAction: Pair<String, () -> Unit>? = null,
) {
    StateView(
        icon = Icons.Rounded.ErrorOutline,
        title = stringResource(R.string.state_error_title),
        body = stringResource(R.string.state_error_body),
        tint = MaterialTheme.oshxona.error,
        primaryAction = stringResource(R.string.common_retry) to onRetry,
        secondaryAction = secondaryAction,
        modifier = modifier
    )
}

@Composable
fun OfflineStateView(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    onOpenSaved: (() -> Unit)? = null,
) {
    StateView(
        icon = Icons.Rounded.CloudOff,
        title = stringResource(R.string.state_offline_title),
        body = stringResource(R.string.state_offline_body),
        tint = MaterialTheme.oshxona.accentInk,
        primaryAction = stringResource(R.string.common_retry) to onRetry,
        secondaryAction = onOpenSaved?.let { stringResource(R.string.state_open_cache) to it },
        modifier = modifier
    )
}

@Composable
fun NotFoundStateView(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    primaryAction: Pair<String, () -> Unit>? = null,
    secondaryAction: Pair<String, () -> Unit>? = null,
) {
    StateView(
        icon = Icons.Rounded.SearchOff,
        title = title,
        body = body,
        primaryAction = primaryAction,
        secondaryAction = secondaryAction,
        modifier = modifier
    )
}

@Composable
fun OfflineBanner(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.input)
            .background(colors.accentTint)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.CloudOff,
                contentDescription = null,
                tint = colors.accentInk,
                modifier = Modifier.size(Sizes.iconSm)
            )
            Spacer(Modifier.size(Spacing.xs))
            Text(
                text = stringResource(R.string.offline_banner),
                style = MaterialTheme.typography.labelMedium,
                color = colors.accentInk
            )
        }
        Text(
            text = stringResource(R.string.common_retry_short),
            style = MaterialTheme.typography.labelMedium,
            color = colors.primaryInk,
            modifier = Modifier
                .clip(Shapes.pill)
                .scaleClickable(onClick = onRetry)
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs)
        )
    }
}

enum class PagingFooterState {
    Idle,
    Loading,
    Error,
    End
}

@Composable
fun PagingFooter(
    state: PagingFooterState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.lg),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            PagingFooterState.Idle -> Unit

            PagingFooterState.Loading -> {
                val transition = rememberInfiniteTransition(label = "dots")
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                    repeat(3) { index ->
                        val alpha by transition.animateFloat(
                            initialValue = 0.3f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 600, delayMillis = index * 150),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "dot$index"
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .alpha(alpha)
                                .clip(CircleShape)
                                .background(colors.primary)
                        )
                    }
                }
            }

            PagingFooterState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.paging_error),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.error
                    )
                    Spacer(Modifier.size(Spacing.xs))
                    SecondaryButton(
                        text = stringResource(R.string.common_retry),
                        onClick = onRetry,
                        modifier = Modifier.padding(horizontal = Spacing.xl)
                    )
                }
            }

            PagingFooterState.End -> Text(
                text = stringResource(R.string.paging_end),
                style = MaterialTheme.typography.labelSmall,
                color = colors.inkFaint
            )
        }
    }
}
