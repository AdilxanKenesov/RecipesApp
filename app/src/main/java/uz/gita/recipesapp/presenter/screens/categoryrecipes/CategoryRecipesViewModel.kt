package uz.gita.recipesapp.presenter.screens.categoryrecipes

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.presenter.ui.components.PagingFooterState
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

            CategoryRecipesContract.CategoryRecipesEvent.LoadMore -> intent {
                if (state.footerState != PagingFooterState.Idle) return@intent
                reduce { state.copy(footerState = PagingFooterState.Loading) }
                delay(600)
                reduce { state.copy(footerState = PagingFooterState.End) }
            }

            CategoryRecipesContract.CategoryRecipesEvent.Retry -> intent {
                reduce { state.copy(hasError = false, footerState = PagingFooterState.Idle) }
            }

            CategoryRecipesContract.CategoryRecipesEvent.Back -> direction.back()
        }
    }

    override val container =
        orbitContainer<CategoryRecipesContract.CategoryRecipesUiState, CategoryRecipesContract.SideEffect>(
            CategoryRecipesContract.CategoryRecipesUiState()
        )
}
