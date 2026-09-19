package uz.gita.recipesapp.presenter.screens.categoryrecipes

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

interface CategoryRecipesContract {
    interface CategoryRecipesViewModel :
        OrbitContainerHost<CategoryRecipesUiState, CategoryRecipesUiState, SideEffect> {
        val recipes: Flow<PagingData<RecipeUiData>>

        fun onEventDispatcher(event: CategoryRecipesEvent)
    }

    sealed interface CategoryRecipesEvent {
        data class Load(val categoryKey: String) : CategoryRecipesEvent
        data class OpenRecipe(val recipeId: Int) : CategoryRecipesEvent
        data class ToggleFavorite(val recipe: RecipeUiData) : CategoryRecipesEvent
        data class LoadFailed(val error: Throwable) : CategoryRecipesEvent
        data object Retry : CategoryRecipesEvent
        data object Back : CategoryRecipesEvent
    }

    data class CategoryRecipesUiState(
        val categoryKey: String? = null,
        val category: CategoryUiData? = null
    )

    sealed interface SideEffect

    interface Direction {
        fun openRecipe(recipeId: Int)
        fun back()
    }
}
