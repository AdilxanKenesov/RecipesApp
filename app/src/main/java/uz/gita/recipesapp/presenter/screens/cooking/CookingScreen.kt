package uz.gita.recipesapp.presenter.screens.cooking

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.module.StepUiData
import uz.gita.recipesapp.presenter.ui.components.CountdownRing
import uz.gita.recipesapp.presenter.ui.components.OshxonaIconButton
import uz.gita.recipesapp.presenter.ui.components.PrimaryButton
import uz.gita.recipesapp.presenter.ui.components.RecipeImage
import uz.gita.recipesapp.presenter.ui.components.SecondaryButton
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import uz.gita.recipesapp.presenter.ui.preview.ThemePreview
import uz.gita.recipesapp.presenter.ui.theme.Overline
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.cardShadow
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.cleanRecipeTitle
import uz.gita.recipesapp.presenter.ui.util.scaleClickable
import java.util.Locale

class CookingScreen(
    private val recipeId: Int = 1
) : Screen {

    override val key: ScreenKey = "cooking_$recipeId"

    @Composable
    override fun Content() {
        val viewModel: CookingContract.CookingViewModel = getViewModel<CookingViewModel>()
        val state by viewModel.collectAsState()
        val pagerState = rememberPagerState { state.steps.size }
        val haptic = LocalHapticFeedback.current
        val view = LocalView.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                is CookingContract.SideEffect.ScrollTo -> pagerState.animateScrollToPage(effect.index)
                CookingContract.SideEffect.TimerFinished -> haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }

        DisposableEffect(view) {
            view.keepScreenOn = true
            onDispose { view.keepScreenOn = false }
        }

        LaunchedEffect(recipeId) {
            viewModel.onEventDispatcher(CookingContract.CookingEvent.Load(recipeId))
        }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                viewModel.onEventDispatcher(CookingContract.CookingEvent.PageChanged(page))
            }
        }

        BackHandler {
            val event = when {
                state.isFinished -> CookingContract.CookingEvent.Close
                state.currentIndex > 0 -> CookingContract.CookingEvent.Previous
                else -> CookingContract.CookingEvent.Close
            }
            viewModel.onEventDispatcher(event)
        }

        CookingContent(state, pagerState, viewModel::onEventDispatcher)
    }

    @Composable
    private fun CookingContent(
        state: CookingContract.CookingUiState,
        pagerState: PagerState,
        onEventDispatcher: (CookingContract.CookingEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        if (state.isFinished) {
            FinishView(state, onEventDispatcher)
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.ground)
                .statusBarsPadding()
        ) {
            TopBar(state, onEventDispatcher)
            StepProgress(state, onEventDispatcher)

            val activeTimer = state.timer
            AnimatedVisibility(
                visible = activeTimer != null && activeTimer.stepIndex != state.currentIndex && !activeTimer.isFinished,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                activeTimer?.let { ActiveTimerChip(it, state, onEventDispatcher) }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                state.steps.getOrNull(page)?.let { step ->
                    StepPage(step, state.timerFor(page), onEventDispatcher)
                }
            }

            BottomBar(state, onEventDispatcher)
        }
    }

    @Composable
    private fun TopBar(
        state: CookingContract.CookingUiState,
        onEventDispatcher: (CookingContract.CookingEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Spacing.topBarTop)
                .padding(horizontal = Spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OshxonaIconButton(
                icon = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.cd_close),
                onClick = { onEventDispatcher(CookingContract.CookingEvent.Close) }
            )
            Text(
                text = state.title,
                style = MaterialTheme.typography.titleSmall,
                color = colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Spacing.xs)
            )
            Text(
                text = stringResource(R.string.cooking_progress, state.currentIndex + 1, state.steps.size),
                style = MaterialTheme.typography.labelLarge.copy(fontFeatureSettings = "tnum"),
                color = colors.inkMuted,
                modifier = Modifier.padding(end = Spacing.sm)
            )
        }
    }

    @Composable
    private fun StepProgress(
        state: CookingContract.CookingUiState,
        onEventDispatcher: (CookingContract.CookingEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
        ) {
            state.steps.indices.forEach { index ->
                val status = state.statuses[index]
                val timer = state.timerFor(index)
                val fill = when {
                    status != null -> 1f
                    index == state.currentIndex && timer != null -> 1f - timer.progress
                    index == state.currentIndex -> 1f
                    else -> 0f
                }
                val fillColor = when {
                    status == CookingContract.StepStatus.SKIPPED -> colors.primary.copy(alpha = 0.35f)
                    index == state.currentIndex && status == null && timer == null -> colors.primary.copy(alpha = 0.6f)
                    else -> colors.primary
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(Sizes.touchTarget)
                        .scaleClickable(role = Role.Tab) {
                            onEventDispatcher(CookingContract.CookingEvent.GoToStep(index))
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(Shapes.pill)
                            .background(colors.hairline)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fill.coerceIn(0f, 1f))
                                .clip(Shapes.pill)
                                .background(fillColor)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ActiveTimerChip(
        timer: CookingContract.StepTimer,
        state: CookingContract.CookingUiState,
        onEventDispatcher: (CookingContract.CookingEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        val stepNumber = state.steps.getOrNull(timer.stepIndex)?.number ?: (timer.stepIndex + 1)
        Row(
            modifier = Modifier
                .padding(top = Spacing.xxs)
                .height(Sizes.touchTarget)
                .clip(Shapes.pill)
                .background(colors.primaryTint)
                .scaleClickable(onClickLabel = stringResource(R.string.cd_active_timer)) {
                    onEventDispatcher(CookingContract.CookingEvent.GoToStep(timer.stepIndex))
                }
                .padding(horizontal = Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            Icon(
                imageVector = Icons.Rounded.Timer,
                contentDescription = null,
                tint = colors.primaryInk,
                modifier = Modifier.size(Sizes.iconSm)
            )
            Text(
                text = "${formatTime(timer.remainingSeconds)} · ${stringResource(R.string.cooking_step_label, stepNumber)}",
                style = MaterialTheme.typography.labelLarge.copy(fontFeatureSettings = "tnum"),
                color = colors.primaryInk
            )
        }
    }

    @Composable
    private fun StepPage(
        step: StepUiData,
        timer: CookingContract.StepTimer?,
        onEventDispatcher: (CookingContract.CookingEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.md)
                .padding(top = Spacing.md, bottom = Spacing.lg)
        ) {
            step.images.firstOrNull()?.let { image ->
                RecipeImage(
                    url = image,
                    toneSeed = step.number,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .clip(Shapes.hero)
                )
                Spacer(Modifier.size(Spacing.lg))
            }
            Text(
                text = stringResource(R.string.cooking_step_label, step.number).uppercase(),
                style = Overline,
                color = colors.inkFaint
            )
            Spacer(Modifier.size(Spacing.xs))
            Text(
                text = step.text,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp, lineHeight = 30.sp),
                color = colors.ink
            )
            step.timerMinutes?.let { minutes ->
                Spacer(Modifier.size(Spacing.xl))
                TimerCard(minutes, timer, onEventDispatcher)
            }
        }
    }

    @Composable
    private fun TimerCard(
        minutes: Int,
        timer: CookingContract.StepTimer?,
        onEventDispatcher: (CookingContract.CookingEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        val remaining = timer?.remainingSeconds ?: minutes * 60
        val isRunning = timer?.isRunning == true
        val isStarted = timer?.isStarted == true
        val isFinished = timer?.isFinished == true
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .cardShadow()
                .clip(Shapes.card)
                .background(colors.surface)
                .padding(Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.detail_timer, minutes),
                style = MaterialTheme.typography.labelMedium,
                color = colors.inkMuted
            )
            Spacer(Modifier.size(Spacing.md))
            CountdownRing(progress = timer?.progress ?: 1f, size = 180.dp) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formatTime(remaining),
                        style = MaterialTheme.typography.displaySmall.copy(fontFeatureSettings = "tnum"),
                        color = colors.ink
                    )
                    if (isFinished) {
                        Text(
                            text = stringResource(R.string.timer_done),
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.primaryInk
                        )
                    }
                }
            }
            Spacer(Modifier.size(Spacing.md))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                if (isFinished) {
                    SecondaryButton(
                        text = stringResource(R.string.timer_reset),
                        onClick = { onEventDispatcher(CookingContract.CookingEvent.ResetTimer) },
                        leadingIcon = Icons.Rounded.Replay
                    )
                } else {
                    PrimaryButton(
                        text = stringResource(
                            when {
                                isRunning -> R.string.timer_pause
                                isStarted -> R.string.timer_resume
                                else -> R.string.timer_start
                            }
                        ),
                        onClick = { onEventDispatcher(CookingContract.CookingEvent.ToggleTimer) },
                        leadingIcon = if (isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        shape = Shapes.pill,
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        text = stringResource(R.string.timer_reset),
                        onClick = { onEventDispatcher(CookingContract.CookingEvent.ResetTimer) },
                        enabled = isStarted,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    @Composable
    private fun BottomBar(
        state: CookingContract.CookingUiState,
        onEventDispatcher: (CookingContract.CookingEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = Spacing.md, vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.cooking_skip),
                style = MaterialTheme.typography.labelLarge,
                color = colors.inkMuted,
                modifier = Modifier
                    .clip(Shapes.pill)
                    .scaleClickable { onEventDispatcher(CookingContract.CookingEvent.Skip) }
                    .padding(horizontal = Spacing.sm, vertical = Spacing.md)
            )
            key(state.isLastStep) {
                PrimaryButton(
                    text = stringResource(if (state.isLastStep) R.string.cooking_finish else R.string.cooking_next),
                    onClick = { onEventDispatcher(CookingContract.CookingEvent.Next) },
                    trailingIcon = if (state.isLastStep) Icons.Rounded.Check else Icons.AutoMirrored.Rounded.ArrowForward,
                    shape = Shapes.pill,
                    fillWidth = false,
                    height = Sizes.buttonHeight,
                    elevated = false
                )
            }
        }
    }

    @Composable
    private fun FinishView(
        state: CookingContract.CookingUiState,
        onEventDispatcher: (CookingContract.CookingEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        val scale = remember { Animatable(0.6f) }
        LaunchedEffect(Unit) {
            scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.ground)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.onPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(Modifier.size(Spacing.lg))
            Text(
                text = stringResource(R.string.cooking_done_title),
                style = MaterialTheme.typography.displaySmall,
                color = colors.ink,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(Spacing.xs))
            Text(
                text = stringResource(R.string.cooking_done_body, state.steps.size, state.doneCount),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.inkMuted,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(Spacing.xl))
            PrimaryButton(
                text = stringResource(R.string.cooking_back_to_recipe),
                onClick = { onEventDispatcher(CookingContract.CookingEvent.Close) },
                shape = Shapes.pill
            )
        }
    }

    private fun formatTime(seconds: Int): String =
        String.format(Locale.ROOT, "%02d:%02d", seconds / 60, seconds % 60)

    @ThemePreview
    @Composable
    private fun CookingPreview() {
        OshxonaTheme {
            val recipe = SampleData.recipeDetail
            CookingContent(
                state = CookingContract.CookingUiState(
                    title = recipe.title.cleanRecipeTitle(),
                    steps = recipe.steps,
                    currentIndex = 0,
                    statuses = emptyMap()
                ),
                pagerState = rememberPagerState { recipe.steps.size },
                onEventDispatcher = { }
            )
        }
    }
}
