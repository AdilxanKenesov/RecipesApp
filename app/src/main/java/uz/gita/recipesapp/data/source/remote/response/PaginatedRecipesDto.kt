package uz.gita.recipesapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PaginatedRecipesDto(
    val items: List<RecipeShortDto> = emptyList(),
    val total: Int = 0,
    val page: Int = 0,
    @SerializedName("per_page") val perPage: Int = 0,
    @SerializedName("total_pages") val totalPages: Int = 0
)
