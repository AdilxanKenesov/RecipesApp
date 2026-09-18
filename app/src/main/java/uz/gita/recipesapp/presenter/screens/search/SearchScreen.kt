package uz.gita.recipesapp.presenter.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.module.RecipeUiData
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
import uz.gita.recipesapp.presenter.ui.components.SelectableChip
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
        val pagerState = rememberPagerState(initialPage = state.mode.pageIndex()) { 2 }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.settledPage }.collect { page ->
                viewModel.onEventDispatcher(SearchContract.SearchEvent.ModeChanged(modeOf(page)))
            }
        }

        LaunchedEffect(state.mode) {
            val target = state.mode.pageIndex()
            if (pagerState.settledPage != target) pagerState.animateScrollToPage(target)
        }

        SearchContent(state, pagerState, viewModel::onEventDispatcher)
    }

    @Composable
    private fun SearchContent(
        state: SearchContract.SearchUiState,
        pagerState: PagerState,
        onEventDispatcher: (SearchContract.SearchEvent) -> Unit
    ) {
        val scope = rememberCoroutineScope()

        OshxonaScaffold(
            topBar = { ScreenTopBar(title = stringResource(R.string.search_title)) }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SegmentedControl(
                    options = listOf(
                        stringResource(R.string.search_tab_by_name),
                        stringResource(R.string.search_tab_by_ingredient)
                    ),
                    selectedIndex = pagerState.currentPage,
                    position = pagerState.currentPage + pagerState.currentPageOffsetFraction,
                    onSelect = { index -> scope.launch { pagerState.animateScrollToPage(index) } },
                    modifier = Modifier.padding(horizontal = Spacing.md)
                )
                Spacer(Modifier.size(Spacing.sm))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->
                    if (page == 0) {
                        NamePage(state, onEventDispatcher)
                    } else {
                        IngredientPage(state, onEventDispatcher)
                    }
                }
            }
        }
    }

    @Composable
    private fun NamePage(
        state: SearchContract.SearchUiState,
        onEventDispatcher: (SearchContract.SearchEvent) -> Unit
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar(
                value = state.query,
                onValueChange = { onEventDispatcher(SearchContract.SearchEvent.QueryChanged(it)) },
                placeholder = stringResource(R.string.search_hint_name),
                modifier = Modifier.padding(horizontal = Spacing.md)
            )
            Spacer(Modifier.size(Spacing.md))

            Box(modifier = Modifier.weight(1f)) {
                when {
                    state.hasError -> ErrorStateView(
                        onRetry = { onEventDispatcher(SearchContract.SearchEvent.Retry) }
                    )

                    state.isNameLoading -> Column(
                        modifier = Modifier.padding(horizontal = Spacing.md),
                        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        repeat(4) { RecipeListCardSkeleton() }
                    }

                    state.showNameNotFound -> SearchNotFound()

                    state.nameResults != null -> ResultsList(
                        results = state.nameResults,
                        limitReached = state.nameLimitReached,
                        onEventDispatcher = onEventDispatcher
                    )

                    else -> NameIdle(state, onEventDispatcher)
                }
            }
        }
    }

    @Composable
    private fun IngredientPage(
        state: SearchContract.SearchUiState,
        onEventDispatcher: (SearchContract.SearchEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                IngredientContent(state, onEventDispatcher)
            }

            AnimatedVisibility(
                visible = state.showFindButton,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.ground)
                        .imePadding()
                        .padding(Spacing.md)
                ) {
                    PrimaryButton(
                        text = stringResource(R.string.search_find_by_ingredients, state.findCount),
                        onClick = { onEventDispatcher(SearchContract.SearchEvent.FindByIngredients) }
                    )
                }
            }
        }
    }

    @Composable
    private fun SearchNotFound() {
        NotFoundStateView(
            title = stringResource(R.string.search_notfound_title),
            body = stringResource(R.string.search_notfound_body)
        )
    }

    @OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
    @Composable
    private fun IngredientContent(
        state: SearchContract.SearchUiState,
        onEventDispatcher: (SearchContract.SearchEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = Spacing.xxl)
        ) {
            item(key = "ingredient_input") {
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
                    placeholder = stringResource(R.string.search_hint_ingredient),
                    modifier = Modifier.padding(horizontal = Spacing.md)
                )
                Spacer(Modifier.size(Spacing.md))
            }

            item(key = "quick_ingredients") {
                Column(modifier = Modifier.padding(horizontal = Spacing.md)) {
                    if (state.ingredients.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_ingredient_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.inkMuted
                        )
                        Spacer(Modifier.size(Spacing.lg))
                    }
                    SectionHeader(title = stringResource(R.string.search_quick_ingredients))
                    Spacer(Modifier.size(Spacing.xs))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        state.quickIngredients.forEach { name ->
                            SelectableChip(
                                text = name,
                                selected = name in state.ingredients,
                                onClick = {
                                    onEventDispatcher(SearchContract.SearchEvent.QuickIngredientClicked(name))
                                }
                            )
                        }
                    }
                    Spacer(Modifier.size(Spacing.lg))
                }
            }

            when {
                state.hasError -> item(key = "error") {
                    ErrorStateView(onRetry = { onEventDispatcher(SearchContract.SearchEvent.Retry) })
                }

                state.isIngredientLoading -> items(count = 4, key = { "skeleton_$it" }) {
                    RecipeListCardSkeleton(
                        modifier = Modifier
                            .padding(horizontal = Spacing.md)
                            .padding(bottom = Spacing.sm)
                    )
                }

                state.showIngredientNotFound -> item(key = "not_found") {
                    SearchNotFound()
                }

                state.ingredientResults != null -> {
                    stickyHeader(key = "results_header") {
                        SectionHeader(
                            title = stringResource(R.string.search_results_title),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colors.ground)
                                .padding(horizontal = Spacing.md, vertical = Spacing.xs)
                        )
                    }

                    items(items = state.ingredientResults, key = { "result_${it.id}" }) { recipe ->
                        RecipeListCard(
                            recipe = recipe,
                            onClick = { onEventDispatcher(SearchContract.SearchEvent.OpenRecipe(recipe.id)) },
                            onBookmarkClick = {
                                onEventDispatcher(SearchContract.SearchEvent.ToggleFavorite(recipe.id))
                            },
                            modifier = Modifier
                                .padding(horizontal = Spacing.md)
                                .padding(bottom = Spacing.sm)
                        )
                    }

                    if (state.ingredientLimitReached) {
                        item(key = "limit_hint") {
                            Text(
                                text = stringResource(R.string.search_limit_hint),
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.accentInk,
                                modifier = Modifier
                                    .padding(horizontal = Spacing.md)
                                    .fillMaxWidth()
                                    .clip(Shapes.input)
                                    .background(colors.accentTint)
                                    .padding(Spacing.sm)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ResultsList(
        results: List<RecipeUiData>,
        limitReached: Boolean,
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

            items(items = results, key = { it.id }) { recipe ->
                RecipeListCard(
                    recipe = recipe,
                    onClick = { onEventDispatcher(SearchContract.SearchEvent.OpenRecipe(recipe.id)) },
                    onBookmarkClick = {
                        onEventDispatcher(SearchContract.SearchEvent.ToggleFavorite(recipe.id))
                    },
                    modifier = Modifier.padding(bottom = Spacing.sm)
                )
            }

            if (limitReached) {
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

        }
    }

    @ThemePreview
    @Composable
    private fun SearchPreview() {
        OshxonaTheme {
            SearchContent(
                pagerState = rememberPagerState { 2 },
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

private fun SearchContract.SearchMode.pageIndex(): Int =
    if (this == SearchContract.SearchMode.BY_INGREDIENT) 1 else 0

private fun modeOf(page: Int): SearchContract.SearchMode =
    if (page == 1) SearchContract.SearchMode.BY_INGREDIENT else SearchContract.SearchMode.BY_NAME
