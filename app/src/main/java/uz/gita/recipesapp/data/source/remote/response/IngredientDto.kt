package uz.gita.recipesapp.data.source.remote.response

data class IngredientDto(
    val type: String? = null,
    val amount: String? = null,
    val name: String? = null
) {
    val isHeading: Boolean get() = type == TYPE_HEADING

    companion object {
        const val TYPE_HEADING = "heading"
    }
}
