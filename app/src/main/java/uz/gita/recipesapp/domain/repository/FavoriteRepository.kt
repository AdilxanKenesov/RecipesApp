package uz.gita.recipesapp.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

interface FavoriteRepository {

    fun getFavorites(language: AppLanguage): Flow<List<RecipeUiData>>

    fun getFavoriteIds(): Flow<Set<Int>>

    suspend fun toggleFavorite(recipe: RecipeUiData)

    suspend fun toggleFavorite(recipe: RecipeDetailUiData)

    suspend fun removeFavorite(recipeId: Int)
}
