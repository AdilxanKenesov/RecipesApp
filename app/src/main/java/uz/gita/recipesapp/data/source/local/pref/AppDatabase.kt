package uz.gita.recipesapp.data.source.local.pref

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.recipesapp.data.source.local.room.dao.FavoriteDao
import uz.gita.recipesapp.data.source.local.room.dao.ShoppingItemDao
import uz.gita.recipesapp.data.source.local.room.entity.FavoriteRecipeEntity
import uz.gita.recipesapp.data.source.local.room.entity.ShoppingItemEntity

@Database(
    entities = [FavoriteRecipeEntity::class, ShoppingItemEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object {
        const val NAME = "pazanda.db"
    }
}