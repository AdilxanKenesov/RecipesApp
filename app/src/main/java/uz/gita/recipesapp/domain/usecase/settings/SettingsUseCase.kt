package uz.gita.recipesapp.domain.usecase.settings

import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.ThemeMode

interface SettingsUseCase {

    fun getLanguage(): AppLanguage

    fun setLanguage(language: AppLanguage)

    fun getThemeMode(): ThemeMode

    fun setThemeMode(mode: ThemeMode)
}
