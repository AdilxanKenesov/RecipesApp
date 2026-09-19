package uz.gita.recipesapp.domain.usecase.recipedetail

import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

interface RecipeDetailUseCase {

    suspend fun getRecipe(recipeId: Int): Result<RecipeDetailUiData>

    suspend fun getRelatedRecipes(recipe: RecipeDetailUiData): Result<List<RecipeUiData>>

    fun getFavoriteIds(): Flow<Set<Int>>

    suspend fun toggleFavorite(recipe: RecipeDetailUiData)

    suspend fun toggleFavorite(recipe: RecipeUiData)

    suspend fun addToShoppingList(recipe: RecipeDetailUiData, ingredientIds: Set<Int>)
}
