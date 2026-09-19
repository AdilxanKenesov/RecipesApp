package uz.gita.recipesapp.presenter.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.usecase.search.SearchUseCase
import uz.gita.recipesapp.presenter.ui.state.AppMessenger
import uz.gita.recipesapp.presenter.ui.state.TabSwitcher
import uz.gita.recipesapp.presenter.ui.util.normalizeQuery
import uz.gita.recipesapp.presenter.ui.util.withFavorites
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val direction: SearchContract.Direction,
    private val tabSwitcher: TabSwitcher,
    private val searchUseCase: SearchUseCase,
    private val messenger: AppMessenger
) : ViewModel(), SearchContract.SearchViewModel {

    private var searchJob: Job? = null
    private var favoriteIds: Set<Int> = emptySet()

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
                searchUseCase.toggleFavorite(event.recipe)
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
                reduce { state.copy(ingredients = ingredients, ingredientInput = "") }
                findByIngredients(ingredients)
            }

            SearchContract.SearchEvent.ClearRecent -> intent {
                searchUseCase.clearRecentSearches()
            }

            SearchContract.SearchEvent.Retry -> intent {
                reduce { state.copy(hasError = false) }
                when (state.mode) {
                    SearchContract.SearchMode.BY_NAME -> scheduleSearch(state.query, immediate = true)
                    SearchContract.SearchMode.BY_INGREDIENT -> {
                        val ingredients = state.searchedIngredients ?: state.ingredients
                        if (ingredients.isNotEmpty()) findByIngredients(ingredients)
                    }
                }
            }
        }
    }

    private fun scheduleSearch(query: String, immediate: Boolean = false) {
        searchJob?.cancel()
        val normalized = query.normalizeQuery()
        if (normalized.length < MIN_QUERY_LENGTH) {
            intent { reduce { state.copy(nameResults = null, isNameLoading = false) } }
            return
        }
        searchJob = intent {
            reduce { state.copy(isNameLoading = true, hasError = false) }
            if (!immediate) delay(SEARCH_DEBOUNCE_MILLIS)
            searchUseCase.searchByName(normalized)
                .onSuccess { found ->
                    reduce { state.copy(isNameLoading = false, nameResults = found.withFavorites(favoriteIds)) }
                }
                .onFailure { error ->
                    reduce { state.copy(isNameLoading = false, hasError = true) }
                    messenger.showError(error)
                }
        }
    }

    private fun findByIngredients(ingredients: List<String>) = intent {
        reduce { state.copy(isIngredientLoading = true, hasError = false) }
        searchUseCase.searchByIngredients(ingredients)
            .onSuccess { found ->
                reduce {
                    state.copy(
                        isIngredientLoading = false,
                        ingredientResults = found.withFavorites(favoriteIds),
                        searchedIngredients = ingredients
                    )
                }
            }
            .onFailure { error ->
                reduce { state.copy(isIngredientLoading = false, hasError = true) }
                messenger.showError(error)
            }
    }

    private fun loadCategories() = intent {
        searchUseCase.getCategories().onSuccess { categories ->
            reduce { state.copy(categories = categories) }
        }
    }

    private fun observeRecentSearches() = intent {
        searchUseCase.getRecentSearches().collect { recent ->
            reduce { state.copy(recent = recent) }
        }
    }

    private fun observeFavorites() = intent {
        searchUseCase.getFavoriteIds().collect { ids ->
            favoriteIds = ids
            reduce {
                state.copy(
                    nameResults = state.nameResults?.withFavorites(ids),
                    ingredientResults = state.ingredientResults?.withFavorites(ids)
                )
            }
        }
    }

    private fun initialMode(): SearchContract.SearchMode {
        if (!tabSwitcher.pendingIngredientSearch.value) return SearchContract.SearchMode.BY_NAME
        tabSwitcher.consumeIngredientSearch()
        return SearchContract.SearchMode.BY_INGREDIENT
    }

    private fun observeTabSwitcher() {
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

    override val container = orbitContainer<SearchContract.SearchUiState, SearchContract.SideEffect>(
        SearchContract.SearchUiState(mode = initialMode())
    )

    init {
        observeTabSwitcher()
        observeRecentSearches()
        observeFavorites()
        loadCategories()
    }

    companion object {
        private const val MIN_QUERY_LENGTH = 2
        private const val SEARCH_DEBOUNCE_MILLIS = 400L
    }
}
