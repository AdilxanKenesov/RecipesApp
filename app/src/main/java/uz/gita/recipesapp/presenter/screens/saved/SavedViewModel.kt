package uz.gita.recipesapp.presenter.screens.saved

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.usecase.saved.SavedUseCase
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val direction: SavedContract.Direction,
    private val savedUseCase: SavedUseCase
) : ViewModel(), SavedContract.SavedViewModel {

    override fun onEventDispatcher(event: SavedContract.SavedEvent) {
        when (event) {
            is SavedContract.SavedEvent.SectionChanged -> intent {
                reduce { state.copy(section = event.section) }
            }

            is SavedContract.SavedEvent.OpenRecipe -> direction.openRecipe(event.recipeId)

            is SavedContract.SavedEvent.ToggleFavorite -> intent {
                savedUseCase.removeFavorite(event.recipeId)
            }

            is SavedContract.SavedEvent.ToggleShoppingItem -> intent {
                savedUseCase.toggleShoppingItem(event.itemId)
            }

            SavedContract.SavedEvent.ClearShopping -> intent {
                savedUseCase.clearShoppingList()
            }

            SavedContract.SavedEvent.OpenCategories -> direction.openCategories()

            is SavedContract.SavedEvent.StartCooking -> direction.openCooking(event.recipeId)
        }
    }

    private fun observeFavorites() = intent {
        savedUseCase.getFavorites().collect { favorites ->
            reduce { state.copy(favorites = favorites) }
        }
    }

    private fun observeShoppingItems() = intent {
        savedUseCase.getShoppingItems().collect { items ->
            reduce { state.copy(shopping = items) }
        }
    }

    override val container = orbitContainer<SavedContract.SavedUiState, SavedContract.SideEffect>(
        SavedContract.SavedUiState()
    )

    init {
        observeFavorites()
        observeShoppingItems()
    }
}
