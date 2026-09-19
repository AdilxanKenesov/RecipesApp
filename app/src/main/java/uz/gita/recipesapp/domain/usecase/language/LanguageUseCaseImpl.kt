package uz.gita.recipesapp.domain.usecase.language

import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.repository.SettingsRepository
import javax.inject.Inject

class LanguageUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository
) : LanguageUseCase {

    override fun getLanguage(): AppLanguage = settingsRepository.getLanguage().value

    override fun setLanguage(language: AppLanguage) {
        settingsRepository.setLanguage(language)
    }

    override fun completeOnboarding() {
        settingsRepository.setActive(true)
    }
}
