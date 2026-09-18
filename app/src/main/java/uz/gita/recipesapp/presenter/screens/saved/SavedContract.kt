package uz.gita.recipesapp.presenter.screens.saved

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.module.ShoppingItemUiData

interface SavedContract {
    interface SavedViewModel : OrbitContainerHost<SavedUiState, SavedUiState, SideEffect> {
        fun onEventDispatcher(event: SavedEvent)
    }

    enum class SavedSection {
        FAVORITES,
        SHOPPING
    }

    sealed interface SavedEvent {
        data class SectionChanged(val section: SavedSection) : SavedEvent
        data class OpenRecipe(val recipeId: Int) : SavedEvent
        data class ToggleFavorite(val recipeId: Int) : SavedEvent
        data class ToggleShoppingItem(val itemId: Int) : SavedEvent
        data object ClearShopping : SavedEvent
        data object OpenCategories : SavedEvent
        data object OpenAllRecipes : SavedEvent
    }

    data class SavedUiState(
        val section: SavedSection = SavedSection.FAVORITES,
        val favorites: List<RecipeUiData> = emptyList(),
        val shopping: List<ShoppingItemUiData> = emptyList()
    )

    sealed interface SideEffect

    interface Direction {
        fun openRecipe(recipeId: Int)
        fun openCategories()
        fun openAllRecipes()
    }
}
