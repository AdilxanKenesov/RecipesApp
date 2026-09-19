package uz.gita.recipesapp.navigation

import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object AppNavigationDispatcher : AppNavigator, AppNavigationHandler {

    private val commands = Channel<AppNavigationParam>(Channel.BUFFERED)

    override val backStack: Flow<AppNavigationParam> = commands.receiveAsFlow()

    private fun navigate(param: AppNavigationParam) {
        commands.trySend(param)
    }

    override fun navigateTo(screen: Screen) = navigate {
        if (lastItemOrNull?.key != screen.key) push(screen)
    }

    override fun replaceTo(screen: Screen) = navigate {
        if (lastItemOrNull?.key != screen.key) replace(screen)
    }

    override fun replaceAll(screen: Screen) = navigate {
        this.replaceAll(screen)
    }

    override fun replaceAll(screens: List<Screen>) = navigate {
        this.replaceAll(screens)
    }

    override fun back() = navigate {
        if (canPop) pop()
    }

    override fun backTo(predicate: (Screen) -> Boolean) = navigate {
        popUntil(predicate)
    }
}
