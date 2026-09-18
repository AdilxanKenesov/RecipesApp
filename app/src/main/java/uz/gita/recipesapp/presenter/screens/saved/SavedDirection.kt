package uz.gita.recipesapp.presenter.screens.saved

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.allrecipes.AllRecipesScreen
import uz.gita.recipesapp.presenter.screens.recipedetail.RecipeDetailScreen
import uz.gita.recipesapp.presenter.ui.state.MainTab
import uz.gita.recipesapp.presenter.ui.state.TabSwitcher
import javax.inject.Inject

class SavedDirection @Inject constructor(
    private val navigator: AppNavigator,
    private val tabSwitcher: TabSwitcher
) : SavedContract.Direction {

    override fun openRecipe(recipeId: Int) {
        navigator.navigateTo(RecipeDetailScreen(recipeId))
    }

    override fun openCategories() {
        tabSwitcher.switchTo(MainTab.CATEGORIES)
    }

    override fun openAllRecipes() {
        navigator.navigateTo(AllRecipesScreen())
    }
}
