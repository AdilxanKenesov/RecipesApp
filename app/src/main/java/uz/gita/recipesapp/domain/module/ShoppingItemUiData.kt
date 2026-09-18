package uz.gita.recipesapp.domain.module

data class ShoppingItemUiData(
    val id: Int,
    val recipeId: Int,
    val recipeTitle: String,
    val amount: String,
    val name: String,
    val isChecked: Boolean = false
)
