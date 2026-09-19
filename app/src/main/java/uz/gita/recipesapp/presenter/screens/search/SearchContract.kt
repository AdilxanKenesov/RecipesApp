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
        data class ToggleFavorite(val recipe: RecipeUiData) : SearchEvent
        data object AddIngredient : SearchEvent
        data object FindByIngredients : SearchEvent
        data object ClearRecent : SearchEvent
        data object Retry : SearchEvent
    }

    data class SearchUiState(
        val mode: SearchMode = SearchMode.BY_NAME,
        val query: String = "",
        val nameResults: List<RecipeUiData>? = null,
        val isNameLoading: Boolean = false,
        val ingredientInput: String = "",
        val ingredients: List<String> = emptyList(),
        val ingredientResults: List<RecipeUiData>? = null,
        val isIngredientLoading: Boolean = false,
        val searchedIngredients: List<String>? = null,
        val hasError: Boolean = false,
        val recent: List<String> = emptyList(),
        val categories: List<CategoryUiData> = emptyList()
    ) {
        val nameLimitReached: Boolean get() = (nameResults?.size ?: 0) >= 40
        val ingredientLimitReached: Boolean get() = (ingredientResults?.size ?: 0) >= 40
        val showNameNotFound: Boolean get() = nameResults != null && nameResults.isEmpty()
        val showIngredientNotFound: Boolean get() = ingredientResults != null && ingredientResults.isEmpty()
        val findCount: Int
            get() = ingredients.size +
                if (ingredientInput.isNotBlank() && ingredientInput.trim() !in ingredients) 1 else 0
        val showFindButton: Boolean
            get() = findCount > 0 &&
                (ingredientInput.isNotBlank() || searchedIngredients != ingredients)
    }

    sealed interface SideEffect

    interface Direction {
        fun openRecipe(recipeId: Int)
        fun openCategoryRecipes(categoryKey: String)
    }
}
