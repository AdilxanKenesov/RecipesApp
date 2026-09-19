package uz.gita.recipesapp.presenter.screens.cooking

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.usecase.cooking.CookingUseCase
import uz.gita.recipesapp.presenter.ui.state.AppMessenger
import uz.gita.recipesapp.presenter.ui.util.cleanRecipeTitle
import javax.inject.Inject

@HiltViewModel
class CookingViewModel @Inject constructor(
    private val direction: CookingContract.Direction,
    private val cookingUseCase: CookingUseCase,
    private val messenger: AppMessenger
) : ViewModel(), CookingContract.CookingViewModel {

    private var timerJob: Job? = null

    override fun onEventDispatcher(event: CookingContract.CookingEvent) {
        when (event) {
            is CookingContract.CookingEvent.Load -> intent {
                if (state.steps.isNotEmpty()) return@intent
                cookingUseCase.getRecipe(event.recipeId)
                    .onSuccess { recipe ->
                        if (recipe.steps.isEmpty()) {
                            direction.back()
                            return@onSuccess
                        }
                        reduce { state.copy(title = recipe.title.cleanRecipeTitle(), steps = recipe.steps) }
                    }
                    .onFailure { error ->
                        messenger.showError(error)
                        direction.back()
                    }
            }

            is CookingContract.CookingEvent.PageChanged -> intent {
                reduce { state.copy(currentIndex = event.index) }
            }

            is CookingContract.CookingEvent.GoToStep -> intent {
                postSideEffect(CookingContract.SideEffect.ScrollTo(event.index))
            }

            CookingContract.CookingEvent.Next -> completeStep(CookingContract.StepStatus.DONE)

            CookingContract.CookingEvent.Skip -> completeStep(CookingContract.StepStatus.SKIPPED)

            CookingContract.CookingEvent.Previous -> intent {
                if (state.currentIndex > 0) {
                    postSideEffect(CookingContract.SideEffect.ScrollTo(state.currentIndex - 1))
                }
            }

            CookingContract.CookingEvent.ToggleTimer -> intent {
                val index = state.currentIndex
                val minutes = state.steps.getOrNull(index)?.timerMinutes ?: return@intent
                val current = state.timerFor(index)
                when {
                    current == null -> {
                        stopTimer()
                        reduce {
                            state.copy(
                                timer = CookingContract.StepTimer(
                                    stepIndex = index,
                                    totalSeconds = minutes * 60,
                                    remainingSeconds = minutes * 60,
                                    isRunning = true
                                )
                            )
                        }
                        startTimer()
                    }

                    current.isFinished -> Unit

                    current.isRunning -> {
                        stopTimer()
                        reduce { state.copy(timer = current.copy(isRunning = false)) }
                    }

                    else -> {
                        reduce { state.copy(timer = current.copy(isRunning = true)) }
                        startTimer()
                    }
                }
            }

            CookingContract.CookingEvent.ResetTimer -> intent {
                if (state.timer?.stepIndex == state.currentIndex) {
                    stopTimer()
                    reduce { state.copy(timer = null) }
                }
            }

            CookingContract.CookingEvent.Close -> {
                stopTimer()
                direction.back()
            }
        }
    }

    private fun completeStep(status: CookingContract.StepStatus) = intent {
        val index = state.currentIndex
        if (state.timer?.stepIndex == index) {
            stopTimer()
            reduce { state.copy(timer = null) }
        }
        val resolved = if (state.statuses[index] == CookingContract.StepStatus.DONE) {
            CookingContract.StepStatus.DONE
        } else {
            status
        }
        reduce { state.copy(statuses = state.statuses + (index to resolved)) }
        if (index >= state.steps.lastIndex) {
            stopTimer()
            reduce { state.copy(isFinished = true, timer = null) }
        } else {
            postSideEffect(CookingContract.SideEffect.ScrollTo(index + 1))
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = intent {
            while (state.timer?.let { it.isRunning && it.remainingSeconds > 0 } == true) {
                delay(1000)
                reduce {
                    val timer = state.timer ?: return@reduce state
                    val remaining = (timer.remainingSeconds - 1).coerceAtLeast(0)
                    state.copy(timer = timer.copy(remainingSeconds = remaining, isRunning = remaining > 0))
                }
                if (state.timer?.isFinished == true) {
                    postSideEffect(CookingContract.SideEffect.TimerFinished)
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override val container = orbitContainer<CookingContract.CookingUiState, CookingContract.SideEffect>(
        CookingContract.CookingUiState()
    )
}
