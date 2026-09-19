package uz.gita.recipesapp.data.mapper

import uz.gita.recipesapp.data.source.local.room.entity.FavoriteRecipeEntity
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData

fun FavoriteRecipeEntity.toUIData(lang: String): RecipeUiData =
    RecipeUiData(
        id = id,
        slug = slug,
        title = title,
        categoryKey = categoryKey,
        categoryName = categoryName(categoryKey, lang),
        imageUrl = imageUrl,
        hasVideo = hasVideo,
        isFavorite = true
    )

fun RecipeUiData.toFavoriteEntity(savedAt: Long = System.currentTimeMillis()): FavoriteRecipeEntity =
    FavoriteRecipeEntity(
        id = id,
        slug = slug,
        title = title,
        categoryKey = categoryKey,
        imageUrl = imageUrl,
        hasVideo = hasVideo,
        savedAt = savedAt
    )

fun RecipeDetailUiData.toFavoriteEntity(savedAt: Long = System.currentTimeMillis()): FavoriteRecipeEntity =
    FavoriteRecipeEntity(
        id = id,
        slug = "",
        title = title,
        categoryKey = categoryKey,
        imageUrl = imageUrl,
        hasVideo = hasVideo,
        savedAt = savedAt
    )
