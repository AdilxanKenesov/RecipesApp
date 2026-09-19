package uz.gita.recipesapp.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.data.source.local.room.entity.ShoppingItemEntity

@Dao
interface ShoppingItemDao {

    @Query("SELECT * FROM shopping_items ORDER BY created_at DESC, id ASC")
    fun getItems(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<ShoppingItemEntity>)

    @Query("UPDATE shopping_items SET is_checked = NOT is_checked WHERE id = :itemId")
    suspend fun toggleChecked(itemId: Int)

    @Query("DELETE FROM shopping_items")
    suspend fun clear()
}
