package uz.gita.recipesapp.data.source.remote.response

data class SearchResultDto(
    val items: List<RecipeShortDto> = emptyList(),
    val total: Int = 0,
    val query: String = ""
)
