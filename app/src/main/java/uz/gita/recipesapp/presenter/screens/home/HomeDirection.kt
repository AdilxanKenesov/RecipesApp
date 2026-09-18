package uz.gita.recipesapp.presenter.screens.home

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.allrecipes.AllRecipesScreen
import uz.gita.recipesapp.presenter.screens.categoryrecipes.CategoryRecipesScreen
import uz.gita.recipesapp.presenter.screens.recipedetail.RecipeDetailScreen
import uz.gita.recipesapp.presenter.screens.settings.SettingsScreen
import uz.gita.recipesapp.presenter.ui.state.MainTab
import uz.gita.recipesapp.presenter.ui.state.TabSwitcher
import javax.inject.Inject

class HomeDirection @Inject constructor(
    private val navigator: AppNavigator,
    private val tabSwitcher: TabSwitcher
) : HomeContract.Direction {

    override fun openSettings() {
        navigator.navigateTo(SettingsScreen())
    }

    override fun openRecipe(recipeId: Int) {
        navigator.navigateTo(RecipeDetailScreen(recipeId))
    }

    override fun openCategoryRecipes(categoryKey: String) {
        navigator.navigateTo(CategoryRecipesScreen(categoryKey))
    }

    override fun openCategories() {
        tabSwitcher.switchTo(MainTab.CATEGORIES)
    }

    override fun openIngredientSearch() {
        tabSwitcher.openIngredientSearch()
    }

    override fun openAllRecipes() {
        navigator.navigateTo(AllRecipesScreen())
    }
}
