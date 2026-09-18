package uz.gita.recipesapp.presenter.screens.cooking

import uz.gita.recipesapp.navigation.AppNavigator
import javax.inject.Inject

class CookingDirection @Inject constructor(
    private val navigator: AppNavigator
) : CookingContract.Direction {

    override fun back() {
        navigator.back()
    }
}
