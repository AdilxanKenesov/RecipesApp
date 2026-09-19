package uz.gita.recipesapp.presenter.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

val LocalIsOnline = compositionLocalOf { true }

@Composable
fun RetryWhenOnline(hasError: Boolean, onRetry: () -> Unit) {
    val isOnline = LocalIsOnline.current
    val currentHasError by rememberUpdatedState(hasError)
    val currentOnRetry by rememberUpdatedState(onRetry)
    LaunchedEffect(isOnline) {
        if (isOnline && currentHasError) currentOnRetry()
    }
}
