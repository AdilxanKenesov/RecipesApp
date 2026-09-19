package uz.gita.recipesapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    val key: String = "",
    @SerializedName("name_uz") val nameUz: String = "",
    @SerializedName("name_ru") val nameRu: String = "",
    val icon: String = "",
    val count: Int = 0
)
