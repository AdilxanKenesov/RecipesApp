package uz.gita.recipesapp.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.RecipeUiData

interface SearchRepository {

    suspend fun searchByName(query: String, language: AppLanguage): Result<List<RecipeUiData>>

    suspend fun searchByIngredients(ingredients: List<String>, language: AppLanguage): Result<List<RecipeUiData>>

    fun getRecentSearches(): StateFlow<List<String>>

    fun addRecentSearch(query: String)

    fun clearRecentSearches()
}
