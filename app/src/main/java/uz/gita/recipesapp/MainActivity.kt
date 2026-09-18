package uz.gita.recipesapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.recipesapp.navigation.AppNavigationHandler
import uz.gita.recipesapp.presenter.screens.splash.SplashScreen
import uz.gita.recipesapp.presenter.ui.state.AppSettingsStore
import uz.gita.recipesapp.presenter.ui.state.LocalizedApp
import uz.gita.recipesapp.presenter.ui.state.ThemeMode
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: AppNavigationHandler

    @Inject
    lateinit var settings: AppSettingsStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val language by settings.language.collectAsStateWithLifecycle()
            val themeMode by settings.themeMode.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> systemDark
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme }
                )
                onDispose { }
            }

            LocalizedApp(language) {
                OshxonaTheme(darkTheme = darkTheme) {
                    Navigator(SplashScreen()) { navigator ->
                        LaunchedEffect(navigator) {
                            navigationHandler.backStack.collect { command -> command(navigator) }
                        }
                        SlideTransition(navigator)
                    }
                }
            }
        }
    }
}
