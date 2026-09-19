package uz.gita.recipesapp.domain.usecase.home

import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

interface HomeUseCase {

    suspend fun getRandomRecipe(): Result<RecipeUiData>

    suspend fun getCategories(): Result<List<CategoryUiData>>

    suspend fun getLatestRecipes(): Result<List<RecipeUiData>>

    fun getFavoriteIds(): Flow<Set<Int>>

    suspend fun toggleFavorite(recipe: RecipeUiData)
}
