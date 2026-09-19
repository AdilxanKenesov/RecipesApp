package uz.gita.recipesapp.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CancellationException
import uz.gita.recipesapp.data.mapper.inLanguage
import uz.gita.recipesapp.data.mapper.toUIData
import uz.gita.recipesapp.data.source.remote.api.ApiConstants
import uz.gita.recipesapp.data.source.remote.api.OshxonaApi
import uz.gita.recipesapp.data.source.remote.api.toDomainException
import uz.gita.recipesapp.data.source.remote.api.toException
import uz.gita.recipesapp.domain.module.RecipeUiData

class RecipesPagingSource(
    private val api: OshxonaApi,
    private val lang: String
) : PagingSource<Int, RecipeUiData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeUiData> {
        var page = params.key ?: INITIAL_PAGE
        var scannedPages = 0

        try {
            while (true) {
                val response = api.recipes(
                    lang = lang,
                    page = page,
                    perPage = PAGE_SIZE
                )
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    return LoadResult.Error(response.toException())
                }

                val recipes = body.items.inLanguage(lang).map { it.toUIData(lang) }
                val hasMore = page + 1 < body.totalPages && body.items.isNotEmpty()
                scannedPages++

                if (recipes.isNotEmpty() || !hasMore || scannedPages >= MAX_PAGES_PER_LOAD) {
                    return LoadResult.Page(
                        data = recipes,
                        prevKey = null,
                        nextKey = if (hasMore) page + 1 else null
                    )
                }
                page++
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return LoadResult.Error(e.toDomainException())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipeUiData>): Int? = null

    companion object {
        const val PAGE_SIZE = ApiConstants.MAX_PER_PAGE
        private const val INITIAL_PAGE = 0
        private const val MAX_PAGES_PER_LOAD = 10
    }
}
