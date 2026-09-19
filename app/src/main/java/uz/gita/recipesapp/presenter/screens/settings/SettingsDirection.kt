package uz.gita.recipesapp.presenter.screens.settings

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.main.MainScreen
import javax.inject.Inject

class SettingsDirection @Inject constructor(
    private val navigator: AppNavigator
) : SettingsContract.Direction {

    override fun back() {
        navigator.back()
    }

    override fun reloadWithLanguage() {
        navigator.replaceAll(listOf(MainScreen(), SettingsScreen()))
    }
}
