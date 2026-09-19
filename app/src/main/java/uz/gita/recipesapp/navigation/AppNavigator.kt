package uz.gita.recipesapp.navigation

import cafe.adriel.voyager.core.screen.Screen

interface AppNavigator {

    fun navigateTo(screen: Screen)

    fun replaceTo(screen: Screen)

    fun replaceAll(screen: Screen)

    fun replaceAll(screens: List<Screen>)

    fun back()

    fun backTo(predicate: (Screen) -> Boolean)
}
