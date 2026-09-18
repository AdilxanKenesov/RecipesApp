package uz.gita.recipesapp.presenter.screens.allrecipes

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.presenter.ui.components.PagingFooterState

interface AllRecipesContract {
    interface AllRecipesViewModel : OrbitContainerHost<AllRecipesUiState, AllRecipesUiState, SideEffect> {
        fun onEventDispatcher(event: AllRecipesEvent)
    }

    sealed interface AllRecipesEvent {
        data class OpenRecipe(val recipeId: Int) : AllRecipesEvent
        data class ToggleFavorite(val recipeId: Int) : AllRecipesEvent
        data object LoadMore : AllRecipesEvent
        data object Retry : AllRecipesEvent
        data object Back : AllRecipesEvent
    }

    data class AllRecipesUiState(
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
        val recipes: List<RecipeUiData> = emptyList(),
        val totalCount: Int = 0,
        val footerState: PagingFooterState = PagingFooterState.Idle
    )

    sealed interface SideEffect

    interface Direction {
        fun openRecipe(recipeId: Int)
        fun back()
    }
}
