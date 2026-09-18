package uz.gita.recipesapp.presenter.screens.recipedetail

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.hilt.getViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.module.IngredientUiData
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.presenter.ui.components.BookmarkButton
import uz.gita.recipesapp.presenter.ui.components.GlassBottomSheet
import uz.gita.recipesapp.presenter.ui.components.IngredientGroupHeader
import uz.gita.recipesapp.presenter.ui.components.IngredientRow
import uz.gita.recipesapp.presenter.ui.components.NotFoundStateView
import uz.gita.recipesapp.presenter.ui.components.OshxonaIconButton
import uz.gita.recipesapp.presenter.ui.components.PrimaryButton
import uz.gita.recipesapp.presenter.ui.components.RecipeGridCard
import uz.gita.recipesapp.presenter.ui.components.RecipeImage
import uz.gita.recipesapp.presenter.ui.components.RecipeListCard
import uz.gita.recipesapp.presenter.ui.components.SheetHandle
import uz.gita.recipesapp.presenter.ui.components.SheetShape
import uz.gita.recipesapp.presenter.ui.components.glassOnImageStyle
import uz.gita.recipesapp.presenter.ui.components.glassStyle
import uz.gita.recipesapp.presenter.ui.components.glassTopEdge
import uz.gita.recipesapp.presenter.ui.components.rememberLastNonNull
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import uz.gita.recipesapp.presenter.ui.preview.ThemePreview
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Overlay
import uz.gita.recipesapp.presenter.ui.theme.Overline
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.cleanRecipeTitle
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

