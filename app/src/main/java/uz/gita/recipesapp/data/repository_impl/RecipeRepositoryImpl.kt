package uz.gita.recipesapp.data.repository_impl

import android.util.LruCache
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import uz.gita.recipesapp.data.mapper.inLanguage
import uz.gita.recipesapp.data.mapper.toUIData
import uz.gita.recipesapp.data.source.remote.api.ApiConstants
import uz.gita.recipesapp.data.source.remote.api.OshxonaApi
import uz.gita.recipesapp.data.source.remote.api.safeApiCall
import uz.gita.recipesapp.data.source.remote.paging.CategoryRecipesPagingSource
import uz.gita.recipesapp.data.source.remote.paging.RecipesPagingSource
import uz.gita.recipesapp.data.source.remote.response.PaginatedRecipesDto
import uz.gita.recipesapp.domain.exception.NotFoundException
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.repository.RecipeRepository
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: OshxonaApi
) : RecipeRepository {

    private val detailCache = LruCache<String, RecipeDetailUiData>(DETAIL_CACHE_SIZE)
    private val recipePools = ConcurrentHashMap<AppLanguage, List<RecipeUiData>>()

    override fun getRecipesPaging(language: AppLanguage): Flow<PagingData<RecipeUiData>> =
        Pager(config = pagingConfig(RecipesPagingSource.PAGE_SIZE)) {
            RecipesPagingSource(api = api, lang = language.code)
        }.flow

    override fun getCategoryRecipesPaging(
        categoryKey: String,
        language: AppLanguage
    ): Flow<PagingData<RecipeUiData>> =
        Pager(config = pagingConfig(CategoryRecipesPagingSource.PAGE_SIZE)) {
            CategoryRecipesPagingSource(api = api, categoryKey = categoryKey, lang = language.code)
        }.flow

    override suspend fun getRecipes(
        language: AppLanguage,
        limit: Int
    ): Result<List<RecipeUiData>> =
        recipePool(language).map { it.take(limit) }

    override suspend fun getCategoryRecipes(
        categoryKey: String,
        language: AppLanguage,
        limit: Int
    ): Result<List<RecipeUiData>> =
        collectInLanguage(language.code, limit, CATEGORY_SCAN_PAGES) { page ->
            api.categoryRecipes(
                key = categoryKey,
                lang = language.code,
                page = page,
                perPage = ApiConstants.MAX_PER_PAGE
            )
        }

    override suspend fun getRandomRecipe(
        language: AppLanguage,
        categoryKey: String?
    ): Result<RecipeUiData> {
        val candidates = if (categoryKey == null) {
            recipePool(language)
        } else {
            getCategoryRecipes(categoryKey, language, POOL_SIZE)
        }
        return candidates.mapCatching { list -> list.randomOrNull() ?: throw NotFoundException() }
    }

    override suspend fun getRecipeDetail(
        recipeId: Int,
        language: AppLanguage
    ): Result<RecipeDetailUiData> {
        val cacheKey = "${recipeId}_${language.code}"
        detailCache.get(cacheKey)?.let { return Result.success(it) }
        return safeApiCall(
            request = { api.recipe(recipeId = recipeId) },
            transform = { it.toUIData(language.code) }
        ).onSuccess { detailCache.put(cacheKey, it) }
    }

    private suspend fun recipePool(language: AppLanguage): Result<List<RecipeUiData>> {
        recipePools[language]?.let { return Result.success(it) }
        return collectInLanguage(language.code, POOL_SIZE, POOL_SCAN_PAGES) { page ->
            api.recipes(
                lang = language.code,
                page = page,
                perPage = ApiConstants.MAX_PER_PAGE
            )
        }.onSuccess { if (it.isNotEmpty()) recipePools[language] = it }
    }

    private suspend fun collectInLanguage(
        lang: String,
        limit: Int,
        maxPages: Int,
        request: suspend (page: Int) -> Response<PaginatedRecipesDto>
    ): Result<List<RecipeUiData>> {
        val collected = mutableListOf<RecipeUiData>()
        var page = FIRST_PAGE
        while (page < maxPages) {
            val result = safeApiCall(request = { request(page) }, transform = { it })
            val body = result.getOrElse { error ->
                return if (collected.isEmpty()) Result.failure(error) else Result.success(collected)
            }
            collected += body.items.inLanguage(lang).map { it.toUIData(lang) }
            if (collected.size >= limit || page + 1 >= body.totalPages || body.items.isEmpty()) break
            page++
        }
        return Result.success(collected.take(limit))
    }

    private fun pagingConfig(pageSize: Int): PagingConfig =
        PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize,
            enablePlaceholders = false
        )

    companion object {
        private const val DETAIL_CACHE_SIZE = 20
        private const val FIRST_PAGE = 0
        private const val POOL_SIZE = 50
        private const val POOL_SCAN_PAGES = 10
        private const val CATEGORY_SCAN_PAGES = 3
    }
}
