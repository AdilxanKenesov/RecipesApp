package uz.gita.recipesapp.presenter.ui.util

private val titlePrefixes = listOf(
    "Videoretsept: ",
    "Batafsil fotoretsept: ",
    "Пошаговый фоторецепт: ",
    "Видеорецепт: "
)

fun String.cleanRecipeTitle(): String {
    var result = trim()
    titlePrefixes.forEach { prefix ->
        if (result.startsWith(prefix, ignoreCase = true)) {
            result = result.removeRange(0, prefix.length).trim()
        }
    }
    return result
}

fun String.normalizeQuery(): String = replace('\'', '’').trim()
