package uz.gita.recipesapp.data.mapper

import uz.gita.recipesapp.data.source.remote.response.IngredientDto
import uz.gita.recipesapp.data.source.remote.response.RecipeFullDto
import uz.gita.recipesapp.data.source.remote.response.RecipeShortDto
import uz.gita.recipesapp.data.source.remote.response.StepDto
import uz.gita.recipesapp.domain.module.IngredientUiData
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.module.StepUiData

private val timerRegex = Regex(
    """(\d+)(?:\s*[-–]\s*(\d+))?\s*(daqiqa|minut|мин|soat|час)""",
    RegexOption.IGNORE_CASE
)

fun RecipeShortDto.toUIData(lang: String): RecipeUiData =
    RecipeUiData(
        id = id,
        slug = slug,
        title = title,
        categoryKey = primaryCategory,
        categoryName = categoryName(primaryCategory, lang),
        imageUrl = imageUrl,
        hasVideo = hasVideo
    )

fun RecipeFullDto.toUIData(lang: String): RecipeDetailUiData =
    RecipeDetailUiData(
        id = id,
        title = title,
        categoryKey = primaryCategory,
        categoryName = categoryName(primaryCategory, lang),
        imageUrl = imageUrl,
        videoUrl = videoUrl.ifBlank { null },
        hasVideo = hasVideo,
        description = description,
        author = author,
        publishedDate = publishedDate,
        url = url,
        ingredients = ingredients.toUIData(),
        steps = steps.map { it.toUIData() }
    )

fun List<IngredientDto>.toUIData(): List<IngredientUiData> =
    mapIndexedNotNull { index, ingredient ->
        val name = ingredient.name.orEmpty().withoutBrand()
        when {
            name.isEmpty() -> null
            ingredient.isHeading -> IngredientUiData.Heading(text = name)
            else -> IngredientUiData.Item(
                id = index,
                amount = ingredient.amount.orEmpty().trim(),
                name = name
            )
        }
    }

fun StepDto.toUIData(): StepUiData =
    StepUiData(
        number = stepNum,
        label = stepLabel,
        text = text,
        images = images,
        timerMinutes = text.timerMinutes()
    )

private fun String.timerMinutes(): Int? {
    val match = timerRegex.find(this) ?: return null
    val from = match.groupValues[1].toIntOrNull() ?: return null
    val to = match.groupValues[2].toIntOrNull()
    val value = maxOf(from, to ?: from)
    val unit = match.groupValues[3].lowercase()
    val minutes = if (unit == "soat" || unit == "час") value * 60 else value
    return minutes.takeIf { it > 0 }
}
