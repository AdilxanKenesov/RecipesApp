package uz.gita.recipesapp.data.source.local.pref

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.ThemeMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var isActive: Boolean
        get() = preferences.getBoolean(KEY_IS_ACTIVE, false)
        set(value) = preferences.edit { putBoolean(KEY_IS_ACTIVE, value) }

    var language: AppLanguage
        get() = AppLanguage.fromCode(preferences.getString(KEY_LANGUAGE, null) ?: AppLanguage.UZ.code)
        set(value) = preferences.edit { putString(KEY_LANGUAGE, value.code) }

    var themeMode: ThemeMode
        get() {
            val name = preferences.getString(KEY_THEME_MODE, null)
            return ThemeMode.entries.firstOrNull { it.name == name } ?: ThemeMode.LIGHT
        }
        set(value) = preferences.edit { putString(KEY_THEME_MODE, value.name) }

    var recentSearches: List<String>
        get() = preferences.getString(KEY_RECENT_SEARCHES, null)
            ?.split(RECENT_SEPARATOR)
            ?.filter { it.isNotBlank() }
            ?: emptyList()
        set(value) = preferences.edit { putString(KEY_RECENT_SEARCHES, value.joinToString(RECENT_SEPARATOR)) }

    companion object {
        private const val FILE_NAME = "pazanda_prefs"
        private const val KEY_IS_ACTIVE = "is_active"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_RECENT_SEARCHES = "recent_searches"
        private const val RECENT_SEPARATOR = "\n"
    }
}
