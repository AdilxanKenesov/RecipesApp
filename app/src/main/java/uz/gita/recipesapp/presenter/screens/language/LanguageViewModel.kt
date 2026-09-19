package uz.gita.recipesapp.presenter.screens.language

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.usecase.language.LanguageUseCase
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val direction: LanguageContract.Direction,
    private val languageUseCase: LanguageUseCase
) : ViewModel(), LanguageContract.LanguageViewModel {

    override fun onEventDispatcher(event: LanguageContract.LanguageEvent) {
        when (event) {
            is LanguageContract.LanguageEvent.Select -> intent {
                languageUseCase.setLanguage(event.language)
                reduce { state.copy(selected = event.language) }
            }

            LanguageContract.LanguageEvent.Confirm -> {
                languageUseCase.completeOnboarding()
                direction.openHome()
            }

            LanguageContract.LanguageEvent.Back -> direction.back()
        }
    }

    override val container = orbitContainer<LanguageContract.LanguageUiState, LanguageContract.SideEffect>(
        LanguageContract.LanguageUiState(selected = languageUseCase.getLanguage())
    )
}
