package uz.gita.recipesapp.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.data.source.local.room.entity.FavoriteRecipeEntity

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_recipes ORDER BY saved_at DESC")
    fun getFavorites(): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT id FROM favorite_recipes")
    fun getFavoriteIds(): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE id = :recipeId)")
    suspend fun exists(recipeId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteRecipeEntity)

    @Query("DELETE FROM favorite_recipes WHERE id = :recipeId")
    suspend fun delete(recipeId: Int)
}
