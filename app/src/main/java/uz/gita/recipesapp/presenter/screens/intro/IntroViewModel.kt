package uz.gita.recipesapp.presenter.screens.intro

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val direction: IntroContract.Direction
) : ViewModel(), IntroContract.IntroViewModel {

    override fun onEventDispatcher(event: IntroContract.IntroEvent) {
        when (event) {
            is IntroContract.IntroEvent.PageChanged -> intent {
                reduce { state.copy(page = event.page) }
            }

            IntroContract.IntroEvent.Next -> intent {
                if (state.isLastPage) {
                    direction.openLanguage()
                } else {
                    postSideEffect(IntroContract.SideEffect.ScrollTo(state.page + 1))
                }
            }

            IntroContract.IntroEvent.Previous -> intent {
                if (state.page > 0) {
                    postSideEffect(IntroContract.SideEffect.ScrollTo(state.page - 1))
                }
            }

            IntroContract.IntroEvent.Skip -> direction.openLanguage()
        }
    }

    override val container = orbitContainer<IntroContract.IntroUiState, IntroContract.SideEffect>(
        IntroContract.IntroUiState()
    )
}
