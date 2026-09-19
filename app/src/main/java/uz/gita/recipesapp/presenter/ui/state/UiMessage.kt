package uz.gita.recipesapp.presenter.ui.state

import android.content.Context
import androidx.annotation.StringRes

sealed interface UiMessage {
    data class Text(val value: String) : UiMessage
    data class Resource(@StringRes val id: Int) : UiMessage

    fun resolve(context: Context): String =
        when (this) {
            is Text -> value
            is Resource -> context.getString(id)
        }
}
