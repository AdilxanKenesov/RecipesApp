package uz.gita.recipesapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RecipeFullDto(
    val id: Int = 0,
    val slug: String = "",
    val lang: String = "",
    val title: String = "",
    @SerializedName("primary_category") val primaryCategory: String = "",
    val description: String = "",
    val ingredients: List<IngredientDto> = emptyList(),
    val steps: List<StepDto> = emptyList(),
    @SerializedName("image_url") val imageUrl: String = "",
    @SerializedName("video_url") val videoUrl: String = "",
    @SerializedName("has_video") val hasVideo: Boolean = false,
    val author: String = "",
    @SerializedName("published_date") val publishedDate: String = "",
    val url: String = "",
    @SerializedName("created_at") val createdAt: String = ""
)
