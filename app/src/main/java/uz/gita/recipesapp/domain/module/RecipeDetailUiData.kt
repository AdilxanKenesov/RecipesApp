package uz.gita.recipesapp.domain.module

data class RecipeDetailUiData(
    val id: Int,
    val title: String,
    val categoryKey: String,
    val categoryName: String,
    val imageUrl: String,
    val videoUrl: String?,
    val hasVideo: Boolean,
    val description: String,
    val author: String,
    val publishedDate: String,
    val url: String,
    val ingredients: List<IngredientUiData>,
    val steps: List<StepUiData>,
    val isFavorite: Boolean = false
) {
    val ingredientCount: Int get() = ingredients.count { it is IngredientUiData.Item }
}
