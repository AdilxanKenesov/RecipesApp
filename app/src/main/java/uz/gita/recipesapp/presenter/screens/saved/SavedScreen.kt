package uz.gita.recipesapp.presenter.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.module.ShoppingItemUiData
import uz.gita.recipesapp.presenter.ui.components.OshxonaScaffold
import uz.gita.recipesapp.presenter.ui.components.RecipeListCard
import uz.gita.recipesapp.presenter.ui.components.ScreenTopBar
import uz.gita.recipesapp.presenter.ui.components.SectionHeader
import uz.gita.recipesapp.presenter.ui.components.SegmentedControl
import uz.gita.recipesapp.presenter.ui.components.StateView
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import uz.gita.recipesapp.presenter.ui.preview.ThemePreview
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

class SavedScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: SavedContract.SavedViewModel = getViewModel<SavedViewModel>()
        val state by viewModel.collectAsState()

        SavedContent(state, viewModel::onEventDispatcher)
    }

    @Composable
    private fun SavedContent(
        state: SavedContract.SavedUiState,
        onEventDispatcher: (SavedContract.SavedEvent) -> Unit
    ) {
        OshxonaScaffold(
            topBar = { ScreenTopBar(title = stringResource(R.string.saved_title)) }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SegmentedControl(
                    options = listOf(
                        stringResource(R.string.saved_tab_favorites),
                        stringResource(R.string.saved_tab_shopping)
                    ),
                    selectedIndex = state.section.ordinal,
                    onSelect = { index ->
                        onEventDispatcher(
                            SavedContract.SavedEvent.SectionChanged(
                                SavedContract.SavedSection.entries[index]
                            )
                        )
                    },
                    modifier = Modifier.padding(horizontal = Spacing.md)
                )
                Spacer(Modifier.size(Spacing.md))

                Box(modifier = Modifier.weight(1f)) {
                    when (state.section) {
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
        val colors = MaterialTheme.oshxona
        if (state.favorites.isEmpty()) {
            EmptySection(
                icon = Icons.Rounded.Bookmarks,
                title = stringResource(R.string.saved_empty_favorites_title),
                body = stringResource(R.string.saved_empty_favorites_body),
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
                item {
                    Text(
                        text = stringResource(R.string.saved_favorites_hint),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.inkFaint,
                        modifier = Modifier.padding(bottom = Spacing.sm)
                    )
                }
                items(items = state.favorites, key = { it.id }) { recipe ->
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
        if (state.shopping.isEmpty()) {
            EmptySection(
                icon = Icons.Rounded.Checklist,
                title = stringResource(R.string.saved_empty_shopping_title),
                body = stringResource(R.string.saved_empty_shopping_body),
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
                item {
                    SectionHeader(
                        title = stringResource(R.string.saved_tab_shopping),
                        actionText = stringResource(R.string.saved_shopping_clear),
                        onActionClick = { onEventDispatcher(SavedContract.SavedEvent.ClearShopping) }
                    )
                    Spacer(Modifier.size(Spacing.sm))
                }

                state.shopping.groupBy { it.recipeTitle }.forEach { (recipeTitle, groupItems) ->
                    item(key = "recipe_$recipeTitle") {
                        Text(
                            text = recipeTitle,
                            style = MaterialTheme.typography.titleSmall,
                            color = colors.primaryInk,
                            modifier = Modifier.padding(vertical = Spacing.xs)
                        )
                    }
                    items(items = groupItems, key = { it.id }) { item ->
                        ShoppingRow(
                            item = item,
                            onToggle = {
                                onEventDispatcher(SavedContract.SavedEvent.ToggleShoppingItem(item.id))
                            }
                        )
                    }
                }
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
                colors = CheckboxDefaults.colors(
                    checkedColor = colors.primary,
                    uncheckedColor = colors.hairline,
                    checkmarkColor = colors.onPrimary
                )
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
    private fun EmptySection(
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        title: String,
        body: String,
        onEventDispatcher: (SavedContract.SavedEvent) -> Unit
    ) {
        val browse: () -> Unit = { onEventDispatcher(SavedContract.SavedEvent.OpenAllRecipes) }
        val categories: () -> Unit = { onEventDispatcher(SavedContract.SavedEvent.OpenCategories) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            StateView(
                icon = icon,
                title = title,
                body = body,
                primaryAction = stringResource(R.string.saved_empty_browse) to browse,
                secondaryAction = stringResource(R.string.saved_empty_categories) to categories
            )
        }
    }

    @ThemePreview
    @Composable
    private fun SavedPreview() {
        OshxonaTheme {
            SavedContent(
                state = SavedContract.SavedUiState(
                    favorites = SampleData.favorites,
                    shopping = SampleData.shoppingItems
                ),
                onEventDispatcher = { }
            )
        }
    }
}
