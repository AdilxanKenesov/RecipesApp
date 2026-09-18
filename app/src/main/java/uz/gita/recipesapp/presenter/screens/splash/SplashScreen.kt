package uz.gita.recipesapp.presenter.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.components.PrimaryButton
import uz.gita.recipesapp.presenter.ui.preview.ThemePreview
import uz.gita.recipesapp.presenter.ui.theme.Overlay
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona

class SplashScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: SplashContract.SplashViewModel = getViewModel<SplashViewModel>()
        val state by viewModel.collectAsState()

        SplashContent(state, viewModel::onEventDispatcher)
    }

    @Composable
    private fun SplashContent(
        state: SplashContract.SplashUiState,
        onEventDispatcher: (SplashContract.SplashEvent) -> Unit
    ) {
        OshxonaTheme(darkTheme = true) {
            SplashBody(onEventDispatcher)
        }
    }

    @Composable
    private fun SplashBody(
        onEventDispatcher: (SplashContract.SplashEvent) -> Unit
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Overlay.copy(alpha = 0.2f), Overlay.copy(alpha = 0.94f))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = Spacing.md)
                    .padding(bottom = Spacing.xxl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.splash_title),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.oshxona.ink,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.size(Spacing.sm))
                Text(
                    text = stringResource(R.string.splash_tagline),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.oshxona.ink,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.size(Spacing.xl))
                PrimaryButton(
                    text = stringResource(R.string.splash_start),
                    onClick = { onEventDispatcher(SplashContract.SplashEvent.Start) },
                    trailingIcon = Icons.AutoMirrored.Rounded.ArrowForward,
                    shape = Shapes.pill
                )
            }
        }
    }

    @ThemePreview
    @Composable
    private fun SplashPreview() {
        OshxonaTheme {
            SplashContent(SplashContract.SplashUiState()) { }
        }
    }
}
