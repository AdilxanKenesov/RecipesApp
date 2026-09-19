package uz.gita.recipesapp.domain.usecase.settings

import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.ThemeMode
import uz.gita.recipesapp.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository
) : SettingsUseCase {

    override fun getLanguage(): AppLanguage = settingsRepository.getLanguage().value

    override fun setLanguage(language: AppLanguage) {
        settingsRepository.setLanguage(language)
    }

    override fun getThemeMode(): ThemeMode = settingsRepository.getThemeMode().value

    override fun setThemeMode(mode: ThemeMode) {
        settingsRepository.setThemeMode(mode)
    }
}
