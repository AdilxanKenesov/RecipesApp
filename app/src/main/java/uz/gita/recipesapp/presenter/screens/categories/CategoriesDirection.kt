package uz.gita.recipesapp.presenter.screens.categories

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.categoryrecipes.CategoryRecipesScreen
import javax.inject.Inject

class CategoriesDirection @Inject constructor(
    private val navigator: AppNavigator
) : CategoriesContract.Direction {

    override fun openCategoryRecipes(categoryKey: String) {
        navigator.navigateTo(CategoryRecipesScreen(categoryKey))
    }
}
