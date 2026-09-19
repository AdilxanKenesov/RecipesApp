package uz.gita.recipesapp.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "category_key") val categoryKey: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "has_video") val hasVideo: Boolean,
    @ColumnInfo(name = "saved_at") val savedAt: Long
)
