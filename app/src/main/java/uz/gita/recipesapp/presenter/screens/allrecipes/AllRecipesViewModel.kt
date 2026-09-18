package uz.gita.recipesapp.presenter.screens.allrecipes

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.presenter.ui.components.PagingFooterState
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import javax.inject.Inject

@HiltViewModel
class AllRecipesViewModel @Inject constructor(
    private val direction: AllRecipesContract.Direction
) : ViewModel(), AllRecipesContract.AllRecipesViewModel {

    override fun onEventDispatcher(event: AllRecipesContract.AllRecipesEvent) {
        when (event) {
            is AllRecipesContract.AllRecipesEvent.OpenRecipe -> direction.openRecipe(event.recipeId)

            is AllRecipesContract.AllRecipesEvent.ToggleFavorite -> intent {
                reduce {
                    state.copy(
                        recipes = state.recipes.map {
                            if (it.id == event.recipeId) it.copy(isFavorite = !it.isFavorite) else it
                        }
                    )
                }
            }

            AllRecipesContract.AllRecipesEvent.LoadMore -> intent {
                if (state.footerState != PagingFooterState.Idle) return@intent
                reduce { state.copy(footerState = PagingFooterState.Loading) }
                delay(600)
                reduce { state.copy(footerState = PagingFooterState.End) }
            }

            AllRecipesContract.AllRecipesEvent.Retry -> intent {
                reduce { state.copy(hasError = false, footerState = PagingFooterState.Idle) }
            }

            AllRecipesContract.AllRecipesEvent.Back -> direction.back()
        }
    }

    override val container = orbitContainer<AllRecipesContract.AllRecipesUiState, AllRecipesContract.SideEffect>(
        AllRecipesContract.AllRecipesUiState(
            recipes = SampleData.recipes,
            totalCount = SampleData.totalRecipeCount
        )
    )
}
