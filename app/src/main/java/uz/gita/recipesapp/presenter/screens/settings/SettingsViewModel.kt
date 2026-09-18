package uz.gita.recipesapp.presenter.screens.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import uz.gita.recipesapp.presenter.ui.state.AppSettingsStore
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val direction: SettingsContract.Direction,
    private val settings: AppSettingsStore
) : ViewModel(), SettingsContract.SettingsViewModel {

    override fun onEventDispatcher(event: SettingsContract.SettingsEvent) {
        when (event) {
            is SettingsContract.SettingsEvent.LanguageChanged -> intent {
                settings.setLanguage(event.language)
                reduce { state.copy(language = event.language) }
            }

            is SettingsContract.SettingsEvent.ThemeChanged -> intent {
                settings.setThemeMode(event.mode)
                reduce { state.copy(themeMode = event.mode) }
            }

            SettingsContract.SettingsEvent.Back -> direction.back()
        }
    }

    override val container = orbitContainer<SettingsContract.SettingsUiState, SettingsContract.SideEffect>(
        SettingsContract.SettingsUiState(
            language = settings.language.value,
            themeMode = settings.themeMode.value,
            savedCount = SampleData.favorites.size + SampleData.viewed.size
        )
    )
}
