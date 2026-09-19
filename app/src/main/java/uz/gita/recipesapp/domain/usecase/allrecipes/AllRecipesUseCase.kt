package uz.gita.recipesapp.domain.usecase.allrecipes

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.RecipeUiData

interface AllRecipesUseCase {

    fun getRecipes(): Flow<PagingData<RecipeUiData>>

    fun getFavoriteIds(): Flow<Set<Int>>

    suspend fun toggleFavorite(recipe: RecipeUiData)
}
