package uz.gita.recipesapp.data.mapper

import uz.gita.recipesapp.data.source.remote.api.ApiConstants
import uz.gita.recipesapp.data.source.remote.response.CategoryDto
import uz.gita.recipesapp.domain.module.CategoryUiData

private val categoryNames: Map<String, Pair<String, String>> = mapOf(
    "nonushta" to ("Nonushta" to "Завтраки"),
    "sho'rva" to ("Sho'rvalar" to "Супы"),
    "go'sht" to ("Go'shtli taomlar" to "Мясные блюда"),
    "baliq" to ("Baliqli taomlar" to "Рыбные блюда"),
    "sabzavot" to ("Sabzavotli taomlar" to "Овощные блюда"),
    "salat" to ("Salatlar" to "Салаты"),
    "shirinlik" to ("Shirinliklar" to "Десерты"),
    "non" to ("Non va xamirlar" to "Выпечка"),
    "ichimlik" to ("Ichimliklar" to "Напитки"),
    "boshqa" to ("Boshqalar" to "Прочее")
)

fun categoryName(key: String, lang: String): String {
    val names = categoryNames[key] ?: return key.replaceFirstChar { it.uppercase() }
    return if (lang == ApiConstants.LANG_RU) names.second else names.first
}

fun CategoryDto.toUIData(lang: String): CategoryUiData =
    CategoryUiData(
        key = key,
        name = (if (lang == ApiConstants.LANG_RU) nameRu else nameUz).ifBlank { categoryName(key, lang) },
        emoji = icon,
        count = count
    )
