package uz.gita.recipesapp.presenter.screens.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.presenter.ui.state.AppSettingsStore
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val direction: SplashContract.Direction,
    private val settings: AppSettingsStore
) : ViewModel(), SplashContract.SplashViewModel {

    override fun onEventDispatcher(event: SplashContract.SplashEvent) {
        when (event) {
            SplashContract.SplashEvent.Start -> {
                if (settings.introShown.value) direction.openHome() else direction.openIntro()
            }
        }
    }

    override val container = orbitContainer<SplashContract.SplashUiState, SplashContract.SideEffect>(
        SplashContract.SplashUiState()
    )
}
