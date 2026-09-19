package uz.gita.recipesapp.domain.usecase.categories

import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.repository.CategoryRepository
import uz.gita.recipesapp.domain.repository.SettingsRepository
import javax.inject.Inject

class CategoriesUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val settingsRepository: SettingsRepository
) : CategoriesUseCase {

    override suspend fun getCategories(): Result<List<CategoryUiData>> =
        categoryRepository.getCategories(language = settingsRepository.getLanguage().value)
}
