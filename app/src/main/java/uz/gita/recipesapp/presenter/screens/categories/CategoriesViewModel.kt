package uz.gita.recipesapp.presenter.screens.categories

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val direction: CategoriesContract.Direction
) : ViewModel(), CategoriesContract.CategoriesViewModel {

    override fun onEventDispatcher(event: CategoriesContract.CategoriesEvent) {
        when (event) {
            is CategoriesContract.CategoriesEvent.OpenCategory ->
                direction.openCategoryRecipes(event.category.key)

            CategoriesContract.CategoriesEvent.Retry -> intent {
                reduce { state.copy(hasError = false, isLoading = false) }
            }
        }
    }

    override val container = orbitContainer<CategoriesContract.CategoriesUiState, CategoriesContract.SideEffect>(
        CategoriesContract.CategoriesUiState(categories = SampleData.categories)
    )
}
