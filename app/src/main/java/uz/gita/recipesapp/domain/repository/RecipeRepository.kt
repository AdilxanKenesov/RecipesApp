package uz.gita.recipesapp.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

interface RecipeRepository {

    fun getRecipesPaging(language: AppLanguage): Flow<PagingData<RecipeUiData>>

    fun getCategoryRecipesPaging(categoryKey: String, language: AppLanguage): Flow<PagingData<RecipeUiData>>

    suspend fun getRecipes(language: AppLanguage, limit: Int): Result<List<RecipeUiData>>

    suspend fun getCategoryRecipes(
        categoryKey: String,
        language: AppLanguage,
        limit: Int
    ): Result<List<RecipeUiData>>

    suspend fun getRandomRecipe(language: AppLanguage, categoryKey: String? = null): Result<RecipeUiData>

    suspend fun getRecipeDetail(recipeId: Int, language: AppLanguage): Result<RecipeDetailUiData>
}
