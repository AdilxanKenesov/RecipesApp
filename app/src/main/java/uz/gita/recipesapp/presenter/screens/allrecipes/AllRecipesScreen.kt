package uz.gita.recipesapp.presenter.screens.allrecipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.components.ErrorStateView
import uz.gita.recipesapp.presenter.ui.components.OshxonaScaffold
import uz.gita.recipesapp.presenter.ui.components.PagingFooter
import uz.gita.recipesapp.presenter.ui.components.RecipeGridCard
import uz.gita.recipesapp.presenter.ui.components.RecipeGridCardSkeleton
import uz.gita.recipesapp.presenter.ui.components.ScreenTopBar
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import uz.gita.recipesapp.presenter.ui.preview.ThemePreview
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona

class AllRecipesScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: AllRecipesContract.AllRecipesViewModel = getViewModel<AllRecipesViewModel>()
        val state by viewModel.collectAsState()

        AllRecipesContent(state, viewModel::onEventDispatcher)
    }

    @Composable
    private fun AllRecipesContent(
        state: AllRecipesContract.AllRecipesUiState,
        onEventDispatcher: (AllRecipesContract.AllRecipesEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona

        OshxonaScaffold(
            topBar = {
                ScreenTopBar(
                    title = stringResource(R.string.all_recipes_title),
                    onBack = { onEventDispatcher(AllRecipesContract.AllRecipesEvent.Back) }
                )
            }
        ) {
            when {
                state.hasError -> ErrorStateView(
                    onRetry = { onEventDispatcher(AllRecipesContract.AllRecipesEvent.Retry) }
                )

                state.isLoading -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = Spacing.md),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    items(items = (1..6).toList()) { RecipeGridCardSkeleton() }
                }

                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Spacing.md,
                        end = Spacing.md,
                        bottom = Spacing.xxl
                    ),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Column {
                            Text(
                                text = stringResource(R.string.all_recipes_total, state.totalCount),
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.inkMuted
                            )
                            Spacer(Modifier.size(Spacing.xs))
                        }
                    }

                    items(items = state.recipes, key = { it.id }) { recipe ->
                        RecipeGridCard(
                            recipe = recipe,
                            onClick = {
                                onEventDispatcher(AllRecipesContract.AllRecipesEvent.OpenRecipe(recipe.id))
                            },
                            onBookmarkClick = {
                                onEventDispatcher(AllRecipesContract.AllRecipesEvent.ToggleFavorite(recipe.id))
                            }
                        )
                    }

                    item(span = { GridItemSpan(2) }) {
                        PagingFooter(
                            state = state.footerState,
                            onRetry = { onEventDispatcher(AllRecipesContract.AllRecipesEvent.Retry) },
                            modifier = Modifier.padding(top = Spacing.sm)
                        )
                    }
                }
            }
        }
    }

    @ThemePreview
    @Composable
    private fun AllRecipesPreview() {
        OshxonaTheme {
            AllRecipesContent(
                state = AllRecipesContract.AllRecipesUiState(
                    recipes = SampleData.recipes,
                    totalCount = SampleData.totalRecipeCount
                ),
                onEventDispatcher = { }
            )
        }
    }
}
