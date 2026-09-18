package uz.gita.recipesapp.presenter.screens.intro

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.language.LanguageScreen
import javax.inject.Inject

class IntroDirection @Inject constructor(
    private val navigator: AppNavigator
) : IntroContract.Direction {

    override fun openLanguage() {
        navigator.navigateTo(LanguageScreen())
    }
}
