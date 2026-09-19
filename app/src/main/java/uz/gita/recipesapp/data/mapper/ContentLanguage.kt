package uz.gita.recipesapp.data.mapper

import uz.gita.recipesapp.data.source.remote.api.ApiConstants
import uz.gita.recipesapp.data.source.remote.response.RecipeShortDto

private val cyrillicRegex = Regex("[А-Яа-яЁё]")

fun String.matchesLanguage(lang: String): Boolean =
    cyrillicRegex.containsMatchIn(this) == (lang == ApiConstants.LANG_RU)

fun List<RecipeShortDto>.inLanguage(lang: String): List<RecipeShortDto> =
    filter { it.title.matchesLanguage(lang) }
