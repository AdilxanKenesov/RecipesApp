package uz.gita.recipesapp.domain.usecase.categoryrecipes

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

interface CategoryRecipesUseCase {

    suspend fun getCategory(categoryKey: String): Result<CategoryUiData?>

    fun getRecipes(categoryKey: String): Flow<PagingData<RecipeUiData>>

    fun getFavoriteIds(): Flow<Set<Int>>

    suspend fun toggleFavorite(recipe: RecipeUiData)
}
