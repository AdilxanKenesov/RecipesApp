package uz.gita.recipesapp.presenter.screens.language

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.main.MainScreen
import javax.inject.Inject

class LanguageDirection @Inject constructor(
    private val navigator: AppNavigator
) : LanguageContract.Direction {

    override fun openHome() {
        navigator.replaceAll(MainScreen())
    }

    override fun back() {
        navigator.back()
    }
}
