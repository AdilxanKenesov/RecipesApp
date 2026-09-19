package uz.gita.recipesapp.data.repository_impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.gita.recipesapp.data.mapper.toShoppingEntities
import uz.gita.recipesapp.data.mapper.toUIData
import uz.gita.recipesapp.data.source.local.room.dao.ShoppingItemDao
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.ShoppingItemUiData
import uz.gita.recipesapp.domain.repository.ShoppingRepository
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val shoppingItemDao: ShoppingItemDao
) : ShoppingRepository {

    override fun getItems(): Flow<List<ShoppingItemUiData>> =
        shoppingItemDao.getItems().map { list -> list.map { it.toUIData() } }

    override suspend fun addItems(recipe: RecipeDetailUiData, ingredientIds: Set<Int>) {
        val items = recipe.toShoppingEntities(ingredientIds)
        if (items.isNotEmpty()) shoppingItemDao.insertAll(items)
    }

    override suspend fun toggleItem(itemId: Int) {
        shoppingItemDao.toggleChecked(itemId)
    }

    override suspend fun clear() {
        shoppingItemDao.clear()
    }
}
