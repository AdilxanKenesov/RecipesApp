package uz.gita.recipesapp.presenter.screens.splash

import org.orbitmvi.orbit.OrbitContainerHost

interface SplashContract {
    interface SplashViewModel : OrbitContainerHost<SplashUiState, SplashUiState, SideEffect> {
        fun onEventDispatcher(event: SplashEvent)
    }

    sealed interface SplashEvent {
        data object Start : SplashEvent
    }

    data class SplashUiState(
        val isLoading: Boolean = false
    )

    sealed interface SideEffect

    interface Direction {
        fun openIntro()
        fun openHome()
    }
}
