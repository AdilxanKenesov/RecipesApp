package uz.gita.recipesapp.presenter.screens.search

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

interface SearchContract {
    interface SearchViewModel : OrbitContainerHost<SearchUiState, SearchUiState, SideEffect> {
        fun onEventDispatcher(event: SearchEvent)
    }

    enum class SearchMode {
        BY_NAME,
        BY_INGREDIENT
    }

    sealed interface SearchEvent {
        data class ModeChanged(val mode: SearchMode) : SearchEvent
        data class QueryChanged(val query: String) : SearchEvent
        data class RecentClicked(val query: String) : SearchEvent
        data class IngredientInputChanged(val value: String) : SearchEvent
        data class RemoveIngredient(val name: String) : SearchEvent
        data class QuickIngredientClicked(val name: String) : SearchEvent
        data class OpenCategory(val category: CategoryUiData) : SearchEvent
        data class OpenRecipe(val recipeId: Int) : SearchEvent
        data class ToggleFavorite(val recipeId: Int) : SearchEvent
        data object AddIngredient : SearchEvent
        data object FindByIngredients : SearchEvent
        data object ClearRecent : SearchEvent
        data object Retry : SearchEvent
    }

    data class SearchUiState(
        val mode: SearchMode = SearchMode.BY_NAME,
        val query: String = "",
        val ingredientInput: String = "",
        val ingredients: List<String> = emptyList(),
        val results: List<RecipeUiData>? = null,
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
        val recent: List<String> = emptyList(),
        val quickIngredients: List<String> = emptyList(),
        val categories: List<CategoryUiData> = emptyList()
    ) {
        val limitReached: Boolean get() = (results?.size ?: 0) >= 40
        val showNotFound: Boolean get() = results != null && results.isEmpty()
    }

    sealed interface SideEffect

    interface Direction {
        fun openRecipe(recipeId: Int)
        fun openCategoryRecipes(categoryKey: String)
    }
}
