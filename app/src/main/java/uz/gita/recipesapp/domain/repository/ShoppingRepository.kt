package uz.gita.recipesapp.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.ShoppingItemUiData

interface ShoppingRepository {

    fun getItems(): Flow<List<ShoppingItemUiData>>

    suspend fun addItems(recipe: RecipeDetailUiData, ingredientIds: Set<Int>)

    suspend fun toggleItem(itemId: Int)

    suspend fun clear()
}
