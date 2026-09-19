package uz.gita.recipesapp.data.repository_impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.gita.recipesapp.data.source.local.pref.AppPreferences
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.ThemeMode
import uz.gita.recipesapp.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences
) : SettingsRepository {

    private val languageState = MutableStateFlow(appPreferences.language)
    private val themeModeState = MutableStateFlow(appPreferences.themeMode)

    override fun getLanguage(): StateFlow<AppLanguage> = languageState.asStateFlow()

    override fun setLanguage(language: AppLanguage) {
        appPreferences.language = language
        languageState.value = language
    }

    override fun getThemeMode(): StateFlow<ThemeMode> = themeModeState.asStateFlow()

    override fun setThemeMode(mode: ThemeMode) {
        appPreferences.themeMode = mode
        themeModeState.value = mode
    }

    override fun isActive(): Boolean = appPreferences.isActive

    override fun setActive(active: Boolean) {
        appPreferences.isActive = active
    }
}
