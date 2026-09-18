package uz.gita.recipesapp.presenter.screens.intro

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.components.PrimaryButton
import uz.gita.recipesapp.presenter.ui.preview.ThemePreview
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.heroShadow
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

private data class IntroPage(
    @DrawableRes val image: Int,
    @StringRes val titleRes: Int,
    @StringRes val bodyRes: Int
)

private val introPages = listOf(
    IntroPage(R.drawable.intro1_img, R.string.intro_first_title, R.string.intro_first_body),
    IntroPage(R.drawable.intro2_img, R.string.intro_second_title, R.string.intro_second_body)
)

class IntroScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: IntroContract.IntroViewModel = getViewModel<IntroViewModel>()
        val state by viewModel.collectAsState()
        val pagerState = rememberPagerState { introPages.size }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                is IntroContract.SideEffect.ScrollTo -> pagerState.animateScrollToPage(effect.page)
            }
        }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                viewModel.onEventDispatcher(IntroContract.IntroEvent.PageChanged(page))
            }
        }

        BackHandler(enabled = state.page > 0) {
            viewModel.onEventDispatcher(IntroContract.IntroEvent.Previous)
        }

        IntroContent(state, pagerState, viewModel::onEventDispatcher)
    }

    @Composable
    private fun IntroContent(
        state: IntroContract.IntroUiState,
        pagerState: androidx.compose.foundation.pager.PagerState,
        onEventDispatcher: (IntroContract.IntroEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.ground)
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.topBarTop)
                    .padding(horizontal = Spacing.md),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.intro_skip),
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.inkMuted,
                    modifier = Modifier
                        .clip(Shapes.pill)
                        .scaleClickable { onEventDispatcher(IntroContract.IntroEvent.Skip) }
                        .padding(horizontal = Spacing.sm, vertical = Spacing.sm)
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                val item = introPages[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.md),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(item.image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 3f)
                            .heroShadow()
                            .clip(Shapes.hero)
                    )
                    Spacer(Modifier.size(Spacing.xl))
                    Text(
                        text = stringResource(item.titleRes),
                        style = MaterialTheme.typography.displaySmall,
                        color = colors.ink,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.size(Spacing.sm))
                    Text(
                        text = stringResource(item.bodyRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.inkMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.md),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(state.pageCount) { index ->
                    val selected = index == state.page
                    Box(
                        modifier = Modifier
                            .padding(horizontal = Spacing.xxs)
                            .height(8.dp)
                            .width(if (selected) 28.dp else 8.dp)
                            .clip(Shapes.pill)
                            .background(if (selected) colors.primary else colors.hairline)
                    )
                }
            }

            PrimaryButton(
                text = stringResource(
                    if (state.isLastPage) R.string.intro_choose_language else R.string.intro_next
                ),
                onClick = { onEventDispatcher(IntroContract.IntroEvent.Next) },
                shape = Shapes.pill,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = Spacing.md)
                    .padding(bottom = Spacing.lg)
            )
        }
    }

    @ThemePreview
    @Composable
    private fun IntroPreview() {
        OshxonaTheme {
            IntroContent(
                state = IntroContract.IntroUiState(page = 0),
                pagerState = rememberPagerState { introPages.size },
                onEventDispatcher = { }
            )
        }
    }
}
