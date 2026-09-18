package uz.gita.recipesapp.presenter.screens.home

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

interface HomeContract {
    interface HomeViewModel : OrbitContainerHost<HomeUiState, HomeUiState, SideEffect> {
        fun onEventDispatcher(event: HomeEvent)
    }

    sealed interface HomeEvent {
        data object OpenSettings : HomeEvent
        data object NextHero : HomeEvent
        data object OpenAllCategories : HomeEvent
        data object OpenAllRecipes : HomeEvent
        data object OpenIngredientSearch : HomeEvent
        data object Retry : HomeEvent
        data class OpenRecipe(val recipeId: Int) : HomeEvent
        data class ToggleFavorite(val recipeId: Int) : HomeEvent
        data class OpenCategory(val category: CategoryUiData) : HomeEvent
    }

    data class HomeUiState(
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
        val isOffline: Boolean = false,
        val hero: RecipeUiData? = null,
        val categories: List<CategoryUiData> = emptyList(),
        val recipes: List<RecipeUiData> = emptyList(),
        val totalCount: Int = 0
    )

    sealed interface SideEffect

    interface Direction {
        fun openSettings()
        fun openRecipe(recipeId: Int)
        fun openCategoryRecipes(categoryKey: String)
        fun openCategories()
        fun openIngredientSearch()
        fun openAllRecipes()
    }
}
