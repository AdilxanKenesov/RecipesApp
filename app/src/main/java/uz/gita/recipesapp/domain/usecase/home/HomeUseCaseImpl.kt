package uz.gita.recipesapp.domain.usecase.home

import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.repository.CategoryRepository
import uz.gita.recipesapp.domain.repository.FavoriteRepository
import uz.gita.recipesapp.domain.repository.RecipeRepository
import uz.gita.recipesapp.domain.repository.SettingsRepository
import javax.inject.Inject

class HomeUseCaseImpl @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val favoriteRepository: FavoriteRepository,
    private val settingsRepository: SettingsRepository
) : HomeUseCase {

    override suspend fun getRandomRecipe(): Result<RecipeUiData> =
        recipeRepository.getRandomRecipe(language = settingsRepository.getLanguage().value)

    override suspend fun getCategories(): Result<List<CategoryUiData>> =
        categoryRepository.getCategories(language = settingsRepository.getLanguage().value)

    override suspend fun getLatestRecipes(): Result<List<RecipeUiData>> =
        recipeRepository.getRecipes(
            language = settingsRepository.getLanguage().value,
            limit = LATEST_RECIPES_COUNT
        )

    override fun getFavoriteIds(): Flow<Set<Int>> = favoriteRepository.getFavoriteIds()

    override suspend fun toggleFavorite(recipe: RecipeUiData) {
        favoriteRepository.toggleFavorite(recipe)
    }

    companion object {
        private const val LATEST_RECIPES_COUNT = 3
    }
}
