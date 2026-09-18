package uz.gita.recipesapp.presenter.ui.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsStore @Inject constructor() {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.UZ)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _introShown = MutableStateFlow(false)
    val introShown: StateFlow<Boolean> = _introShown.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun setLanguage(language: AppLanguage) {
        _language.value = language
    }

    fun setIntroShown(shown: Boolean) {
        _introShown.value = shown
    }
}
