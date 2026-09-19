package uz.gita.recipesapp.domain.usecase.allrecipes

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.repository.FavoriteRepository
import uz.gita.recipesapp.domain.repository.RecipeRepository
import uz.gita.recipesapp.domain.repository.SettingsRepository
import javax.inject.Inject

class AllRecipesUseCaseImpl @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val favoriteRepository: FavoriteRepository,
    private val settingsRepository: SettingsRepository
) : AllRecipesUseCase {

    override fun getRecipes(): Flow<PagingData<RecipeUiData>> =
        recipeRepository.getRecipesPaging(language = settingsRepository.getLanguage().value)

    override fun getFavoriteIds(): Flow<Set<Int>> = favoriteRepository.getFavoriteIds()

    override suspend fun toggleFavorite(recipe: RecipeUiData) {
        favoriteRepository.toggleFavorite(recipe)
    }
}
