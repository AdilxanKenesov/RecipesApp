package uz.gita.recipesapp.domain.usecase.recipedetail

import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.repository.FavoriteRepository
import uz.gita.recipesapp.domain.repository.RecipeRepository
import uz.gita.recipesapp.domain.repository.SettingsRepository
import uz.gita.recipesapp.domain.repository.ShoppingRepository
import javax.inject.Inject

class RecipeDetailUseCaseImpl @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val favoriteRepository: FavoriteRepository,
    private val shoppingRepository: ShoppingRepository,
    private val settingsRepository: SettingsRepository
) : RecipeDetailUseCase {

    override suspend fun getRecipe(recipeId: Int): Result<RecipeDetailUiData> =
        recipeRepository.getRecipeDetail(
            recipeId = recipeId,
            language = settingsRepository.getLanguage().value
        )

    override suspend fun getRelatedRecipes(recipe: RecipeDetailUiData): Result<List<RecipeUiData>> {
        if (recipe.categoryKey.isBlank()) return Result.success(emptyList())
        return recipeRepository.getCategoryRecipes(
            categoryKey = recipe.categoryKey,
            language = settingsRepository.getLanguage().value,
            limit = RELATED_COUNT + 1
        ).map { recipes ->
            recipes.filter { it.id != recipe.id }.take(RELATED_COUNT)
        }
    }

    override fun getFavoriteIds(): Flow<Set<Int>> = favoriteRepository.getFavoriteIds()

    override suspend fun toggleFavorite(recipe: RecipeDetailUiData) {
        favoriteRepository.toggleFavorite(recipe)
    }

    override suspend fun toggleFavorite(recipe: RecipeUiData) {
        favoriteRepository.toggleFavorite(recipe)
    }

    override suspend fun addToShoppingList(recipe: RecipeDetailUiData, ingredientIds: Set<Int>) {
        shoppingRepository.addItems(recipe = recipe, ingredientIds = ingredientIds)
    }

    companion object {
        private const val RELATED_COUNT = 6
    }
}
