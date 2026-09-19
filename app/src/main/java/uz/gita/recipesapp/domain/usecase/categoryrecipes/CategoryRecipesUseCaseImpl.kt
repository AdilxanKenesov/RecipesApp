package uz.gita.recipesapp.domain.usecase.categoryrecipes

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.repository.CategoryRepository
import uz.gita.recipesapp.domain.repository.FavoriteRepository
import uz.gita.recipesapp.domain.repository.RecipeRepository
import uz.gita.recipesapp.domain.repository.SettingsRepository
import javax.inject.Inject

class CategoryRecipesUseCaseImpl @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val favoriteRepository: FavoriteRepository,
    private val settingsRepository: SettingsRepository
) : CategoryRecipesUseCase {

    override suspend fun getCategory(categoryKey: String): Result<CategoryUiData?> =
        categoryRepository.getCategories(language = settingsRepository.getLanguage().value)
            .map { categories -> categories.firstOrNull { it.key == categoryKey } }

    override fun getRecipes(categoryKey: String): Flow<PagingData<RecipeUiData>> =
        recipeRepository.getCategoryRecipesPaging(
            categoryKey = categoryKey,
            language = settingsRepository.getLanguage().value
        )

    override fun getFavoriteIds(): Flow<Set<Int>> = favoriteRepository.getFavoriteIds()

    override suspend fun toggleFavorite(recipe: RecipeUiData) {
        favoriteRepository.toggleFavorite(recipe)
    }
}
