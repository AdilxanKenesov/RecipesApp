package uz.gita.recipesapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RecipeShortDto(
    val id: Int = 0,
    val slug: String = "",
    val lang: String = "",
    val title: String = "",
    @SerializedName("primary_category") val primaryCategory: String = "",
    @SerializedName("image_url") val imageUrl: String = "",
    @SerializedName("video_url") val videoUrl: String = "",
    @SerializedName("has_video") val hasVideo: Boolean = false
)
