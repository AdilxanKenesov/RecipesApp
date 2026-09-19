package uz.gita.recipesapp.presenter.screens.splash

import uz.gita.recipesapp.navigation.AppNavigator
import uz.gita.recipesapp.presenter.screens.intro.IntroScreen
import javax.inject.Inject

class SplashDirection @Inject constructor(
    private val navigator: AppNavigator
) : SplashContract.Direction {

    override fun openIntro() {
        navigator.replaceAll(IntroScreen())
    }
}
