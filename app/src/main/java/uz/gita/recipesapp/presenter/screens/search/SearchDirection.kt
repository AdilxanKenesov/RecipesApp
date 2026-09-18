package uz.gita.recipesapp.presenter.screens.search

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.categoryrecipes.CategoryRecipesScreen
import uz.gita.recipesapp.presenter.screens.recipedetail.RecipeDetailScreen
import javax.inject.Inject

class SearchDirection @Inject constructor(
    private val navigator: AppNavigator
) : SearchContract.Direction {

    override fun openRecipe(recipeId: Int) {
        navigator.navigateTo(RecipeDetailScreen(recipeId))
    }

    override fun openCategoryRecipes(categoryKey: String) {
        navigator.navigateTo(CategoryRecipesScreen(categoryKey))
    }
}
