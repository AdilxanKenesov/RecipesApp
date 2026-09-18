package uz.gita.recipesapp.domain.module

data class RecipeUiData(
    val id: Int,
    val slug: String,
    val title: String,
    val categoryKey: String,
    val categoryName: String,
    val imageUrl: String,
    val hasVideo: Boolean,
    val isFavorite: Boolean = false
)
