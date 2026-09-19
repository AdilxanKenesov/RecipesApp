package uz.gita.recipesapp.domain.usecase.language

import uz.gita.recipesapp.domain.module.AppLanguage

interface LanguageUseCase {

    fun getLanguage(): AppLanguage

    fun setLanguage(language: AppLanguage)

    fun completeOnboarding()
}
