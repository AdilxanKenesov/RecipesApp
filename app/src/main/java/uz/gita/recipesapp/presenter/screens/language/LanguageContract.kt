package uz.gita.recipesapp.presenter.screens.language

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.AppLanguage

interface LanguageContract {
    interface LanguageViewModel : OrbitContainerHost<LanguageUiState, LanguageUiState, SideEffect> {
        fun onEventDispatcher(event: LanguageEvent)
    }

    sealed interface LanguageEvent {
        data class Select(val language: AppLanguage) : LanguageEvent
        data object Confirm : LanguageEvent
        data object Back : LanguageEvent
    }

    data class LanguageUiState(
        val selected: AppLanguage = AppLanguage.UZ
    )

    sealed interface SideEffect

    interface Direction {
        fun openHome()
        fun back()
    }
}
