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
                        reduce { state.copy(mode = SearchContract.SearchMode.BY_INGREDIENT) }
                    }
                    tabSwitcher.consumeIngredientSearch()
                }
            }
        }
    }

    override fun onEventDispatcher(event: SearchContract.SearchEvent) {
        when (event) {
            is SearchContract.SearchEvent.ModeChanged -> intent {
                if (state.mode != event.mode) reduce { state.copy(mode = event.mode) }
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
                    if (state.ingredients.contains(event.name)) {
                        state.copy(ingredients = state.ingredients - event.name)
                    } else {
                        state.copy(ingredients = state.ingredients + event.name)
                    }
                }
            }

            is SearchContract.SearchEvent.OpenCategory -> direction.openCategoryRecipes(event.category.key)

            is SearchContract.SearchEvent.OpenRecipe -> direction.openRecipe(event.recipeId)

            is SearchContract.SearchEvent.ToggleFavorite -> intent {
                val toggle: (RecipeUiData) -> RecipeUiData = {
                    if (it.id == event.recipeId) it.copy(isFavorite = !it.isFavorite) else it
                }
                reduce {
                    state.copy(
                        nameResults = state.nameResults?.map(toggle),
                        ingredientResults = state.ingredientResults?.map(toggle)
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
                val pending = state.ingredientInput.trim()
                val ingredients = if (pending.isNotEmpty() && pending !in state.ingredients) {
                    state.ingredients + pending
                } else {
                    state.ingredients
                }
                if (ingredients.isEmpty()) return@intent
                reduce { state.copy(ingredients = ingredients, ingredientInput = "", isIngredientLoading = true) }
                delay(400)
                val found = searchByIngredients(ingredients)
                reduce {
                    state.copy(
                        isIngredientLoading = false,
                        ingredientResults = found,
                        searchedIngredients = ingredients
                    )
                }
            }

            SearchContract.SearchEvent.ClearRecent -> intent {
                reduce { state.copy(recent = emptyList()) }
            }

            SearchContract.SearchEvent.Retry -> intent {
                reduce { state.copy(hasError = false, isNameLoading = false, isIngredientLoading = false) }
            }
        }
    }

    private fun scheduleSearch(query: String, immediate: Boolean = false) {
        searchJob?.cancel()
        val normalized = query.normalizeQuery()
        if (normalized.length < 2) {
            intent { reduce { state.copy(nameResults = null, isNameLoading = false) } }
            return
        }
        searchJob = intent {
            reduce { state.copy(isNameLoading = true) }
            if (!immediate) delay(400)
            val found = searchByName(normalized)
            reduce {
                state.copy(
                    isNameLoading = false,
                    nameResults = found,
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
