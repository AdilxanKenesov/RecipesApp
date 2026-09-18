package uz.gita.recipesapp.presenter.screens.allrecipes

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.recipedetail.RecipeDetailScreen
import javax.inject.Inject

class AllRecipesDirection @Inject constructor(
    private val navigator: AppNavigator
) : AllRecipesContract.Direction {

    override fun openRecipe(recipeId: Int) {
        navigator.navigateTo(RecipeDetailScreen(recipeId))
    }

    override fun back() {
        navigator.back()
    }
}
