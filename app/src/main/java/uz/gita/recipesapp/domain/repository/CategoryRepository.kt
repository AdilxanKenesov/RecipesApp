package uz.gita.recipesapp.domain.repository

import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.CategoryUiData

interface CategoryRepository {

    suspend fun getCategories(language: AppLanguage): Result<List<CategoryUiData>>
}
