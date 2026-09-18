package uz.gita.recipesapp.navigation

import kotlinx.coroutines.flow.Flow

interface AppNavigationHandler {

    val backStack: Flow<AppNavigationParam>
}
