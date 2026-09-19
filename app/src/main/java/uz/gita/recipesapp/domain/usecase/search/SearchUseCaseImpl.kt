package uz.gita.recipesapp.domain.usecase.search

import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.repository.CategoryRepository
import uz.gita.recipesapp.domain.repository.FavoriteRepository
import uz.gita.recipesapp.domain.repository.SearchRepository
import uz.gita.recipesapp.domain.repository.SettingsRepository
import javax.inject.Inject

class SearchUseCaseImpl @Inject constructor(
    private val searchRepository: SearchRepository,
    private val categoryRepository: CategoryRepository,
    private val favoriteRepository: FavoriteRepository,
    private val settingsRepository: SettingsRepository
) : SearchUseCase {

    override suspend fun searchByName(query: String): Result<List<RecipeUiData>> =
        searchRepository.searchByName(
            query = query,
            language = settingsRepository.getLanguage().value
        ).onSuccess { searchRepository.addRecentSearch(query) }

    override suspend fun searchByIngredients(ingredients: List<String>): Result<List<RecipeUiData>> =
        searchRepository.searchByIngredients(
            ingredients = ingredients,
            language = settingsRepository.getLanguage().value
        )

    override fun getRecentSearches(): Flow<List<String>> = searchRepository.getRecentSearches()

    override fun clearRecentSearches() {
        searchRepository.clearRecentSearches()
    }

    override suspend fun getCategories(): Result<List<CategoryUiData>> =
        categoryRepository.getCategories(language = settingsRepository.getLanguage().value)
            .map { categories -> categories.filter { it.count > 0 } }

    override fun getFavoriteIds(): Flow<Set<Int>> = favoriteRepository.getFavoriteIds()

    override suspend fun toggleFavorite(recipe: RecipeUiData) {
        favoriteRepository.toggleFavorite(recipe)
    }
}
