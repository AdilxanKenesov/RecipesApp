package uz.gita.recipesapp.presenter.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.module.ShoppingItemUiData
import uz.gita.recipesapp.presenter.ui.components.OshxonaScaffold
import uz.gita.recipesapp.presenter.ui.components.RecipeListCard
import uz.gita.recipesapp.presenter.ui.components.ScreenTopBar
import uz.gita.recipesapp.presenter.ui.components.SegmentedControl
import uz.gita.recipesapp.presenter.ui.components.StateView
import uz.gita.recipesapp.presenter.ui.components.oshxonaCheckboxColors
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.CONTENT_RECIPE
import uz.gita.recipesapp.presenter.ui.util.CONTENT_SHOPPING
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

class SavedScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: SavedContract.SavedViewModel = getViewModel<SavedViewModel>()
        val state by viewModel.collectAsState()
        val pagerState = rememberPagerState(initialPage = state.section.ordinal) { SECTION_COUNT }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.settledPage }.drop(1).collect { page ->
                viewModel.onEventDispatcher(
                    SavedContract.SavedEvent.SectionChanged(SavedContract.SavedSection.entries[page])
                )
            }
        }

        LaunchedEffect(state.section) {
            val target = state.section.ordinal
            if (pagerState.settledPage != target) pagerState.animateScrollToPage(target)
        }

        SavedContent(state, pagerState, viewModel::onEventDispatcher)
    }

    @Composable
    private fun SavedContent(
        state: SavedContract.SavedUiState,
        pagerState: PagerState,
        onEventDispatcher: (SavedContract.SavedEvent) -> Unit
    ) {
        val scope = rememberCoroutineScope()

        OshxonaScaffold(
            topBar = { ScreenTopBar(title = stringResource(R.string.saved_title)) }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SegmentedControl(
                    options = listOf(
                        stringResource(R.string.saved_tab_favorites),
                        stringResource(R.string.saved_tab_shopping)
                    ),
                    selectedIndex = pagerState.currentPage,
                    position = pagerState.currentPage + pagerState.currentPageOffsetFraction,
                    onSelect = { index -> scope.launch { pagerState.animateScrollToPage(index) } },
                    modifier = Modifier.padding(horizontal = Spacing.md)
                )
                Spacer(Modifier.size(Spacing.md))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->
                    when (SavedContract.SavedSection.entries[page]) {
                        SavedContract.SavedSection.FAVORITES -> FavoritesSection(state, onEventDispatcher)
                        SavedContract.SavedSection.SHOPPING -> ShoppingSection(state, onEventDispatcher)
                    }
                }
            }
        }
    }

    @Composable
    private fun FavoritesSection(
        state: SavedContract.SavedUiState,
        onEventDispatcher: (SavedContract.SavedEvent) -> Unit
    ) {
        if (state.favorites.isEmpty()) {
            EmptySection(
                icon = Icons.Rounded.Bookmarks,
                title = stringResource(R.string.saved_empty_favorites_title),
                onEventDispatcher = onEventDispatcher
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = Spacing.md,
                    end = Spacing.md,
                    bottom = Spacing.xxl
                )
            ) {
                items(items = state.favorites, key = { it.id }, contentType = { CONTENT_RECIPE }) { recipe ->
                    RecipeListCard(
                        recipe = recipe,
                        onClick = { onEventDispatcher(SavedContract.SavedEvent.OpenRecipe(recipe.id)) },
                        onBookmarkClick = {
                            onEventDispatcher(SavedContract.SavedEvent.ToggleFavorite(recipe.id))
                        },
                        modifier = Modifier.padding(bottom = Spacing.sm)
                    )
                }
            }
        }
    }

    @Composable
    private fun ShoppingSection(
        state: SavedContract.SavedUiState,
        onEventDispatcher: (SavedContract.SavedEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        val groups = remember(state.shopping) { state.shopping.groupBy { it.recipeId } }

        if (groups.isEmpty()) {
            EmptySection(
                icon = Icons.Rounded.Checklist,
                title = stringResource(R.string.saved_empty_shopping_title),
                onEventDispatcher = onEventDispatcher
            )
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Spacing.md,
                end = Spacing.md,
                bottom = Spacing.xxl
            )
        ) {
            item(key = "clear") {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Text(
                        text = stringResource(R.string.saved_shopping_clear),
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.inkMuted,
                        modifier = Modifier
                            .clip(Shapes.pill)
                            .scaleClickable { onEventDispatcher(SavedContract.SavedEvent.ClearShopping) }
                            .padding(horizontal = Spacing.sm, vertical = Spacing.xs)
                    )
                }
            }

            groups.forEach { (recipeId, groupItems) ->
                item(key = "recipe_$recipeId") {
                    Text(
                        text = groupItems.first().recipeTitle,
                        style = MaterialTheme.typography.titleSmall,
                        color = colors.primaryInk,
                        modifier = Modifier.padding(vertical = Spacing.xs)
                    )
                }
                items(items = groupItems, key = { it.id }, contentType = { CONTENT_SHOPPING }) { item ->
                    ShoppingRow(
                        item = item,
                        onToggle = {
                            onEventDispatcher(SavedContract.SavedEvent.ToggleShoppingItem(item.id))
                        }
                    )
                }
                if (groupItems.all { it.isChecked }) {
                    item(key = "cook_$recipeId") {
                        ReadyToCookButton(
                            onClick = { onEventDispatcher(SavedContract.SavedEvent.StartCooking(recipeId)) }
                        )
                    }
                }
                item(key = "gap_$recipeId") { Spacer(Modifier.size(Spacing.sm)) }
            }
        }
    }

    @Composable
    private fun ShoppingRow(
        item: ShoppingItemUiData,
        onToggle: () -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.input)
                .background(colors.surface)
                .scaleClickable(role = null, onClick = onToggle)
                .padding(horizontal = Spacing.xs, vertical = Spacing.xxs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onToggle() },
                colors = oshxonaCheckboxColors()
            )
            Text(
                text = item.amount,
                style = MaterialTheme.typography.labelMedium,
                color = if (item.isChecked) colors.inkDisabled else colors.ink,
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else null
            )
            Spacer(Modifier.size(Spacing.xs))
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                color = if (item.isChecked) colors.inkDisabled else colors.inkMuted,
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else null
            )
        }
        Spacer(Modifier.size(Spacing.xxs))
    }

    @Composable
    private fun ReadyToCookButton(onClick: () -> Unit) {
        val colors = MaterialTheme.oshxona
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.xs)
                .clip(Shapes.pill)
                .background(colors.primaryTint)
                .scaleClickable(onClick = onClick)
                .height(Sizes.touchTarget)
                .padding(horizontal = Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = null,
                tint = colors.primaryInk,
                modifier = Modifier.size(Sizes.iconSm)
            )
            Text(
                text = stringResource(R.string.saved_shopping_ready),
                style = MaterialTheme.typography.labelMedium,
                color = colors.inkMuted
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(R.string.saved_shopping_cook),
                style = MaterialTheme.typography.labelMedium,
                color = colors.primaryInk
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = colors.primaryInk,
                modifier = Modifier.size(Sizes.iconSm)
            )
        }
    }

    @Composable
    private fun EmptySection(
        icon: ImageVector,
        title: String,
        onEventDispatcher: (SavedContract.SavedEvent) -> Unit
    ) {
        val categories: () -> Unit = { onEventDispatcher(SavedContract.SavedEvent.OpenCategories) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            StateView(
                icon = icon,
                title = title,
                primaryAction = stringResource(R.string.saved_empty_categories) to categories
            )
        }
    }

    private companion object {
        const val SECTION_COUNT = 2
    }
}
