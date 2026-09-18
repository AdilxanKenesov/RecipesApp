package uz.gita.recipesapp.presenter.screens.categoryrecipes

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.recipedetail.RecipeDetailScreen
import javax.inject.Inject

class CategoryRecipesDirection @Inject constructor(
    private val navigator: AppNavigator
) : CategoryRecipesContract.Direction {

    override fun openRecipe(recipeId: Int) {
        navigator.navigateTo(RecipeDetailScreen(recipeId))
    }

    override fun back() {
        navigator.back()
    }
}
