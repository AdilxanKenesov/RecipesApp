package uz.gita.recipesapp.presenter.screens.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.usecase.home.HomeUseCase
import uz.gita.recipesapp.presenter.ui.state.AppMessenger
import uz.gita.recipesapp.presenter.ui.util.withFavorite
import uz.gita.recipesapp.presenter.ui.util.withFavorites
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val direction: HomeContract.Direction,
    private val homeUseCase: HomeUseCase,
    private val messenger: AppMessenger
) : ViewModel(), HomeContract.HomeViewModel {

    private var favoriteIds: Set<Int> = emptySet()

    override fun onEventDispatcher(event: HomeContract.HomeEvent) {
        when (event) {
            HomeContract.HomeEvent.OpenSettings -> direction.openSettings()

            HomeContract.HomeEvent.OpenAllCategories -> direction.openCategories()

            HomeContract.HomeEvent.OpenAllRecipes -> direction.openAllRecipes()

            HomeContract.HomeEvent.OpenIngredientSearch -> direction.openIngredientSearch()

            HomeContract.HomeEvent.NextHero -> intent {
                homeUseCase.getRandomRecipe()
                    .onSuccess { recipe -> reduce { state.copy(hero = recipe.withFavorite(favoriteIds)) } }
                    .onFailure(messenger::showError)
            }

            HomeContract.HomeEvent.Retry -> load()

            is HomeContract.HomeEvent.OpenRecipe -> direction.openRecipe(event.recipeId)

            is HomeContract.HomeEvent.OpenCategory -> direction.openCategoryRecipes(event.category.key)

            is HomeContract.HomeEvent.ToggleFavorite -> intent {
                homeUseCase.toggleFavorite(event.recipe)
            }
        }
    }

    private fun load() = intent {
        reduce { state.copy(isLoading = true, hasError = false) }
        coroutineScope {
            val hero = async { homeUseCase.getRandomRecipe() }
            val categories = async { homeUseCase.getCategories() }
            val latest = async { homeUseCase.getLatestRecipes() }
            val latestResult = latest.await()
            val recipes = latestResult.getOrNull()
            latestResult.exceptionOrNull()?.let(messenger::showError)
            val heroRecipe = hero.await().getOrNull()
            val categoryList = categories.await().getOrDefault(emptyList())
            reduce {
                state.copy(
                    isLoading = false,
                    hasError = recipes == null,
                    hero = heroRecipe?.withFavorite(favoriteIds),
                    categories = categoryList,
                    recipes = recipes.orEmpty().withFavorites(favoriteIds)
                )
            }
        }
    }

    private fun observeFavorites() = intent {
        homeUseCase.getFavoriteIds().collect { ids ->
            favoriteIds = ids
            reduce {
                state.copy(
                    hero = state.hero?.withFavorite(ids),
                    recipes = state.recipes.withFavorites(ids)
                )
            }
        }
    }

    override val container = orbitContainer<HomeContract.HomeUiState, HomeContract.SideEffect>(
        HomeContract.HomeUiState(isLoading = true)
    )

    init {
        observeFavorites()
        load()
    }
}
