package uz.gita.recipesapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class StepDto(
    @SerializedName("step_num") val stepNum: Int = 0,
    @SerializedName("step_label") val stepLabel: String = "",
    val text: String = "",
    val images: List<String> = emptyList()
)
