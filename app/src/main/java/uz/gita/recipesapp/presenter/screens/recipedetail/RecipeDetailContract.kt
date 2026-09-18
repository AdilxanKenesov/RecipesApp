package uz.gita.recipesapp.presenter.screens.recipedetail

import org.orbitmvi.orbit.OrbitContainerHost
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.module.StepUiData

interface RecipeDetailContract {
    interface RecipeDetailViewModel :
        OrbitContainerHost<RecipeDetailUiState, RecipeDetailUiState, SideEffect> {
        fun onEventDispatcher(event: RecipeDetailEvent)
    }

    sealed interface RecipeDetailEvent {
        data class Load(val recipeId: Int) : RecipeDetailEvent
        data class ToggleIngredient(val ingredientId: Int) : RecipeDetailEvent
        data class OpenRecipe(val recipeId: Int) : RecipeDetailEvent
        data class OpenTimer(val step: StepUiData) : RecipeDetailEvent
        data class ToggleShoppingItem(val ingredientId: Int) : RecipeDetailEvent
        data object ToggleFavorite : RecipeDetailEvent
        data object Share : RecipeDetailEvent
        data object OpenInBrowser : RecipeDetailEvent
        data object CloseTimer : RecipeDetailEvent
        data object ToggleTimer : RecipeDetailEvent
        data object ResetTimer : RecipeDetailEvent
        data object OpenShoppingSheet : RecipeDetailEvent
        data object CloseShoppingSheet : RecipeDetailEvent
        data object ToggleAllShopping : RecipeDetailEvent
        data object ConfirmShopping : RecipeDetailEvent
        data object OpenHome : RecipeDetailEvent
        data object Back : RecipeDetailEvent
    }

    data class TimerUiState(
        val stepNumber: Int,
        val totalSeconds: Int,
        val remainingSeconds: Int,
        val isRunning: Boolean = false
    ) {
        val isFinished: Boolean get() = remainingSeconds == 0
        val isStarted: Boolean get() = remainingSeconds < totalSeconds
        val progress: Float get() = if (totalSeconds == 0) 0f else remainingSeconds.toFloat() / totalSeconds
    }

    data class RecipeDetailUiState(
        val isLoading: Boolean = false,
        val notFound: Boolean = false,
        val recipe: RecipeDetailUiData? = null,
        val related: List<RecipeUiData> = emptyList(),
        val timer: TimerUiState? = null,
        val shoppingSelection: Set<Int>? = null
    )

    sealed interface SideEffect {
        data class OpenUrl(val url: String) : SideEffect
        data class ShareUrl(val url: String) : SideEffect
        data object AddedToShoppingList : SideEffect
    }

    interface Direction {
        fun back()
        fun openRecipe(recipeId: Int)
        fun openHome()
    }
}
