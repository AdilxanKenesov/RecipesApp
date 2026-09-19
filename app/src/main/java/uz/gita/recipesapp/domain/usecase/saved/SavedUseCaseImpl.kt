package uz.gita.recipesapp.domain.usecase.saved

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.module.ShoppingItemUiData
import uz.gita.recipesapp.domain.repository.FavoriteRepository
import uz.gita.recipesapp.domain.repository.SettingsRepository
import uz.gita.recipesapp.domain.repository.ShoppingRepository
import javax.inject.Inject

class SavedUseCaseImpl @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val shoppingRepository: ShoppingRepository,
    private val settingsRepository: SettingsRepository
) : SavedUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFavorites(): Flow<List<RecipeUiData>> =
        settingsRepository.getLanguage().flatMapLatest { language ->
            favoriteRepository.getFavorites(language)
        }

    override fun getShoppingItems(): Flow<List<ShoppingItemUiData>> = shoppingRepository.getItems()

    override suspend fun removeFavorite(recipeId: Int) {
        favoriteRepository.removeFavorite(recipeId)
    }

    override suspend fun toggleShoppingItem(itemId: Int) {
        shoppingRepository.toggleItem(itemId)
    }

    override suspend fun clearShoppingList() {
        shoppingRepository.clear()
    }
}
