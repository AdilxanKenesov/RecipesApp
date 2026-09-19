package uz.gita.recipesapp.presenter.screens.allrecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.usecase.allrecipes.AllRecipesUseCase
import uz.gita.recipesapp.presenter.ui.state.AppMessenger
import uz.gita.recipesapp.presenter.ui.util.withFavorite
import javax.inject.Inject

@HiltViewModel
class AllRecipesViewModel @Inject constructor(
    private val direction: AllRecipesContract.Direction,
    private val allRecipesUseCase: AllRecipesUseCase,
    private val messenger: AppMessenger
) : ViewModel(), AllRecipesContract.AllRecipesViewModel {

    override val recipes: Flow<PagingData<RecipeUiData>> =
        allRecipesUseCase.getRecipes()
            .cachedIn(viewModelScope)
            .combine(allRecipesUseCase.getFavoriteIds()) { data, ids ->
                data.map { it.withFavorite(ids) }
            }

    override fun onEventDispatcher(event: AllRecipesContract.AllRecipesEvent) {
        when (event) {
            is AllRecipesContract.AllRecipesEvent.OpenRecipe -> direction.openRecipe(event.recipeId)

            is AllRecipesContract.AllRecipesEvent.ToggleFavorite -> intent {
                allRecipesUseCase.toggleFavorite(event.recipe)
            }

            is AllRecipesContract.AllRecipesEvent.LoadFailed -> messenger.showError(event.error)

            AllRecipesContract.AllRecipesEvent.Back -> direction.back()
        }
    }

    override val container = orbitContainer<AllRecipesContract.AllRecipesUiState, AllRecipesContract.SideEffect>(
        AllRecipesContract.AllRecipesUiState
    )
}
