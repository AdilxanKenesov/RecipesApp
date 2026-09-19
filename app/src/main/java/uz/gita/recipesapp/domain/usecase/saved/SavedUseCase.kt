package uz.gita.recipesapp.domain.usecase.saved

import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.module.ShoppingItemUiData

interface SavedUseCase {

    fun getFavorites(): Flow<List<RecipeUiData>>

    fun getShoppingItems(): Flow<List<ShoppingItemUiData>>

    suspend fun removeFavorite(recipeId: Int)

    suspend fun toggleShoppingItem(itemId: Int)

    suspend fun clearShoppingList()
}
