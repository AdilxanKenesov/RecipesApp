package uz.gita.recipesapp.presenter.screens.categories

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.CategoryUiData

interface CategoriesContract {
    interface CategoriesViewModel : OrbitContainerHost<CategoriesUiState, CategoriesUiState, SideEffect> {
        fun onEventDispatcher(event: CategoriesEvent)
    }

    sealed interface CategoriesEvent {
        data class OpenCategory(val category: CategoryUiData) : CategoriesEvent
        data object Retry : CategoriesEvent
    }

    data class CategoriesUiState(
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
        val categories: List<CategoryUiData> = emptyList()
    ) {
        val available: List<CategoryUiData> get() = categories.filter { it.count > 0 }
        val empty: List<CategoryUiData> get() = categories.filter { it.count == 0 }
    }

    sealed interface SideEffect

    interface Direction {
        fun openCategoryRecipes(categoryKey: String)
    }
}
