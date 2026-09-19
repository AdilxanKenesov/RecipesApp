package uz.gita.recipesapp.domain.usecase.cooking

import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.repository.RecipeRepository
import uz.gita.recipesapp.domain.repository.SettingsRepository
import javax.inject.Inject

class CookingUseCaseImpl @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val settingsRepository: SettingsRepository
) : CookingUseCase {

    override suspend fun getRecipe(recipeId: Int): Result<RecipeDetailUiData> =
        recipeRepository.getRecipeDetail(
            recipeId = recipeId,
            language = settingsRepository.getLanguage().value
        )
}
