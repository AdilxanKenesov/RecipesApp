package uz.gita.recipesapp.presenter.screens.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val direction: HomeContract.Direction
) : ViewModel(), HomeContract.HomeViewModel {

    override fun onEventDispatcher(event: HomeContract.HomeEvent) {
        when (event) {
            HomeContract.HomeEvent.OpenSettings -> direction.openSettings()

            HomeContract.HomeEvent.OpenAllCategories -> direction.openCategories()

            HomeContract.HomeEvent.OpenAllRecipes -> direction.openAllRecipes()

            HomeContract.HomeEvent.OpenIngredientSearch -> direction.openIngredientSearch()

            HomeContract.HomeEvent.ShuffleHero -> intent {
                val next = SampleData.recipes.filter { it.id != state.hero?.id }.randomOrNull()
                reduce { state.copy(hero = next ?: state.hero) }
            }

            HomeContract.HomeEvent.Retry -> intent {
                reduce { state.copy(hasError = false, isOffline = false, isLoading = false) }
            }

            is HomeContract.HomeEvent.OpenRecipe -> direction.openRecipe(event.recipeId)

            is HomeContract.HomeEvent.OpenCategory -> direction.openCategoryRecipes(event.category.key)

            is HomeContract.HomeEvent.ToggleFavorite -> intent {
                reduce {
                    state.copy(
                        hero = state.hero?.let {
                            if (it.id == event.recipeId) it.copy(isFavorite = !it.isFavorite) else it
                        },
                        recipes = state.recipes.map {
                            if (it.id == event.recipeId) it.copy(isFavorite = !it.isFavorite) else it
                        }
                    )
                }
            }
        }
    }

    override val container = orbitContainer<HomeContract.HomeUiState, HomeContract.SideEffect>(
        HomeContract.HomeUiState(
            hero = SampleData.recipes.first(),
            categories = SampleData.categories,
            recipes = SampleData.recipes.take(3),
            totalCount = SampleData.totalRecipeCount
        )
    )
}
