package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import uz.gita.recipesapp.presenter.ui.theme.oshxona

@Composable
fun RecipeImage(
    url: String,
    toneSeed: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val tones = MaterialTheme.oshxona.placeholderTones
    val tone = tones[((toneSeed % tones.size) + tones.size) % tones.size]

    Box(modifier = modifier.background(tone)) {
        if (url.isNotBlank()) {
            AsyncImage(
                model = url,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}
