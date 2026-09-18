package uz.gita.recipesapp.presenter.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.components.CategoryChip
import uz.gita.recipesapp.presenter.ui.components.ErrorStateView
import uz.gita.recipesapp.presenter.ui.components.IngredientChipInput
import uz.gita.recipesapp.presenter.ui.components.NotFoundStateView
import uz.gita.recipesapp.presenter.ui.components.OshxonaScaffold
import uz.gita.recipesapp.presenter.ui.components.PrimaryButton
import uz.gita.recipesapp.presenter.ui.components.RecipeListCard
import uz.gita.recipesapp.presenter.ui.components.RecipeListCardSkeleton
import uz.gita.recipesapp.presenter.ui.components.ScreenTopBar
import uz.gita.recipesapp.presenter.ui.components.SearchBar
import uz.gita.recipesapp.presenter.ui.components.SectionHeader
import uz.gita.recipesapp.presenter.ui.components.SegmentedControl
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import uz.gita.recipesapp.presenter.ui.preview.ThemePreview
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

class SearchScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: SearchContract.SearchViewModel = getViewModel<SearchViewModel>()
        val state by viewModel.collectAsState()

        SearchContent(state, viewModel::onEventDispatcher)
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun SearchContent(
        state: SearchContract.SearchUiState,
        onEventDispatcher: (SearchContract.SearchEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        val byIngredient = state.mode == SearchContract.SearchMode.BY_INGREDIENT

        OshxonaScaffold(
            topBar = { ScreenTopBar(title = stringResource(R.string.search_title)) }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.padding(horizontal = Spacing.md)) {
                    SegmentedControl(
                        options = listOf(
                            stringResource(R.string.search_tab_by_name),
                            stringResource(R.string.search_tab_by_ingredient)
                        ),
                        selectedIndex = if (byIngredient) 1 else 0,
                        onSelect = { index ->
                            onEventDispatcher(
                                SearchContract.SearchEvent.ModeChanged(
                                    if (index == 1) SearchContract.SearchMode.BY_INGREDIENT
                                    else SearchContract.SearchMode.BY_NAME
                                )
                            )
                        }
                    )
                    Spacer(Modifier.size(Spacing.sm))

                    if (byIngredient) {
                        IngredientChipInput(
                            items = state.ingredients,
                            value = state.ingredientInput,
                            onValueChange = {
                                onEventDispatcher(SearchContract.SearchEvent.IngredientInputChanged(it))
                            },
                            onAdd = { onEventDispatcher(SearchContract.SearchEvent.AddIngredient) },
                            onRemove = {
                                onEventDispatcher(SearchContract.SearchEvent.RemoveIngredient(it))
                            },
                            placeholder = stringResource(R.string.search_hint_ingredient)
                        )
                    } else {
                        SearchBar(
                            value = state.query,
                            onValueChange = {
                                onEventDispatcher(SearchContract.SearchEvent.QueryChanged(it))
                            },
                            placeholder = stringResource(R.string.search_hint_name)
                        )
                    }
                    Spacer(Modifier.size(Spacing.md))
                }

                Box(modifier = Modifier.weight(1f)) {
                    when {
                        state.hasError -> ErrorStateView(
                            onRetry = { onEventDispatcher(SearchContract.SearchEvent.Retry) }
                        )

                        state.isLoading -> Column(
                            modifier = Modifier.padding(horizontal = Spacing.md),
                            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                        ) {
                            repeat(4) { RecipeListCardSkeleton() }
                        }

                        state.showNotFound -> {
                            val switchLabel = stringResource(
                                if (byIngredient) R.string.search_switch_to_name
                                else R.string.search_switch_to_ingredient
                            )
                            val switchAction: () -> Unit = {
                                onEventDispatcher(
                                    SearchContract.SearchEvent.ModeChanged(
                                        if (byIngredient) SearchContract.SearchMode.BY_NAME
                                        else SearchContract.SearchMode.BY_INGREDIENT
                                    )
                                )
                            }
                            NotFoundStateView(
                                title = stringResource(R.string.search_notfound_title),
                                body = stringResource(R.string.search_notfound_body),
                                secondaryAction = switchLabel to switchAction
                            )
                        }

                        state.results != null -> ResultsList(state, onEventDispatcher)

                        byIngredient -> IngredientIdle(state, onEventDispatcher)

                        else -> NameIdle(state, onEventDispatcher)
                    }
                }

                if (byIngredient && state.ingredients.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.ground)
                            .imePadding()
                            .padding(Spacing.md)
                    ) {
                        PrimaryButton(
                            text = stringResource(
                                R.string.search_find_by_ingredients,
                                state.ingredients.size
                            ),
                            onClick = { onEventDispatcher(SearchContract.SearchEvent.FindByIngredients) }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ResultsList(
        state: SearchContract.SearchUiState,
        onEventDispatcher: (SearchContract.SearchEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Spacing.md,
                end = Spacing.md,
                bottom = Spacing.xxl
            )
        ) {
            item {
                SectionHeader(title = stringResource(R.string.search_results_title))
                Spacer(Modifier.size(Spacing.sm))
            }

            items(items = state.results.orEmpty(), key = { it.id }) { recipe ->
                RecipeListCard(
                    recipe = recipe,
                    onClick = { onEventDispatcher(SearchContract.SearchEvent.OpenRecipe(recipe.id)) },
                    onBookmarkClick = {
                        onEventDispatcher(SearchContract.SearchEvent.ToggleFavorite(recipe.id))
                    },
                    modifier = Modifier.padding(bottom = Spacing.sm)
                )
            }

            if (state.limitReached) {
                item {
                    Text(
                        text = stringResource(R.string.search_limit_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.accentInk,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(Shapes.input)
                            .background(colors.accentTint)
                            .padding(Spacing.sm)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun NameIdle(
        state: SearchContract.SearchUiState,
        onEventDispatcher: (SearchContract.SearchEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Spacing.md,
                end = Spacing.md,
                bottom = Spacing.xxl
            )
        ) {
            item {
                Text(
                    text = stringResource(R.string.search_initial_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.ink
                )
                Spacer(Modifier.size(Spacing.xxs))
                Text(
                    text = stringResource(R.string.search_initial_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.inkMuted
                )
                if (state.query.isNotEmpty() && state.query.length < 2) {
                    Spacer(Modifier.size(Spacing.xs))
                    Text(
                        text = stringResource(R.string.search_min_chars),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.inkFaint
                    )
                }
                Spacer(Modifier.size(Spacing.lg))
            }

            if (state.recent.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = stringResource(R.string.search_recent),
                        actionText = stringResource(R.string.search_recent_clear),
                        onActionClick = { onEventDispatcher(SearchContract.SearchEvent.ClearRecent) }
                    )
                    Spacer(Modifier.size(Spacing.xs))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        state.recent.forEach { query ->
                            Text(
                                text = query,
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.ink,
                                modifier = Modifier
                                    .padding(bottom = Spacing.xs)
                                    .clip(Shapes.pill)
                                    .background(colors.surfaceAlt)
                                    .scaleClickable {
                                        onEventDispatcher(SearchContract.SearchEvent.RecentClicked(query))
                                    }
                                    .padding(horizontal = Spacing.sm, vertical = Spacing.sm)
                            )
                        }
                    }
                    Spacer(Modifier.size(Spacing.lg))
                }
            }

            item {
                SectionHeader(title = stringResource(R.string.categories_title))
                Spacer(Modifier.size(Spacing.xs))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    state.categories.forEach { category ->
                        CategoryChip(
                            emoji = category.emoji,
                            name = category.name,
                            onClick = {
                                onEventDispatcher(SearchContract.SearchEvent.OpenCategory(category))
                            },
                            modifier = Modifier.padding(bottom = Spacing.xs)
                        )
                    }
                }
                Spacer(Modifier.size(Spacing.lg))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(Shapes.card)
                        .background(colors.accentTint)
                        .scaleClickable {
                            onEventDispatcher(
                                SearchContract.SearchEvent.ModeChanged(
                                    SearchContract.SearchMode.BY_INGREDIENT
                                )
                            )
                        }
                        .padding(Spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = colors.accentInk
                    )
                    Spacer(Modifier.size(Spacing.sm))
                    Text(
                        text = stringResource(R.string.search_switch_to_ingredient),
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.accentInk
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun IngredientIdle(
        state: SearchContract.SearchUiState,
        onEventDispatcher: (SearchContract.SearchEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.md)
        ) {
            Text(
                text = stringResource(R.string.search_ingredient_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.inkMuted,
                textAlign = TextAlign.Start
            )
            Spacer(Modifier.size(Spacing.lg))
            SectionHeader(title = stringResource(R.string.search_quick_ingredients))
            Spacer(Modifier.size(Spacing.xs))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                state.quickIngredients.forEach { name ->
                    val selected = state.ingredients.contains(name)
                    CategoryChip(
                        emoji = "",
                        name = name,
                        selected = selected,
                        onClick = {
                            onEventDispatcher(SearchContract.SearchEvent.QuickIngredientClicked(name))
                        },
                        modifier = Modifier.padding(bottom = Spacing.xs)
                    )
                }
            }
        }
    }

    @ThemePreview
    @Composable
    private fun SearchPreview() {
        OshxonaTheme {
            SearchContent(
                state = SearchContract.SearchUiState(
                    recent = SampleData.recentSearches,
                    quickIngredients = SampleData.quickIngredients,
                    categories = SampleData.categories.filter { it.count > 0 }
                ),
                onEventDispatcher = { }
            )
        }
    }
}
