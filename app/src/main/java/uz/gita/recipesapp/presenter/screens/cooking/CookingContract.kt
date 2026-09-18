package uz.gita.recipesapp.presenter.screens.cooking

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.StepUiData

interface CookingContract {
    interface CookingViewModel : OrbitContainerHost<CookingUiState, CookingUiState, SideEffect> {
        fun onEventDispatcher(event: CookingEvent)
    }

    enum class StepStatus {
        DONE,
        SKIPPED
    }

    sealed interface CookingEvent {
        data class Load(val recipeId: Int) : CookingEvent
        data class PageChanged(val index: Int) : CookingEvent
        data class GoToStep(val index: Int) : CookingEvent
        data object Next : CookingEvent
        data object Skip : CookingEvent
        data object Previous : CookingEvent
        data object ToggleTimer : CookingEvent
        data object ResetTimer : CookingEvent
        data object Close : CookingEvent
    }

    data class StepTimer(
        val stepIndex: Int,
        val totalSeconds: Int,
        val remainingSeconds: Int,
        val isRunning: Boolean = false
    ) {
        val isFinished: Boolean get() = remainingSeconds == 0
        val isStarted: Boolean get() = remainingSeconds < totalSeconds
        val progress: Float get() = if (totalSeconds == 0) 0f else remainingSeconds.toFloat() / totalSeconds
    }

    data class CookingUiState(
        val title: String = "",
        val steps: List<StepUiData> = emptyList(),
        val currentIndex: Int = 0,
        val statuses: Map<Int, StepStatus> = emptyMap(),
        val timer: StepTimer? = null,
        val isFinished: Boolean = false
    ) {
        val isLastStep: Boolean get() = currentIndex >= steps.lastIndex
        val doneCount: Int get() = statuses.values.count { it == StepStatus.DONE }

        fun timerFor(index: Int): StepTimer? = timer?.takeIf { it.stepIndex == index }
    }

    sealed interface SideEffect {
        data class ScrollTo(val index: Int) : SideEffect
        data object TimerFinished : SideEffect
    }

    interface Direction {
        fun back()
    }
}
