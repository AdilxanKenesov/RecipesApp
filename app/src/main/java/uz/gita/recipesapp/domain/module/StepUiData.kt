package uz.gita.recipesapp.domain.module

data class StepUiData(
    val number: Int,
    val label: String,
    val text: String,
    val images: List<String> = emptyList(),
    val timerMinutes: Int? = null
)
