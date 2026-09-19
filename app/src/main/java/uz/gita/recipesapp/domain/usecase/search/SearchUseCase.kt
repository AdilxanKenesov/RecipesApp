package uz.gita.recipesapp.domain.usecase.search

import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

interface SearchUseCase {

    suspend fun searchByName(query: String): Result<List<RecipeUiData>>

    suspend fun searchByIngredients(ingredients: List<String>): Result<List<RecipeUiData>>

    fun getRecentSearches(): Flow<List<String>>

    fun clearRecentSearches()

    suspend fun getCategories(): Result<List<CategoryUiData>>

    fun getFavoriteIds(): Flow<Set<Int>>

    suspend fun toggleFavorite(recipe: RecipeUiData)
}
