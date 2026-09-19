package uz.gita.recipesapp.presenter.screens.recipedetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.exception.NotFoundException
import uz.gita.recipesapp.domain.module.IngredientUiData
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.usecase.recipedetail.RecipeDetailUseCase
import uz.gita.recipesapp.presenter.ui.state.AppMessenger
import uz.gita.recipesapp.presenter.ui.util.withFavorites
import uz.gita.recipesapp.presenter.ui.util.youTubeVideoId
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val direction: RecipeDetailContract.Direction,
    private val recipeDetailUseCase: RecipeDetailUseCase,
    private val messenger: AppMessenger
) : ViewModel(), RecipeDetailContract.RecipeDetailViewModel {

    private var recipeId: Int? = null
    private var favoriteIds: Set<Int> = emptySet()

    override fun onEventDispatcher(event: RecipeDetailContract.RecipeDetailEvent) {
        when (event) {
            is RecipeDetailContract.RecipeDetailEvent.Load -> {
                if (recipeId == event.recipeId) return
                recipeId = event.recipeId
                load(event.recipeId)
            }

            RecipeDetailContract.RecipeDetailEvent.Retry -> recipeId?.let { load(it) }

            is RecipeDetailContract.RecipeDetailEvent.ToggleIngredient -> intent {
                reduce {
                    val current = state.recipe
                    state.copy(
                        recipe = current?.copy(
                            ingredients = current.ingredients.map { ingredient ->
                                if (ingredient is IngredientUiData.Item && ingredient.id == event.ingredientId) {
                                    ingredient.copy(isChecked = !ingredient.isChecked)
                                } else {
                                    ingredient
                                }
                            }
                        )
                    )
                }
            }

            is RecipeDetailContract.RecipeDetailEvent.OpenRecipe -> direction.openRecipe(event.recipeId)

            RecipeDetailContract.RecipeDetailEvent.StartCooking -> intent {
                state.recipe?.let { direction.openCooking(it.id) }
            }

            RecipeDetailContract.RecipeDetailEvent.OpenShoppingSheet -> intent {
                reduce { state.copy(shoppingSelection = missingIngredientIds(state.recipe)) }
            }

            RecipeDetailContract.RecipeDetailEvent.CloseShoppingSheet -> intent {
                reduce { state.copy(shoppingSelection = null) }
            }

            is RecipeDetailContract.RecipeDetailEvent.ToggleShoppingItem -> intent {
                reduce {
                    val selection = state.shoppingSelection ?: return@reduce state
                    state.copy(
                        shoppingSelection = if (event.ingredientId in selection) {
                            selection - event.ingredientId
                        } else {
                            selection + event.ingredientId
                        }
                    )
                }
            }

            RecipeDetailContract.RecipeDetailEvent.ToggleAllShopping -> intent {
                reduce {
                    val selection = state.shoppingSelection ?: return@reduce state
                    val all = ingredientIds(state.recipe)
                    state.copy(shoppingSelection = if (selection.size == all.size) emptySet() else all)
                }
            }

            RecipeDetailContract.RecipeDetailEvent.ConfirmShopping -> intent {
                val recipe = state.recipe ?: return@intent
                val selection = state.shoppingSelection
                if (selection.isNullOrEmpty()) return@intent
                recipeDetailUseCase.addToShoppingList(recipe, selection)
                reduce { state.copy(shoppingSelection = null) }
                messenger.show(R.string.detail_added_to_shopping)
            }

            RecipeDetailContract.RecipeDetailEvent.ToggleFavorite -> intent {
                state.recipe?.let { recipeDetailUseCase.toggleFavorite(it) }
            }

            is RecipeDetailContract.RecipeDetailEvent.ToggleRelatedFavorite -> intent {
                recipeDetailUseCase.toggleFavorite(event.recipe)
            }

            RecipeDetailContract.RecipeDetailEvent.Share -> intent {
                val url = state.recipe?.url.orEmpty()
                if (url.isNotBlank()) postSideEffect(RecipeDetailContract.SideEffect.ShareUrl(url))
            }

            RecipeDetailContract.RecipeDetailEvent.PlayVideo -> intent {
                val videoId = state.recipe?.videoUrl?.youTubeVideoId()
                if (videoId == null) {
                    messenger.show(R.string.video_unavailable)
                } else {
                    reduce { state.copy(videoId = videoId) }
                }
            }

            RecipeDetailContract.RecipeDetailEvent.CloseVideo -> intent {
                reduce { state.copy(videoId = null) }
            }

            RecipeDetailContract.RecipeDetailEvent.VideoFailed -> intent {
                reduce { state.copy(videoId = null) }
                messenger.show(R.string.video_unavailable)
            }

            RecipeDetailContract.RecipeDetailEvent.OpenHome -> direction.openHome()

            RecipeDetailContract.RecipeDetailEvent.Back -> direction.back()
        }
    }

    private fun load(recipeId: Int) = intent {
        reduce { state.copy(hasError = false, notFound = false) }
        recipeDetailUseCase.getRecipe(recipeId)
            .onSuccess { recipe ->
                reduce { state.copy(recipe = recipe.copy(isFavorite = recipe.id in favoriteIds)) }
                loadRelated(recipe)
            }
            .onFailure { error ->
                reduce {
                    state.copy(
                        notFound = error is NotFoundException,
                        hasError = error !is NotFoundException
                    )
                }
                if (error !is NotFoundException) messenger.showError(error)
            }
    }

    private fun loadRelated(recipe: RecipeDetailUiData) = intent {
        recipeDetailUseCase.getRelatedRecipes(recipe).onSuccess { related ->
            reduce { state.copy(related = related.withFavorites(favoriteIds)) }
        }
    }

    private fun observeFavorites() = intent {
        recipeDetailUseCase.getFavoriteIds().collect { ids ->
            favoriteIds = ids
            reduce {
                state.copy(
                    recipe = state.recipe?.let { it.copy(isFavorite = it.id in ids) },
                    related = state.related.withFavorites(ids)
                )
            }
        }
    }

    private fun ingredientIds(recipe: RecipeDetailUiData?): Set<Int> =
        recipe?.ingredients
            ?.filterIsInstance<IngredientUiData.Item>()
            ?.map { it.id }
            ?.toSet()
            ?: emptySet()

    private fun missingIngredientIds(recipe: RecipeDetailUiData?): Set<Int> =
        recipe?.ingredients
            ?.filterIsInstance<IngredientUiData.Item>()
            ?.filterNot { it.isChecked }
            ?.map { it.id }
            ?.toSet()
            ?: emptySet()

    override val container =
        orbitContainer<RecipeDetailContract.RecipeDetailUiState, RecipeDetailContract.SideEffect>(
            RecipeDetailContract.RecipeDetailUiState()
        )

    init {
        observeFavorites()
    }
}
