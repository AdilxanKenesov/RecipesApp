package uz.gita.recipesapp.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.ThemeMode

interface SettingsRepository {

    fun getLanguage(): StateFlow<AppLanguage>

    fun setLanguage(language: AppLanguage)

    fun getThemeMode(): StateFlow<ThemeMode>

    fun setThemeMode(mode: ThemeMode)

    fun isActive(): Boolean

    fun setActive(active: Boolean)
}
