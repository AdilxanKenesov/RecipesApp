package uz.gita.recipesapp.domain.module

data class ViewedRecipeUiData(
    val recipe: RecipeUiData,
    val group: ViewedGroup
)

enum class ViewedGroup {
    TODAY,
    YESTERDAY,
    EARLIER
}
