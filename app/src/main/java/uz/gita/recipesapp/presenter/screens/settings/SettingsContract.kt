package uz.gita.recipesapp.presenter.screens.settings

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.presenter.ui.state.AppLanguage
import uz.gita.recipesapp.presenter.ui.state.ThemeMode

interface SettingsContract {
    interface SettingsViewModel : OrbitContainerHost<SettingsUiState, SettingsUiState, SideEffect> {
        fun onEventDispatcher(event: SettingsEvent)
    }

    sealed interface SettingsEvent {
        data class LanguageChanged(val language: AppLanguage) : SettingsEvent
        data class ThemeChanged(val mode: ThemeMode) : SettingsEvent
        data object Back : SettingsEvent
    }

    data class SettingsUiState(
        val language: AppLanguage = AppLanguage.UZ,
        val themeMode: ThemeMode = ThemeMode.LIGHT
    )

    sealed interface SideEffect

    interface Direction {
        fun back()
    }
}
