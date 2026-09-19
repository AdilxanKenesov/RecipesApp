package uz.gita.recipesapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.recipesapp.domain.module.ThemeMode
import uz.gita.recipesapp.domain.repository.NetworkRepository
import uz.gita.recipesapp.domain.repository.SettingsRepository
import uz.gita.recipesapp.navigation.AppNavigationHandler
import uz.gita.recipesapp.presenter.screens.main.MainScreen
import uz.gita.recipesapp.presenter.screens.splash.SplashScreen
import uz.gita.recipesapp.presenter.ui.components.AppMessageHost
import uz.gita.recipesapp.presenter.ui.state.AppMessenger
import uz.gita.recipesapp.presenter.ui.state.LocalizedApp
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.util.LocalIsOnline
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: AppNavigationHandler

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var networkRepository: NetworkRepository

    @Inject
    lateinit var messenger: AppMessenger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val startScreen = if (settingsRepository.isActive()) MainScreen() else SplashScreen()
        setContent {
            val language by settingsRepository.getLanguage().collectAsStateWithLifecycle()
            val themeMode by settingsRepository.getThemeMode().collectAsStateWithLifecycle()
            val isOnline by networkRepository.isOnline().collectAsStateWithLifecycle()
            val darkTheme = themeMode == ThemeMode.DARK

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme }
                )
                onDispose { }
            }

            LocalizedApp(language) {
                OshxonaTheme(darkTheme = darkTheme) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CompositionLocalProvider(LocalIsOnline provides isOnline) {
                            Navigator(startScreen) { navigator ->
                                LaunchedEffect(navigator) {
                                    navigationHandler.backStack.collect { command -> command(navigator) }
                                }
                                SlideTransition(navigator)
                            }
                        }
                        AppMessageHost(messages = messenger.messages, isOnline = isOnline)
                    }
                }
            }
        }
    }
}
