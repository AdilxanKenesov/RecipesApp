package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.state.UiMessage
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona

@Composable
fun OshxonaSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    val colors = MaterialTheme.oshxona
    Snackbar(
        modifier = modifier.padding(horizontal = Spacing.md),
        shape = Shapes.input,
        containerColor = colors.ink,
        contentColor = colors.ground
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(Sizes.iconSm)
                )
                Spacer(Modifier.size(Spacing.xs))
            }
            Text(
                text = data.visuals.message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun BoxScope.AppMessageHost(
    messages: Flow<UiMessage>,
    isOnline: Boolean,
) {
    val context = LocalContext.current
    val messageState = remember { SnackbarHostState() }
    val offlineState = remember { SnackbarHostState() }
    val offlineText = stringResource(R.string.error_no_internet)

    LaunchedEffect(messages, context) {
        messages.collectLatest { message ->
            messageState.showSnackbar(message.resolve(context))
        }
    }

    LaunchedEffect(isOnline, offlineText) {
        if (!isOnline) {
            offlineState.showSnackbar(offlineText, duration = SnackbarDuration.Indefinite)
        }
    }

    SnackbarHost(
        hostState = offlineState,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .statusBarsPadding()
            .padding(top = Spacing.xs)
    ) { data ->
        OshxonaSnackbar(data = data, icon = Icons.Rounded.WifiOff)
    }

    SnackbarHost(
        hostState = messageState,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(bottom = Sizes.bottomBar + Spacing.xs)
    ) { data ->
        OshxonaSnackbar(data = data)
    }
}
