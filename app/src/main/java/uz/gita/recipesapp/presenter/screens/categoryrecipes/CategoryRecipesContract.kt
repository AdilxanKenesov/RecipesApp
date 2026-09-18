package uz.gita.recipesapp.presenter.screens.categoryrecipes

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.presenter.ui.components.PagingFooterState

interface CategoryRecipesContract {
    interface CategoryRecipesViewModel :
        OrbitContainerHost<CategoryRecipesUiState, CategoryRecipesUiState, SideEffect> {
        fun onEventDispatcher(event: CategoryRecipesEvent)
    }

    sealed interface CategoryRecipesEvent {
        data class Load(val categoryKey: String) : CategoryRecipesEvent
        data class OpenRecipe(val recipeId: Int) : CategoryRecipesEvent
        data class ToggleFavorite(val recipeId: Int) : CategoryRecipesEvent
        data object LoadMore : CategoryRecipesEvent
        data object Retry : CategoryRecipesEvent
        data object Back : CategoryRecipesEvent
    }

    data class CategoryRecipesUiState(
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
        val category: CategoryUiData? = null,
        val recipes: List<RecipeUiData> = emptyList(),
        val footerState: PagingFooterState = PagingFooterState.Idle
    )

    sealed interface SideEffect

    interface Direction {
        fun openRecipe(recipeId: Int)
        fun back()
    }
}
