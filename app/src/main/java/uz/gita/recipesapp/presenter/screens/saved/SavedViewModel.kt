package uz.gita.recipesapp.presenter.screens.saved

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val direction: SavedContract.Direction
) : ViewModel(), SavedContract.SavedViewModel {

    override fun onEventDispatcher(event: SavedContract.SavedEvent) {
        when (event) {
            is SavedContract.SavedEvent.SectionChanged -> intent {
                reduce { state.copy(section = event.section) }
            }

            is SavedContract.SavedEvent.OpenRecipe -> direction.openRecipe(event.recipeId)

            is SavedContract.SavedEvent.ToggleFavorite -> intent {
                reduce { state.copy(favorites = state.favorites.filterNot { it.id == event.recipeId }) }
            }

            is SavedContract.SavedEvent.ToggleShoppingItem -> intent {
                reduce {
                    state.copy(
                        shopping = state.shopping.map {
                            if (it.id == event.itemId) it.copy(isChecked = !it.isChecked) else it
                        }
                    )
                }
            }

            SavedContract.SavedEvent.ClearShopping -> intent {
                reduce { state.copy(shopping = emptyList()) }
            }

            SavedContract.SavedEvent.OpenCategories -> direction.openCategories()

            SavedContract.SavedEvent.OpenAllRecipes -> direction.openAllRecipes()
        }
    }

    override val container = orbitContainer<SavedContract.SavedUiState, SavedContract.SideEffect>(
        SavedContract.SavedUiState(
            favorites = SampleData.favorites,
            shopping = SampleData.shoppingItems
        )
    )
}
