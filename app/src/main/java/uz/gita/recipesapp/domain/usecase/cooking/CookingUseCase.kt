package uz.gita.recipesapp.domain.usecase.cooking

import uz.gita.recipesapp.domain.module.RecipeDetailUiData

interface CookingUseCase {

    suspend fun getRecipe(recipeId: Int): Result<RecipeDetailUiData>
}
