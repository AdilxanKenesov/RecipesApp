package uz.gita.recipesapp.data.repository_impl

import uz.gita.recipesapp.data.mapper.toUIData
import uz.gita.recipesapp.data.source.remote.api.OshxonaApi
import uz.gita.recipesapp.data.source.remote.api.safeApiCall
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.repository.CategoryRepository
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api: OshxonaApi
) : CategoryRepository {

    private val cache = ConcurrentHashMap<AppLanguage, List<CategoryUiData>>()

    override suspend fun getCategories(language: AppLanguage): Result<List<CategoryUiData>> {
        cache[language]?.let { return Result.success(it) }
        return safeApiCall(
            request = { api.categories(lang = language.code) },
            transform = { list -> list.map { it.toUIData(language.code) } }
        ).onSuccess { cache[language] = it }
    }
}
