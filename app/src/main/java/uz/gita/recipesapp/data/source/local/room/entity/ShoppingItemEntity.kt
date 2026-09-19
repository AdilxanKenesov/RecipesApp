package uz.gita.recipesapp.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping_items",
    indices = [Index(value = ["recipe_id", "name"], unique = true)]
)
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: Int,
    @ColumnInfo(name = "recipe_title") val recipeTitle: String,
    @ColumnInfo(name = "amount") val amount: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_checked") val isChecked: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long
)
