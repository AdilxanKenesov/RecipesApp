package uz.gita.recipesapp.presenter.ui.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

private val youTubeIdRegex = Regex("""(?:[?&]v=|youtu\.be/|/embed/|/shorts/|/live/)([A-Za-z0-9_-]{11})""")

fun String.youTubeVideoId(): String? = youTubeIdRegex.find(this)?.groupValues?.get(1)

fun Context.findActivity(): Activity? {
    var current: Context? = this
    while (current != null) {
        if (current is Activity) return current
        current = (current as? ContextWrapper)?.baseContext
    }
    return null
}
