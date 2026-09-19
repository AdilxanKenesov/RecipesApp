package uz.gita.recipesapp.presenter.screens.categoryrecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.usecase.categoryrecipes.CategoryRecipesUseCase
import uz.gita.recipesapp.presenter.ui.state.AppMessenger
import uz.gita.recipesapp.presenter.ui.util.withFavorite
import javax.inject.Inject

@HiltViewModel
class CategoryRecipesViewModel @Inject constructor(
    private val direction: CategoryRecipesContract.Direction,
    private val categoryRecipesUseCase: CategoryRecipesUseCase,
    private val messenger: AppMessenger
) : ViewModel(), CategoryRecipesContract.CategoryRecipesViewModel {

    private val categoryKey = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val recipes: Flow<PagingData<RecipeUiData>> =
        categoryKey
            .filterNotNull()
            .flatMapLatest { key -> categoryRecipesUseCase.getRecipes(key) }
            .cachedIn(viewModelScope)
            .combine(categoryRecipesUseCase.getFavoriteIds()) { data, ids ->
                data.map { it.withFavorite(ids) }
            }

    override fun onEventDispatcher(event: CategoryRecipesContract.CategoryRecipesEvent) {
        when (event) {
            is CategoryRecipesContract.CategoryRecipesEvent.Load -> intent {
                if (state.categoryKey == event.categoryKey) return@intent
                categoryKey.value = event.categoryKey
                reduce { state.copy(categoryKey = event.categoryKey) }
                loadCategory(event.categoryKey)
            }

            is CategoryRecipesContract.CategoryRecipesEvent.OpenRecipe ->
                direction.openRecipe(event.recipeId)

            is CategoryRecipesContract.CategoryRecipesEvent.ToggleFavorite -> intent {
                categoryRecipesUseCase.toggleFavorite(event.recipe)
            }

            CategoryRecipesContract.CategoryRecipesEvent.Retry -> intent {
                val key = state.categoryKey ?: return@intent
                if (state.category == null) loadCategory(key)
            }

            is CategoryRecipesContract.CategoryRecipesEvent.LoadFailed -> messenger.showError(event.error)

            CategoryRecipesContract.CategoryRecipesEvent.Back -> direction.back()
        }
    }

    private fun loadCategory(key: String) = intent {
        categoryRecipesUseCase.getCategory(key).onSuccess { category ->
            reduce { state.copy(category = category) }
        }
    }

    override val container =
        orbitContainer<CategoryRecipesContract.CategoryRecipesUiState, CategoryRecipesContract.SideEffect>(
            CategoryRecipesContract.CategoryRecipesUiState()
        )
}
