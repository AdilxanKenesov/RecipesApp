package uz.gita.recipesapp.data.repository_impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.gita.recipesapp.data.mapper.inLanguage
import uz.gita.recipesapp.data.mapper.toUIData
import uz.gita.recipesapp.data.source.local.pref.AppPreferences
import uz.gita.recipesapp.data.source.remote.api.ApiConstants
import uz.gita.recipesapp.data.source.remote.api.OshxonaApi
import uz.gita.recipesapp.data.source.remote.api.safeApiCall
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: OshxonaApi,
    private val appPreferences: AppPreferences
) : SearchRepository {

    private val recentSearches = MutableStateFlow(appPreferences.recentSearches)

    override suspend fun searchByName(
        query: String,
        language: AppLanguage
    ): Result<List<RecipeUiData>> =
        safeApiCall(
            request = {
                api.search(
                    query = query.trim(),
                    lang = language.code,
                    limit = ApiConstants.DEFAULT_SEARCH_LIMIT
                )
            },
            transform = { result -> result.items.inLanguage(language.code).map { it.toUIData(language.code) } }
        )

    override suspend fun searchByIngredients(
        ingredients: List<String>,
        language: AppLanguage
    ): Result<List<RecipeUiData>> {
        val query = ingredients
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinctBy { it.lowercase() }
            .take(ApiConstants.MAX_INGREDIENTS)
            .joinToString(ApiConstants.INGREDIENT_SEPARATOR)
        if (query.isEmpty()) return Result.success(emptyList())
        return safeApiCall(
            request = {
                api.searchByIngredients(
                    ingredients = query,
                    lang = language.code,
                    limit = ApiConstants.DEFAULT_SEARCH_LIMIT
                )
            },
            transform = { result -> result.items.inLanguage(language.code).map { it.toUIData(language.code) } }
        )
    }

    override fun getRecentSearches(): StateFlow<List<String>> = recentSearches.asStateFlow()

    override fun addRecentSearch(query: String) {
        val value = query.trim()
        if (value.isEmpty()) return
        val updated = (listOf(value) + recentSearches.value.filterNot { it.equals(value, ignoreCase = true) })
            .take(MAX_RECENT_SEARCHES)
        appPreferences.recentSearches = updated
        recentSearches.value = updated
    }

    override fun clearRecentSearches() {
        appPreferences.recentSearches = emptyList()
        recentSearches.value = emptyList()
    }

    companion object {
        private const val MAX_RECENT_SEARCHES = 6
    }
}
