package uz.gita.recipesapp.presenter.screens.allrecipes

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.RecipeUiData

interface AllRecipesContract {
    interface AllRecipesViewModel : OrbitContainerHost<AllRecipesUiState, AllRecipesUiState, SideEffect> {
        val recipes: Flow<PagingData<RecipeUiData>>

        fun onEventDispatcher(event: AllRecipesEvent)
    }

    sealed interface AllRecipesEvent {
        data class OpenRecipe(val recipeId: Int) : AllRecipesEvent
        data class ToggleFavorite(val recipe: RecipeUiData) : AllRecipesEvent
        data class LoadFailed(val error: Throwable) : AllRecipesEvent
        data object Back : AllRecipesEvent
    }

    data object AllRecipesUiState

    sealed interface SideEffect

    interface Direction {
        fun openRecipe(recipeId: Int)
        fun back()
    }
}
