package uz.gita.recipesapp.presenter.screens.categoryrecipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.components.ErrorStateView
import uz.gita.recipesapp.presenter.ui.components.OshxonaScaffold
import uz.gita.recipesapp.presenter.ui.components.RecipeListCard
import uz.gita.recipesapp.presenter.ui.components.RecipeListCardSkeleton
import uz.gita.recipesapp.presenter.ui.components.ScreenTopBar
import uz.gita.recipesapp.presenter.ui.components.StateView
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import uz.gita.recipesapp.presenter.ui.preview.ThemePreview
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Inbox

class CategoryRecipesScreen(
    private val categoryKey: String = "gosht"
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

        CategoryRecipesContent(state, viewModel::onEventDispatcher)
    }

    @Composable
    private fun CategoryRecipesContent(
        state: CategoryRecipesContract.CategoryRecipesUiState,
        onEventDispatcher: (CategoryRecipesContract.CategoryRecipesEvent) -> Unit
    ) {
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
                state.hasError -> ErrorStateView(
                    onRetry = { onEventDispatcher(CategoryRecipesContract.CategoryRecipesEvent.Retry) }
                )

                state.isLoading -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    items(items = (1..5).toList()) { RecipeListCardSkeleton() }
                }

                state.recipes.isEmpty() -> StateView(
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
                    items(items = state.recipes, key = { it.id }) { recipe ->
                        RecipeListCard(
                            recipe = recipe,
                            onClick = {
                                onEventDispatcher(
                                    CategoryRecipesContract.CategoryRecipesEvent.OpenRecipe(recipe.id)
                                )
                            },
                            onBookmarkClick = {
                                onEventDispatcher(
                                    CategoryRecipesContract.CategoryRecipesEvent.ToggleFavorite(recipe.id)
                                )
                            },
                            modifier = Modifier.padding(bottom = Spacing.sm)
                        )
                    }

                }
            }
        }
    }

    @ThemePreview
    @Composable
    private fun CategoryRecipesPreview() {
        OshxonaTheme {
            CategoryRecipesContent(
                state = CategoryRecipesContract.CategoryRecipesUiState(
                    category = SampleData.categories.first { it.key == "gosht" },
                    recipes = SampleData.recipes.filter { it.categoryKey == "gosht" }
                ),
                onEventDispatcher = { }
            )
        }
    }
}
