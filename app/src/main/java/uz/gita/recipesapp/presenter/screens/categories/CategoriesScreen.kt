package uz.gita.recipesapp.presenter.screens.categories

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
import uz.gita.recipesapp.presenter.ui.components.CategoryCard
import uz.gita.recipesapp.presenter.ui.components.CategoryCardSkeleton
import uz.gita.recipesapp.presenter.ui.components.ErrorStateView
import uz.gita.recipesapp.presenter.ui.components.OshxonaScaffold
import uz.gita.recipesapp.presenter.ui.components.ScreenTopBar
import uz.gita.recipesapp.presenter.ui.components.rememberShimmer
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.CONTENT_CATEGORY
import uz.gita.recipesapp.presenter.ui.util.RetryWhenOnline

class CategoriesScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: CategoriesContract.CategoriesViewModel = getViewModel<CategoriesViewModel>()
        val state by viewModel.collectAsState()

        RetryWhenOnline(hasError = state.hasError) {
            viewModel.onEventDispatcher(CategoriesContract.CategoriesEvent.Retry)
        }

        CategoriesContent(state, viewModel::onEventDispatcher)
    }

    @Composable
    private fun CategoriesContent(
        state: CategoriesContract.CategoriesUiState,
        onEventDispatcher: (CategoriesContract.CategoriesEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona

        OshxonaScaffold(
            topBar = { ScreenTopBar(title = stringResource(R.string.categories_title)) }
        ) {
            if (state.hasError) {
                ErrorStateView(onRetry = { onEventDispatcher(CategoriesContract.CategoriesEvent.Retry) })
            } else if (state.isLoading) {
                val shimmer = rememberShimmer()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = Spacing.md),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                    userScrollEnabled = false
                ) {
                    items(count = 10) { CategoryCardSkeleton(shimmer = shimmer) }
                }
            } else {
                LazyVerticalGrid(
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
                    items(items = state.available, key = { it.key }, contentType = { CONTENT_CATEGORY }) { category ->
                        CategoryCard(
                            category = category,
                            onClick = {
                                onEventDispatcher(CategoriesContract.CategoriesEvent.OpenCategory(category))
                            }
                        )
                    }

                    if (state.empty.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Column {
                                Spacer(Modifier.size(Spacing.lg))
                                Text(
                                    text = stringResource(R.string.categories_empty_section),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = colors.inkMuted,
                                    modifier = Modifier.padding(bottom = Spacing.xxs)
                                )
                            }
                        }

                        items(items = state.empty, key = { it.key }, contentType = { CONTENT_CATEGORY }) { category ->
                            CategoryCard(
                                category = category,
                                onClick = { },
                                enabled = false
                            )
                        }
                    }
                }
            }
        }
    }
}
