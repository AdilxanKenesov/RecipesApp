package uz.gita.recipesapp.presenter.screens.categoryrecipes

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import javax.inject.Inject

@HiltViewModel
class CategoryRecipesViewModel @Inject constructor(
    private val direction: CategoryRecipesContract.Direction
) : ViewModel(), CategoryRecipesContract.CategoryRecipesViewModel {

    override fun onEventDispatcher(event: CategoryRecipesContract.CategoryRecipesEvent) {
        when (event) {
            is CategoryRecipesContract.CategoryRecipesEvent.Load -> intent {
                val category = SampleData.categories.firstOrNull { it.key == event.categoryKey }
                reduce {
                    state.copy(
                        category = category,
                        recipes = SampleData.recipes.filter { it.categoryKey == event.categoryKey }
                    )
                }
            }

            is CategoryRecipesContract.CategoryRecipesEvent.OpenRecipe ->
                direction.openRecipe(event.recipeId)

            is CategoryRecipesContract.CategoryRecipesEvent.ToggleFavorite -> intent {
                reduce {
                    state.copy(
                        recipes = state.recipes.map {
                            if (it.id == event.recipeId) it.copy(isFavorite = !it.isFavorite) else it
                        }
                    )
                }
            }

            CategoryRecipesContract.CategoryRecipesEvent.Retry -> intent {
                reduce { state.copy(hasError = false) }
            }

            CategoryRecipesContract.CategoryRecipesEvent.Back -> direction.back()
        }
    }

    override val container =
        orbitContainer<CategoryRecipesContract.CategoryRecipesUiState, CategoryRecipesContract.SideEffect>(
            CategoryRecipesContract.CategoryRecipesUiState()
        )
}
