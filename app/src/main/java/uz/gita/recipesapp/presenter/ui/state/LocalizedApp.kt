package uz.gita.recipesapp.presenter.ui.state

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import java.util.Locale

private class LocalizedContext(
    base: Context,
    private val localizedResources: Resources
) : ContextWrapper(base) {

    override fun getResources(): Resources = localizedResources
}

@Composable
fun LocalizedApp(
    language: AppLanguage,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val localized = remember(language, context) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocales(LocaleList(locale))
        val localizedResources = context.createConfigurationContext(configuration).resources
        LocalizedContext(context, localizedResources)
    }

    CompositionLocalProvider(
        LocalContext provides localized,
        LocalResources provides localized.resources,
        LocalConfiguration provides localized.resources.configuration,
        content = content
    )
}
