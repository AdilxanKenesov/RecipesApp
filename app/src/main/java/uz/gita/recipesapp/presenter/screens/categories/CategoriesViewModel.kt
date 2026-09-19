package uz.gita.recipesapp.presenter.screens.categories

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.usecase.categories.CategoriesUseCase
import uz.gita.recipesapp.presenter.ui.state.AppMessenger
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val direction: CategoriesContract.Direction,
    private val categoriesUseCase: CategoriesUseCase,
    private val messenger: AppMessenger
) : ViewModel(), CategoriesContract.CategoriesViewModel {

    override fun onEventDispatcher(event: CategoriesContract.CategoriesEvent) {
        when (event) {
            is CategoriesContract.CategoriesEvent.OpenCategory ->
                direction.openCategoryRecipes(event.category.key)

            CategoriesContract.CategoriesEvent.Retry -> load()
        }
    }

    private fun load() = intent {
        reduce { state.copy(isLoading = state.categories.isEmpty(), hasError = false) }
        categoriesUseCase.getCategories()
            .onSuccess { categories -> reduce { state.copy(isLoading = false, categories = categories) } }
            .onFailure { error ->
                reduce { state.copy(isLoading = false, hasError = true) }
                messenger.showError(error)
            }
    }

    override val container = orbitContainer<CategoriesContract.CategoriesUiState, CategoriesContract.SideEffect>(
        CategoriesContract.CategoriesUiState()
    )

    init {
        load()
    }
}
