package uz.gita.recipesapp.presenter.screens.recipedetail

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.main.MainScreen
import uz.gita.recipesapp.presenter.ui.state.MainTab
import uz.gita.recipesapp.presenter.ui.state.TabSwitcher
import javax.inject.Inject

class RecipeDetailDirection @Inject constructor(
    private val navigator: AppNavigator,
    private val tabSwitcher: TabSwitcher
) : RecipeDetailContract.Direction {

    override fun back() {
        navigator.back()
    }

    override fun openRecipe(recipeId: Int) {
        navigator.navigateTo(RecipeDetailScreen(recipeId))
    }

    override fun openHome() {
        tabSwitcher.switchTo(MainTab.HOME)
        navigator.backTo { it is MainScreen }
    }
}
