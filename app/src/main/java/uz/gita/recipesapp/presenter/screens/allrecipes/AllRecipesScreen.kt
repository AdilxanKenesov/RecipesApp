package uz.gita.recipesapp.presenter.screens.allrecipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.presenter.ui.components.ErrorStateView
import uz.gita.recipesapp.presenter.ui.components.OshxonaScaffold
import uz.gita.recipesapp.presenter.ui.components.PagingFooter
import uz.gita.recipesapp.presenter.ui.components.RecipeGridCard
import uz.gita.recipesapp.presenter.ui.components.RecipeGridCardSkeleton
import uz.gita.recipesapp.presenter.ui.components.ScreenTopBar
import uz.gita.recipesapp.presenter.ui.components.rememberShimmer
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.util.CONTENT_RECIPE
import uz.gita.recipesapp.presenter.ui.util.RetryWhenOnline
import uz.gita.recipesapp.presenter.ui.util.toFooterState

class AllRecipesScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: AllRecipesContract.AllRecipesViewModel = getViewModel<AllRecipesViewModel>()
        val recipes = viewModel.recipes.collectAsLazyPagingItems()

        AllRecipesContent(recipes, viewModel::onEventDispatcher)
    }

    @Composable
    private fun AllRecipesContent(
        recipes: LazyPagingItems<RecipeUiData>,
        onEventDispatcher: (AllRecipesContract.AllRecipesEvent) -> Unit
    ) {
        val refreshState = recipes.loadState.refresh
        val loadError = (refreshState as? LoadState.Error ?: recipes.loadState.append as? LoadState.Error)?.error
        val retry: () -> Unit = { recipes.retry() }

        RetryWhenOnline(hasError = loadError != null, onRetry = retry)

        LaunchedEffect(loadError) {
            loadError?.let { onEventDispatcher(AllRecipesContract.AllRecipesEvent.LoadFailed(it)) }
        }

        OshxonaScaffold(
            topBar = {
                ScreenTopBar(
                    title = stringResource(R.string.all_recipes_title),
                    onBack = { onEventDispatcher(AllRecipesContract.AllRecipesEvent.Back) }
                )
            }
        ) {
            when {
                refreshState is LoadState.Error && recipes.itemCount == 0 -> ErrorStateView(onRetry = retry)

                refreshState is LoadState.Loading && recipes.itemCount == 0 -> {
                    val shimmer = rememberShimmer()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = Spacing.md),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                        userScrollEnabled = false
                    ) {
                        items(count = 6) { RecipeGridCardSkeleton(shimmer = shimmer) }
                    }
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
                    items(
                        count = recipes.itemCount,
                        key = recipes.itemKey { it.id },
                        contentType = recipes.itemContentType { CONTENT_RECIPE }
                    ) { index ->
                        recipes[index]?.let { recipe ->
                            RecipeGridCard(
                                recipe = recipe,
                                onClick = {
                                    onEventDispatcher(AllRecipesContract.AllRecipesEvent.OpenRecipe(recipe.id))
                                },
                                onBookmarkClick = {
                                    onEventDispatcher(AllRecipesContract.AllRecipesEvent.ToggleFavorite(recipe))
                                }
                            )
                        }
                    }

                    item(span = { GridItemSpan(2) }) {
                        PagingFooter(
                            state = recipes.loadState.append.toFooterState(),
                            onRetry = retry,
                            modifier = Modifier.padding(top = Spacing.sm)
                        )
                    }
                }
            }
        }
    }
}
