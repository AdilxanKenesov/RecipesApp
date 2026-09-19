package uz.gita.recipesapp.presenter.screens.categoryrecipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.presenter.ui.components.ErrorStateView
import uz.gita.recipesapp.presenter.ui.components.OshxonaScaffold
import uz.gita.recipesapp.presenter.ui.components.PagingFooter
import uz.gita.recipesapp.presenter.ui.components.RecipeListCard
import uz.gita.recipesapp.presenter.ui.components.RecipeListCardSkeleton
import uz.gita.recipesapp.presenter.ui.components.ScreenTopBar
import uz.gita.recipesapp.presenter.ui.components.StateView
import uz.gita.recipesapp.presenter.ui.components.rememberShimmer
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.util.CONTENT_RECIPE
import uz.gita.recipesapp.presenter.ui.util.RetryWhenOnline
import uz.gita.recipesapp.presenter.ui.util.toFooterState

class CategoryRecipesScreen(
    private val categoryKey: String
) : Screen {

    override val key: ScreenKey = "category_recipes_$categoryKey"

    @Composable
    override fun Content() {
        val viewModel: CategoryRecipesContract.CategoryRecipesViewModel =
            getViewModel<CategoryRecipesViewModel>()
        val state by viewModel.collectAsState()

        LaunchedEffect(categoryKey) {
            viewModel.onEventDispatcher(
                CategoryRecipesContract.CategoryRecipesEvent.Load(categoryKey)
            )
        }

        val recipes = viewModel.recipes.collectAsLazyPagingItems()

        CategoryRecipesContent(state, recipes, viewModel::onEventDispatcher)
    }

    @Composable
    private fun CategoryRecipesContent(
        state: CategoryRecipesContract.CategoryRecipesUiState,
        recipes: LazyPagingItems<RecipeUiData>,
        onEventDispatcher: (CategoryRecipesContract.CategoryRecipesEvent) -> Unit
    ) {
        val refreshState = recipes.loadState.refresh
        val loadError = (refreshState as? LoadState.Error ?: recipes.loadState.append as? LoadState.Error)?.error
        val retry: () -> Unit = {
            recipes.retry()
            onEventDispatcher(CategoryRecipesContract.CategoryRecipesEvent.Retry)
        }

        RetryWhenOnline(hasError = loadError != null, onRetry = retry)

        LaunchedEffect(loadError) {
            loadError?.let { onEventDispatcher(CategoryRecipesContract.CategoryRecipesEvent.LoadFailed(it)) }
        }

        OshxonaScaffold(
            topBar = {
                ScreenTopBar(
                    title = state.category?.let { "${it.emoji}  ${it.name}" }
                        ?: stringResource(R.string.categories_title),
                    onBack = { onEventDispatcher(CategoryRecipesContract.CategoryRecipesEvent.Back) }
                )
            }
        ) {
            when {
                refreshState is LoadState.Error && recipes.itemCount == 0 -> ErrorStateView(onRetry = retry)

                refreshState is LoadState.Loading && recipes.itemCount == 0 -> {
                    val shimmer = rememberShimmer()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = Spacing.md),
                        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                        userScrollEnabled = false
                    ) {
                        items(count = 5) { RecipeListCardSkeleton(shimmer = shimmer) }
                    }
                }

                recipes.itemCount == 0 && recipes.loadState.append.endOfPaginationReached -> StateView(
                    icon = Icons.Rounded.Inbox,
                    title = stringResource(R.string.state_empty_title),
                    body = stringResource(R.string.state_empty_body)
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Spacing.md,
                        end = Spacing.md,
                        bottom = Spacing.xxl
                    )
                ) {
                    items(
                        count = recipes.itemCount,
                        key = recipes.itemKey { it.id },
                        contentType = recipes.itemContentType { CONTENT_RECIPE }
                    ) { index ->
                        recipes[index]?.let { recipe ->
                            RecipeListCard(
                                recipe = recipe,
                                onClick = {
                                    onEventDispatcher(
                                        CategoryRecipesContract.CategoryRecipesEvent.OpenRecipe(recipe.id)
                                    )
                                },
                                onBookmarkClick = {
                                    onEventDispatcher(
                                        CategoryRecipesContract.CategoryRecipesEvent.ToggleFavorite(recipe)
                                    )
                                },
                                modifier = Modifier.padding(bottom = Spacing.sm)
                            )
                        }
                    }

                    item {
                        PagingFooter(
                            state = recipes.loadState.append.toFooterState(),
                            onRetry = retry
                        )
                    }
                }
            }
        }
    }
}
