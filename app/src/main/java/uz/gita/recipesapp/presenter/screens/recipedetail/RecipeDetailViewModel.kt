package uz.gita.recipesapp.presenter.screens.recipedetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.gita.recipesapp.domain.module.IngredientUiData
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.presenter.ui.preview.SampleData
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val direction: RecipeDetailContract.Direction
) : ViewModel(), RecipeDetailContract.RecipeDetailViewModel {

    private var timerJob: Job? = null

    override fun onEventDispatcher(event: RecipeDetailContract.RecipeDetailEvent) {
        when (event) {
            is RecipeDetailContract.RecipeDetailEvent.Load -> intent {
                val detail = SampleData.recipeDetail.takeIf { event.recipeId > 0 }
                val related = if (detail == null) {
                    SampleData.recipes.take(2)
                } else {
                    SampleData.recipes
                        .filter { it.categoryKey == detail.categoryKey && it.id != event.recipeId }
                        .take(6)
                }
                reduce {
                    state.copy(
                        recipe = detail,
                        notFound = detail == null,
                        related = related
                    )
                }
            }

            is RecipeDetailContract.RecipeDetailEvent.ToggleIngredient -> intent {
                reduce {
                    val current = state.recipe
                    state.copy(
                        recipe = current?.copy(
                            ingredients = current.ingredients.map { ingredient ->
                                if (ingredient is IngredientUiData.Item && ingredient.id == event.ingredientId) {
                                    ingredient.copy(isChecked = !ingredient.isChecked)
                                } else {
                                    ingredient
                                }
                            }
                        )
                    )
                }
            }

            is RecipeDetailContract.RecipeDetailEvent.OpenRecipe -> direction.openRecipe(event.recipeId)

            is RecipeDetailContract.RecipeDetailEvent.OpenTimer -> {
                stopTimer()
                val seconds = (event.step.timerMinutes ?: 0) * 60
                intent {
                    reduce {
                        state.copy(
                            timer = RecipeDetailContract.TimerUiState(
                                stepNumber = event.step.number,
                                totalSeconds = seconds,
                                remainingSeconds = seconds
                            )
                        )
                    }
                }
            }

            RecipeDetailContract.RecipeDetailEvent.CloseTimer -> {
                stopTimer()
                intent { reduce { state.copy(timer = null) } }
            }

            RecipeDetailContract.RecipeDetailEvent.ToggleTimer -> intent {
                val timer = state.timer ?: return@intent
                when {
                    timer.isFinished -> Unit
                    timer.isRunning -> {
                        stopTimer()
                        reduce { state.copy(timer = timer.copy(isRunning = false)) }
                    }
                    else -> {
                        reduce { state.copy(timer = timer.copy(isRunning = true)) }
                        startTimer()
                    }
                }
            }

            RecipeDetailContract.RecipeDetailEvent.ResetTimer -> {
                stopTimer()
                intent {
                    reduce {
                        state.copy(
                            timer = state.timer?.let {
                                it.copy(remainingSeconds = it.totalSeconds, isRunning = false)
                            }
                        )
                    }
                }
            }

            RecipeDetailContract.RecipeDetailEvent.OpenShoppingSheet -> intent {
                reduce { state.copy(shoppingSelection = ingredientIds(state.recipe)) }
            }

            RecipeDetailContract.RecipeDetailEvent.CloseShoppingSheet -> intent {
                reduce { state.copy(shoppingSelection = null) }
            }

            is RecipeDetailContract.RecipeDetailEvent.ToggleShoppingItem -> intent {
                reduce {
                    val selection = state.shoppingSelection ?: return@reduce state
                    state.copy(
                        shoppingSelection = if (event.ingredientId in selection) {
                            selection - event.ingredientId
                        } else {
                            selection + event.ingredientId
                        }
                    )
                }
            }

            RecipeDetailContract.RecipeDetailEvent.ToggleAllShopping -> intent {
                reduce {
                    val selection = state.shoppingSelection ?: return@reduce state
                    val all = ingredientIds(state.recipe)
                    state.copy(shoppingSelection = if (selection.size == all.size) emptySet() else all)
                }
            }

            RecipeDetailContract.RecipeDetailEvent.ConfirmShopping -> intent {
                if (state.shoppingSelection.isNullOrEmpty()) return@intent
                reduce { state.copy(shoppingSelection = null) }
                postSideEffect(RecipeDetailContract.SideEffect.AddedToShoppingList)
            }

            RecipeDetailContract.RecipeDetailEvent.ToggleFavorite -> intent {
                reduce {
                    val current = state.recipe
                    state.copy(recipe = current?.copy(isFavorite = !current.isFavorite))
                }
            }

            RecipeDetailContract.RecipeDetailEvent.Share -> intent {
                state.recipe?.let { postSideEffect(RecipeDetailContract.SideEffect.ShareUrl(it.url)) }
            }

            RecipeDetailContract.RecipeDetailEvent.OpenInBrowser -> intent {
                state.recipe?.let { postSideEffect(RecipeDetailContract.SideEffect.OpenUrl(it.url)) }
            }

            RecipeDetailContract.RecipeDetailEvent.OpenHome -> direction.openHome()

            RecipeDetailContract.RecipeDetailEvent.Back -> direction.back()
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
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun ingredientIds(recipe: RecipeDetailUiData?): Set<Int> =
        recipe?.ingredients
            ?.filterIsInstance<IngredientUiData.Item>()
            ?.map { it.id }
            ?.toSet()
            ?: emptySet()

    override val container =
        orbitContainer<RecipeDetailContract.RecipeDetailUiState, RecipeDetailContract.SideEffect>(
            RecipeDetailContract.RecipeDetailUiState()
        )
}
