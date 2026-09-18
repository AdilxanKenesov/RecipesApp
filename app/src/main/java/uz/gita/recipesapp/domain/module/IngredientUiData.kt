package uz.gita.recipesapp.domain.module

sealed interface IngredientUiData {

    data class Heading(val text: String) : IngredientUiData

    data class Item(
        val id: Int,
        val amount: String,
        val name: String,
        val isChecked: Boolean = false
    ) : IngredientUiData
}
