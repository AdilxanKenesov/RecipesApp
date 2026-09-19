package uz.gita.recipesapp.data.repository_impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import uz.gita.recipesapp.data.mapper.toFavoriteEntity
import uz.gita.recipesapp.data.mapper.toUIData
import uz.gita.recipesapp.data.source.local.room.dao.FavoriteDao
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getFavorites(language: AppLanguage): Flow<List<RecipeUiData>> =
        favoriteDao.getFavorites().map { list -> list.map { it.toUIData(language.code) } }

    override fun getFavoriteIds(): Flow<Set<Int>> =
        favoriteDao.getFavoriteIds().map { it.toSet() }.distinctUntilChanged()

    override suspend fun toggleFavorite(recipe: RecipeUiData) {
        if (favoriteDao.exists(recipe.id)) {
            favoriteDao.delete(recipe.id)
        } else {
            favoriteDao.insert(recipe.toFavoriteEntity())
        }
    }

    override suspend fun toggleFavorite(recipe: RecipeDetailUiData) {
        if (favoriteDao.exists(recipe.id)) {
            favoriteDao.delete(recipe.id)
        } else {
            favoriteDao.insert(recipe.toFavoriteEntity())
        }
    }

    override suspend fun removeFavorite(recipeId: Int) {
        favoriteDao.delete(recipeId)
    }
}
