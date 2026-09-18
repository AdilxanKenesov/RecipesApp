package uz.gita.recipesapp.presenter.ui.state

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class MainTab(val index: Int) {
    HOME(0),
    CATEGORIES(1),
    SEARCH(2),
    SAVED(3)
}

@Singleton
class TabSwitcher @Inject constructor() {

    private val _target = MutableSharedFlow<MainTab>(replay = 1, extraBufferCapacity = 1)
    val target: SharedFlow<MainTab> = _target.asSharedFlow()

    private val _pendingIngredientSearch = MutableStateFlow(false)
    val pendingIngredientSearch: StateFlow<Boolean> = _pendingIngredientSearch.asStateFlow()

    fun switchTo(tab: MainTab) {
        _target.tryEmit(tab)
    }

    fun openIngredientSearch() {
        _pendingIngredientSearch.value = true
        _target.tryEmit(MainTab.SEARCH)
    }

    fun consumeIngredientSearch() {
        _pendingIngredientSearch.value = false
    }

    fun consumeTarget() {
        _target.resetReplayCache()
    }
}
