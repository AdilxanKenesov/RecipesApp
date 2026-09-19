package uz.gita.recipesapp.presenter.screens.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.usecase.settings.SettingsUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val direction: SettingsContract.Direction,
    private val settingsUseCase: SettingsUseCase
) : ViewModel(), SettingsContract.SettingsViewModel {

    override fun onEventDispatcher(event: SettingsContract.SettingsEvent) {
        when (event) {
            is SettingsContract.SettingsEvent.LanguageChanged -> intent {
                if (state.language == event.language) return@intent
                settingsUseCase.setLanguage(event.language)
                reduce { state.copy(language = event.language) }
                direction.reloadWithLanguage()
            }

            is SettingsContract.SettingsEvent.ThemeChanged -> intent {
                settingsUseCase.setThemeMode(event.mode)
                reduce { state.copy(themeMode = event.mode) }
            }

            SettingsContract.SettingsEvent.Back -> direction.back()
        }
    }

    override val container = orbitContainer<SettingsContract.SettingsUiState, SettingsContract.SideEffect>(
        SettingsContract.SettingsUiState(
            language = settingsUseCase.getLanguage(),
            themeMode = settingsUseCase.getThemeMode()
        )
    )
}
