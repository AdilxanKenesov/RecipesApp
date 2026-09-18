package uz.gita.recipesapp.presenter.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import uz.gita.recipesapp.presenter.ui.state.TabSwitcher
import uz.gita.recipesapp.presenter.ui.util.normalizeQuery
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val direction: SearchContract.Direction,
    private val tabSwitcher: TabSwitcher
) : ViewModel(), SearchContract.SearchViewModel {

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            tabSwitcher.pendingIngredientSearch.collect { pending ->
                if (pending) {
                    intent {
                        reduce {
                            state.copy(mode = SearchContract.SearchMode.BY_INGREDIENT, results = null)
                        }
                    }
                    tabSwitcher.consumeIngredientSearch()
                }
            }
        }
    }

    override fun onEventDispatcher(event: SearchContract.SearchEvent) {
        when (event) {
            is SearchContract.SearchEvent.ModeChanged -> intent {
                reduce { state.copy(mode = event.mode, results = null, isLoading = false) }
            }

            is SearchContract.SearchEvent.QueryChanged -> {
                intent { reduce { state.copy(query = event.query) } }
                scheduleSearch(event.query)
            }

            is SearchContract.SearchEvent.RecentClicked -> {
                intent { reduce { state.copy(query = event.query) } }
                scheduleSearch(event.query, immediate = true)
            }

            is SearchContract.SearchEvent.IngredientInputChanged -> intent {
                reduce { state.copy(ingredientInput = event.value) }
            }

            is SearchContract.SearchEvent.RemoveIngredient -> intent {
                reduce { state.copy(ingredients = state.ingredients - event.name) }
            }

            is SearchContract.SearchEvent.QuickIngredientClicked -> intent {
                reduce {
                    if (state.ingredients.contains(event.name)) state
                    else state.copy(ingredients = state.ingredients + event.name)
                }
            }

            is SearchContract.SearchEvent.OpenCategory -> direction.openCategoryRecipes(event.category.key)

            is SearchContract.SearchEvent.OpenRecipe -> direction.openRecipe(event.recipeId)

            is SearchContract.SearchEvent.ToggleFavorite -> intent {
                reduce {
                    state.copy(
                        results = state.results?.map {
                            if (it.id == event.recipeId) it.copy(isFavorite = !it.isFavorite) else it
                        }
                    )
                }
            }

            SearchContract.SearchEvent.AddIngredient -> intent {
                val value = state.ingredientInput.trim()
                reduce {
                    if (value.isEmpty() || state.ingredients.contains(value)) {
                        state.copy(ingredientInput = "")
                    } else {
                        state.copy(ingredients = state.ingredients + value, ingredientInput = "")
                    }
                }
            }

            SearchContract.SearchEvent.FindByIngredients -> intent {
                reduce { state.copy(isLoading = true) }
                delay(400)
                val found = searchByIngredients(state.ingredients)
                reduce { state.copy(isLoading = false, results = found) }
            }

            SearchContract.SearchEvent.ClearRecent -> intent {
                reduce { state.copy(recent = emptyList()) }
            }

            SearchContract.SearchEvent.Retry -> intent {
                reduce { state.copy(hasError = false, isLoading = false) }
            }
        }
    }

    private fun scheduleSearch(query: String, immediate: Boolean = false) {
        searchJob?.cancel()
        val normalized = query.normalizeQuery()
        if (normalized.length < 2) {
            intent { reduce { state.copy(results = null, isLoading = false) } }
            return
        }
        searchJob = intent {
            reduce { state.copy(isLoading = true) }
            if (!immediate) delay(400)
            val found = searchByName(normalized)
            reduce {
                state.copy(
                    isLoading = false,
                    results = found,
                    recent = (listOf(query) + state.recent).distinct().take(6)
                )
            }
        }
    }

    private fun searchByName(query: String): List<RecipeUiData> =
        SampleData.recipes.filter { it.title.contains(query, ignoreCase = true) }

    private fun searchByIngredients(ingredients: List<String>): List<RecipeUiData> =
        if (ingredients.isEmpty()) emptyList()
        else SampleData.recipes.filterIndexed { index, _ -> index % 2 == 0 }

    override val container = orbitContainer<SearchContract.SearchUiState, SearchContract.SideEffect>(
        SearchContract.SearchUiState(
            recent = SampleData.recentSearches,
            quickIngredients = SampleData.quickIngredients,
            categories = SampleData.categories.filter { it.count > 0 }
        )
    )
}
