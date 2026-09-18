package uz.gita.recipesapp.presenter.screens.settings

import uz.gita.recipesapp.navigation.AppNavigator
import javax.inject.Inject

class SettingsDirection @Inject constructor(
    private val navigator: AppNavigator
) : SettingsContract.Direction {

    override fun back() {
        navigator.back()
    }
}
