package uz.gita.recipesapp.presenter.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.components.CategoryCard
import uz.gita.recipesapp.presenter.ui.components.ErrorStateView
import uz.gita.recipesapp.presenter.ui.components.HeroCard
import uz.gita.recipesapp.presenter.ui.components.HomeSkeleton
import uz.gita.recipesapp.presenter.ui.components.OshxonaIconButton
import uz.gita.recipesapp.presenter.ui.components.OshxonaScaffold
import uz.gita.recipesapp.presenter.ui.components.RecipeListCard
import uz.gita.recipesapp.presenter.ui.components.ScreenTopBar
import uz.gita.recipesapp.presenter.ui.components.SecondaryButton
import uz.gita.recipesapp.presenter.ui.components.SectionHeader
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.CONTENT_CATEGORY
import uz.gita.recipesapp.presenter.ui.util.CONTENT_RECIPE
import uz.gita.recipesapp.presenter.ui.util.RetryWhenOnline
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: HomeContract.HomeViewModel = getViewModel<HomeViewModel>()
        val state by viewModel.collectAsState()

        RetryWhenOnline(hasError = state.hasError) {
            viewModel.onEventDispatcher(HomeContract.HomeEvent.Retry)
        }

        HomeContent(state, viewModel::onEventDispatcher)
    }

    @Composable
    private fun HomeContent(
        state: HomeContract.HomeUiState,
        onEventDispatcher: (HomeContract.HomeEvent) -> Unit
    ) {
        val categoryRows = remember(state.categories) {
            state.categories.filter { it.count > 0 }.take(4).chunked(2)
        }

        OshxonaScaffold(
            topBar = {
                ScreenTopBar(
                    title = stringResource(R.string.app_name),
                    actions = {
                        OshxonaIconButton(
                            icon = Icons.Rounded.Settings,
                            contentDescription = stringResource(R.string.cd_settings),
                            onClick = { onEventDispatcher(HomeContract.HomeEvent.OpenSettings) }
                        )
                    }
                )
            }
        ) {
            when {
                state.hasError -> ErrorStateView(
                    onRetry = { onEventDispatcher(HomeContract.HomeEvent.Retry) }
                )

                state.isLoading -> HomeSkeleton(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState(), enabled = false)
                        .padding(horizontal = Spacing.md)
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Spacing.md,
                        end = Spacing.md,
                        bottom = Spacing.xxl
                    )
                ) {
                    item {
                        state.hero?.let { hero ->
                            HeroCard(
                                recipe = hero,
                                overline = stringResource(R.string.home_hero_overline),
                                onClick = { onEventDispatcher(HomeContract.HomeEvent.OpenRecipe(hero.id)) },
                                onBookmarkClick = {
                                    onEventDispatcher(HomeContract.HomeEvent.ToggleFavorite(hero))
                                }
                            )
                            Spacer(Modifier.size(Spacing.sm))
                            SecondaryButton(
                                text = stringResource(R.string.home_next_recipe),
                                onClick = { onEventDispatcher(HomeContract.HomeEvent.NextHero) },
                                trailingIcon = Icons.AutoMirrored.Rounded.ArrowForward
                            )
                        }
                    }

                    item { Spacer(Modifier.size(Spacing.xl)) }

                    item {
                        SectionHeader(
                            title = stringResource(R.string.home_categories),
                            actionText = stringResource(R.string.common_all),
                            onActionClick = { onEventDispatcher(HomeContract.HomeEvent.OpenAllCategories) }
                        )
                        Spacer(Modifier.size(Spacing.sm))
                    }

                    items(
                        items = categoryRows,
                        key = { row -> row.first().key },
                        contentType = { CONTENT_CATEGORY }
                    ) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Spacing.sm),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                        ) {
                            row.forEach { category ->
                                CategoryCard(
                                    category = category,
                                    onClick = { onEventDispatcher(HomeContract.HomeEvent.OpenCategory(category)) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (row.size == 1) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }

                    item { Spacer(Modifier.size(Spacing.lg)) }

                    item {
                        FridgeBlock(
                            onClick = { onEventDispatcher(HomeContract.HomeEvent.OpenIngredientSearch) }
                        )
                        Spacer(Modifier.size(Spacing.xl))
                    }

                    item {
                        SectionHeader(title = stringResource(R.string.home_recipes))
                        Spacer(Modifier.size(Spacing.sm))
                    }

                    items(items = state.recipes, key = { it.id }, contentType = { CONTENT_RECIPE }) { recipe ->
                        RecipeListCard(
                            recipe = recipe,
                            onClick = { onEventDispatcher(HomeContract.HomeEvent.OpenRecipe(recipe.id)) },
                            onBookmarkClick = {
                                onEventDispatcher(HomeContract.HomeEvent.ToggleFavorite(recipe))
                            },
                            modifier = Modifier.padding(bottom = Spacing.sm)
                        )
                    }

                    item {
                        Spacer(Modifier.size(Spacing.xs))
                        SecondaryButton(
                            text = stringResource(R.string.home_all_recipes),
                            onClick = { onEventDispatcher(HomeContract.HomeEvent.OpenAllRecipes) }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun FridgeBlock(onClick: () -> Unit) {
        val colors = MaterialTheme.oshxona
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.card)
                .background(colors.accentTint)
                .scaleClickable(onClick = onClick)
                .padding(Spacing.md)
        ) {
            Text(
                text = stringResource(R.string.home_fridge_title),
                style = MaterialTheme.typography.titleSmall,
                color = colors.accentInk
            )
            Spacer(Modifier.size(Spacing.xxs))
            Text(
                text = stringResource(R.string.home_fridge_body),
                style = MaterialTheme.typography.bodySmall,
                color = colors.accentInk
            )
            Spacer(Modifier.size(Spacing.sm))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.home_fridge_action),
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.primaryInk
                )
                Spacer(Modifier.size(Spacing.xxs))
                androidx.compose.material3.Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    tint = colors.primaryInk,
                    modifier = Modifier.size(Sizes.iconSm)
                )
            }
        }
    }
}
