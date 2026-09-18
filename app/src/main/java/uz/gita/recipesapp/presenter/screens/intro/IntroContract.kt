package uz.gita.recipesapp.presenter.screens.intro

import org.orbitmvi.orbit.OrbitContainerHost

interface IntroContract {
    interface IntroViewModel : OrbitContainerHost<IntroUiState, IntroUiState, SideEffect> {
        fun onEventDispatcher(event: IntroEvent)
    }

    sealed interface IntroEvent {
        data class PageChanged(val page: Int) : IntroEvent
        data object Next : IntroEvent
        data object Previous : IntroEvent
        data object Skip : IntroEvent
    }

    data class IntroUiState(
        val page: Int = 0,
        val pageCount: Int = 2
    ) {
        val isLastPage: Boolean get() = page == pageCount - 1
    }

    sealed interface SideEffect {
        data class ScrollTo(val page: Int) : SideEffect
    }

    interface Direction {
        fun openLanguage()
    }
}