class RecipeDetailScreen(
    private val recipeId: Int = 1
) : Screen {

    override val key: ScreenKey = "recipe_detail_$recipeId"

    @Composable
    override fun Content() {
        val viewModel: RecipeDetailContract.RecipeDetailViewModel = getViewModel<RecipeDetailViewModel>()
        val state by viewModel.collectAsState()
        val context = LocalContext.current
        val addedMessage = stringResource(R.string.detail_added_to_shopping)

        viewModel.collectSideEffect { effect ->
            when (effect) {
                is RecipeDetailContract.SideEffect.OpenUrl ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(effect.url)))

                is RecipeDetailContract.SideEffect.ShareUrl -> {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, effect.url)
                    }
                    context.startActivity(Intent.createChooser(intent, null))
                }

                RecipeDetailContract.SideEffect.AddedToShoppingList ->
                    Toast.makeText(context, addedMessage, Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(recipeId) {
            viewModel.onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.Load(recipeId))
        }

        RecipeDetailContent(state, viewModel::onEventDispatcher)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun RecipeDetailContent(
        state: RecipeDetailContract.RecipeDetailUiState,
        onEventDispatcher: (RecipeDetailContract.RecipeDetailEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        val recipe = state.recipe

        if (state.notFound || recipe == null) {
            NotFoundContent(state, onEventDispatcher)
            return
        }

        val hazeState = rememberHazeState()
        val scaffoldState = rememberBottomSheetScaffoldState()
        val shownSelection = rememberLastNonNull(state.shoppingSelection)

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.ground)
        ) {
            val screenHeight = maxHeight

            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = screenHeight * 0.5f,
                sheetShape = SheetShape,
                sheetContainerColor = Color.Transparent,
                sheetContentColor = colors.ink,
                sheetTonalElevation = 0.dp,
                sheetShadowElevation = 0.dp,
                sheetDragHandle = null,
                containerColor = colors.ground,
                sheetContent = {
                    DetailSheet(
                        recipe = recipe,
                        state = state,
                        hazeState = hazeState,
                        height = screenHeight * 0.82f,
                        onEventDispatcher = onEventDispatcher
                    )
                }
            ) {
                HeroLayer(
                    recipe = recipe,
                    hazeState = hazeState,
                    screenHeight = screenHeight,
                    onEventDispatcher = onEventDispatcher
                )
            }

            TopActions(recipe, hazeState, onEventDispatcher)

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, colors.ground.copy(alpha = 0.92f), colors.ground)
                        )
                    )
                    .navigationBarsPadding()
                    .padding(start = Spacing.md, end = Spacing.md, top = Spacing.lg, bottom = Spacing.sm)
            ) {
                PrimaryButton(
                    text = stringResource(R.string.detail_start_cooking),
                    onClick = { onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.StartCooking) },
                    leadingIcon = Icons.Rounded.PlayArrow,
                    shape = Shapes.pill
                )
            }

            GlassBottomSheet(
                visible = state.shoppingSelection != null,
                onDismiss = { onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.CloseShoppingSheet) },
                hazeState = hazeState
            ) {
                ShoppingSheet(
                    recipe = recipe,
                    selection = shownSelection ?: emptySet(),
                    maxListHeight = screenHeight * 0.55f,
                    onEventDispatcher = onEventDispatcher
                )
            }
        }
    }

    @Composable
    private fun HeroLayer(
        recipe: RecipeDetailUiData,
        hazeState: HazeState,
        screenHeight: Dp,
        onEventDispatcher: (RecipeDetailContract.RecipeDetailEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.ground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.55f)
                    .hazeSource(state = hazeState, zIndex = 0f)
            ) {
                RecipeImage(
                    url = recipe.imageUrl,
                    toneSeed = recipe.id,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Overlay.copy(alpha = 0.45f),
                                    Color.Transparent,
                                    Overlay.copy(alpha = 0.25f)
                                )
                            )
                        )
                )
                if (recipe.hasVideo) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = screenHeight * 0.25f - 32.dp)
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(colors.accent)
                            .scaleClickable {
                                onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.OpenInBrowser)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = stringResource(R.string.cd_play),
                            tint = Overlay,
                            modifier = Modifier.size(Sizes.icon + 6.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun TopActions(
        recipe: RecipeDetailUiData,
        hazeState: HazeState,
        onEventDispatcher: (RecipeDetailContract.RecipeDetailEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        val glass = Modifier
            .clip(CircleShape)
            .hazeEffect(state = hazeState, style = glassOnImageStyle())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OshxonaIconButton(
                icon = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                onClick = { onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.Back) },
                modifier = glass,
                tint = Color.White
            )
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                OshxonaIconButton(
                    icon = Icons.Rounded.Share,
                    contentDescription = stringResource(R.string.cd_share),
                    onClick = { onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.Share) },
                    modifier = glass,
                    tint = Color.White
                )
                BookmarkButton(
                    isFavorite = recipe.isFavorite,
                    onClick = { onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.ToggleFavorite) },
                    modifier = glass,
                    tint = if (recipe.isFavorite) colors.accent else Color.White
                )
            }
        }
    }

    @Composable
    private fun DetailSheet(
        recipe: RecipeDetailUiData,
        state: RecipeDetailContract.RecipeDetailUiState,
        hazeState: HazeState,
        height: Dp,
        onEventDispatcher: (RecipeDetailContract.RecipeDetailEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(SheetShape)
                .hazeSource(state = hazeState, zIndex = 1f)
                .hazeEffect(state = hazeState, style = glassStyle())
                .glassTopEdge(colors.surface.copy(alpha = if (colors.isDark) 0.18f else 0.9f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SheetHandle()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = Spacing.md,
                    end = Spacing.md,
                    top = Spacing.xs,
                    bottom = Sizes.buttonHeightLarge + Spacing.xxl + Spacing.md
                )
            ) {
                item { AuthorRow(recipe) }

                item {
                    Spacer(Modifier.size(Spacing.md))
                    Text(
                        text = recipe.title.cleanRecipeTitle(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = colors.ink
                    )
                    Spacer(Modifier.size(Spacing.sm))
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.inkMuted
                    )
                    Spacer(Modifier.size(Spacing.lg))
                    MetaRow(recipe)
                    Spacer(Modifier.size(Spacing.lg))
                    IngredientsHeader(onEventDispatcher)
                }

                items(items = recipe.ingredients, key = { ingredient ->
                    when (ingredient) {
                        is IngredientUiData.Heading -> "heading_${ingredient.text}"
                        is IngredientUiData.Item -> "item_${ingredient.id}"
                    }
                }) { ingredient ->
                    when (ingredient) {
                        is IngredientUiData.Heading -> IngredientGroupHeader(text = ingredient.text)
                        is IngredientUiData.Item -> IngredientRow(
                            item = ingredient,
                            onCheckedChange = {
                                onEventDispatcher(
                                    RecipeDetailContract.RecipeDetailEvent.ToggleIngredient(ingredient.id)
                                )
                            }
                        )
                    }
                }

                item {
                    Spacer(Modifier.size(Spacing.lg))
                    HorizontalDivider(color = colors.hairline)
                    Spacer(Modifier.size(Spacing.lg))
                    PreparationSummary(recipe, onEventDispatcher)
                    Spacer(Modifier.size(Spacing.xl))
                }

                if (state.related.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.detail_more_from, recipe.categoryName),
                            style = MaterialTheme.typography.titleLarge,
                            color = colors.ink
                        )
                        Spacer(Modifier.size(Spacing.sm))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                            items(items = state.related, key = { it.id }) { related ->
                                RecipeGridCard(
                                    recipe = related,
                                    onClick = {
                                        onEventDispatcher(
                                            RecipeDetailContract.RecipeDetailEvent.OpenRecipe(related.id)
                                        )
                                    },
                                    onBookmarkClick = { },
                                    modifier = Modifier.width(164.dp)
                                )
                            }
                        }
                        Spacer(Modifier.size(Spacing.md))
                    }
                }

            }
        }
    }

    @Composable
    private fun AuthorRow(recipe: RecipeDetailUiData) {
        val colors = MaterialTheme.oshxona
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Sizes.touchTarget)
                    .clip(CircleShape)
                    .background(colors.primaryTint),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = recipe.author.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.primaryInk
                )
            }
            Spacer(Modifier.size(Spacing.sm))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.detail_author).uppercase(),
                    style = Overline,
                    color = colors.inkFaint
                )
                Text(
                    text = "${recipe.author} · ${recipe.publishedDate}",
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.ink
                )
            }
        }
    }

    @Composable
    private fun MetaRow(recipe: RecipeDetailUiData) {
        val colors = MaterialTheme.oshxona
        Column {
            HorizontalDivider(color = colors.hairline)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.md)
            ) {
                MetaCell(
                    label = stringResource(R.string.detail_meta_ingredients_label),
                    value = stringResource(R.string.categories_count, recipe.ingredientCount),
                    modifier = Modifier.weight(1f)
                )
                MetaCell(
                    label = stringResource(R.string.detail_meta_steps_label),
                    value = stringResource(R.string.categories_count, recipe.steps.size),
                    modifier = Modifier.weight(1f)
                )
                MetaCell(
                    label = stringResource(R.string.detail_meta_category_label),
                    value = recipe.categoryName,
                    modifier = Modifier.weight(1f)
                )
            }
            HorizontalDivider(color = colors.hairline)
        }
    }

    @Composable
    private fun MetaCell(
        label: String,
        value: String,
        modifier: Modifier = Modifier
    ) {
        val colors = MaterialTheme.oshxona
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label.uppercase(),
                style = Overline,
                color = colors.inkFaint,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(Spacing.xxs))
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                color = colors.ink,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun IngredientsHeader(
        onEventDispatcher: (RecipeDetailContract.RecipeDetailEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.detail_ingredients),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colors.ink,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier
                        .clip(Shapes.pill)
                        .background(colors.primaryTint)
                        .scaleClickable {
                            onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.OpenShoppingSheet)
                        }
                        .height(Sizes.touchTarget)
                        .padding(horizontal = Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlaylistAdd,
                        contentDescription = null,
                        tint = colors.primaryInk,
                        modifier = Modifier.size(Sizes.iconSm)
                    )
                    Text(
                        text = stringResource(R.string.detail_add_to_list_short),
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.primaryInk
                    )
                }
            }
            Spacer(Modifier.size(Spacing.xxs))
            Text(
                text = stringResource(R.string.detail_ingredients_hint),
                style = MaterialTheme.typography.bodySmall,
                color = colors.inkFaint
            )
            Spacer(Modifier.size(Spacing.xs))
        }
    }

    @Composable
    private fun PreparationSummary(
        recipe: RecipeDetailUiData,
        onEventDispatcher: (RecipeDetailContract.RecipeDetailEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        val timedSteps = recipe.steps.filter { it.timerMinutes != null }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.card)
                .background(colors.surface)
                .scaleClickable { onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.StartCooking) }
                .padding(Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Sizes.categoryIconBox)
                    .clip(Shapes.image)
                    .background(colors.accentTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Restaurant,
                    contentDescription = null,
                    tint = colors.accentInk,
                    modifier = Modifier.size(Sizes.icon)
                )
            }
            Spacer(Modifier.size(Spacing.sm))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.detail_steps),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.ink
                )
                Text(
                    text = stringResource(R.string.detail_steps_count, recipe.steps.size),
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.inkMuted
                )
                if (timedSteps.isNotEmpty()) {
                    Text(
                        text = stringResource(
                            R.string.detail_timers_summary,
                            timedSteps.size,
                            timedSteps.sumOf { it.timerMinutes ?: 0 }
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.inkFaint
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = colors.primaryInk,
                modifier = Modifier.size(Sizes.icon)
            )
        }
    }

    @Composable
    private fun ShoppingSheet(
        recipe: RecipeDetailUiData,
        selection: Set<Int>,
        maxListHeight: Dp,
        onEventDispatcher: (RecipeDetailContract.RecipeDetailEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        val allSelected = selection.size == recipe.ingredientCount
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.detail_add_to_shopping),
                style = MaterialTheme.typography.titleLarge,
                color = colors.ink
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.shopping_selected, selection.size),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.inkMuted
                )
                Text(
                    text = stringResource(
                        if (allSelected) R.string.shopping_clear_selection else R.string.shopping_select_all
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.primaryInk,
                    modifier = Modifier
                        .clip(Shapes.pill)
                        .scaleClickable {
                            onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.ToggleAllShopping)
                        }
                        .padding(horizontal = Spacing.xs, vertical = Spacing.sm)
                )
            }
            LazyColumn(modifier = Modifier.heightIn(max = maxListHeight)) {
                items(items = recipe.ingredients, key = { ingredient ->
                    when (ingredient) {
                        is IngredientUiData.Heading -> "shop_heading_${ingredient.text}"
                        is IngredientUiData.Item -> "shop_item_${ingredient.id}"
                    }
                }) { ingredient ->
                    when (ingredient) {
                        is IngredientUiData.Heading -> IngredientGroupHeader(text = ingredient.text)
                        is IngredientUiData.Item -> IngredientRow(
                            item = ingredient.copy(isChecked = ingredient.id in selection),
                            onCheckedChange = {
                                onEventDispatcher(
                                    RecipeDetailContract.RecipeDetailEvent.ToggleShoppingItem(ingredient.id)
                                )
                            },
                            strikeWhenChecked = false
                        )
                    }
                }
            }
            Spacer(Modifier.size(Spacing.md))
            PrimaryButton(
                text = stringResource(R.string.shopping_confirm, selection.size),
                onClick = { onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.ConfirmShopping) },
                enabled = selection.isNotEmpty(),
                leadingIcon = Icons.Rounded.PlaylistAdd,
                shape = Shapes.pill
            )
        }
    }

    @Composable
    private fun NotFoundContent(
        state: RecipeDetailContract.RecipeDetailUiState,
        onEventDispatcher: (RecipeDetailContract.RecipeDetailEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.ground)
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.topBarTop)
                    .padding(horizontal = Spacing.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OshxonaIconButton(
                    icon = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    onClick = { onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.Back) }
                )
            }
            val goHome: () -> Unit = { onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.OpenHome) }
            val openBrowser: () -> Unit = { onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.OpenInBrowser) }
            NotFoundStateView(
                title = stringResource(R.string.detail_notfound_title),
                body = stringResource(R.string.detail_notfound_body),
                primaryAction = stringResource(R.string.detail_go_home) to goHome,
                secondaryAction = stringResource(R.string.common_open_in_browser) to openBrowser
            )
            if (state.related.isNotEmpty()) {
                Column(modifier = Modifier.padding(horizontal = Spacing.md)) {
                    Text(
                        text = stringResource(R.string.detail_suggestions),
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.ink
                    )
                    Spacer(Modifier.size(Spacing.sm))
                    state.related.take(2).forEach { suggestion ->
                        RecipeListCard(
                            recipe = suggestion,
                            onClick = {
                                onEventDispatcher(RecipeDetailContract.RecipeDetailEvent.OpenRecipe(suggestion.id))
                            },
                            onBookmarkClick = { },
                            modifier = Modifier.padding(bottom = Spacing.sm)
                        )
                    }
                }
            }
        }
    }

    @ThemePreview
    @Composable
    private fun RecipeDetailPreview() {
        OshxonaTheme {
            RecipeDetailContent(
                state = RecipeDetailContract.RecipeDetailUiState(
                    recipe = SampleData.recipeDetail,
                    related = SampleData.recipes.filter { it.categoryKey == "gosht" }
                ),
                onEventDispatcher = { }
            )
        }
    }
}
