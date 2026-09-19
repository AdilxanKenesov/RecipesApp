package uz.gita.recipesapp.domain.usecase.categories

import uz.gita.recipesapp.domain.module.CategoryUiData

interface CategoriesUseCase {

    suspend fun getCategories(): Result<List<CategoryUiData>>
}
