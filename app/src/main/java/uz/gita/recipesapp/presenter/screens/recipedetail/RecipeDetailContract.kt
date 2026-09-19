package uz.gita.recipesapp.presenter.screens.recipedetail

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

interface RecipeDetailContract {
    interface RecipeDetailViewModel :
        OrbitContainerHost<RecipeDetailUiState, RecipeDetailUiState, SideEffect> {
        fun onEventDispatcher(event: RecipeDetailEvent)
    }

    sealed interface RecipeDetailEvent {
        data class Load(val recipeId: Int) : RecipeDetailEvent
        data class ToggleIngredient(val ingredientId: Int) : RecipeDetailEvent
        data class OpenRecipe(val recipeId: Int) : RecipeDetailEvent
        data class ToggleShoppingItem(val ingredientId: Int) : RecipeDetailEvent
        data object ToggleFavorite : RecipeDetailEvent
        data class ToggleRelatedFavorite(val recipe: RecipeUiData) : RecipeDetailEvent
        data object StartCooking : RecipeDetailEvent
        data object Share : RecipeDetailEvent
        data object PlayVideo : RecipeDetailEvent
        data object VideoFailed : RecipeDetailEvent
        data object CloseVideo : RecipeDetailEvent
        data object OpenShoppingSheet : RecipeDetailEvent
        data object CloseShoppingSheet : RecipeDetailEvent
        data object ToggleAllShopping : RecipeDetailEvent
        data object ConfirmShopping : RecipeDetailEvent
        data object OpenHome : RecipeDetailEvent
        data object Retry : RecipeDetailEvent
        data object Back : RecipeDetailEvent
    }

    data class RecipeDetailUiState(
        val hasError: Boolean = false,
        val notFound: Boolean = false,
        val recipe: RecipeDetailUiData? = null,
        val related: List<RecipeUiData> = emptyList(),
        val shoppingSelection: Set<Int>? = null,
        val videoId: String? = null
    )

    sealed interface SideEffect {
        data class ShareUrl(val url: String) : SideEffect
    }

    interface Direction {
        fun back()
        fun openRecipe(recipeId: Int)
        fun openHome()
        fun openCooking(recipeId: Int)
    }
}
