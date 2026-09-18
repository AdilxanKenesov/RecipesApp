package uz.gita.recipesapp.presenter.ui.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class ThemePreview

@ThemePreview
@Preview(name = "Russian", locale = "ru")
annotation class ThemeLocalePreview

@Composable
fun PreviewContainer(
    darkTheme: Boolean = isSystemInDarkTheme(),
    padded: Boolean = true,
    content: @Composable () -> Unit,
) {
    OshxonaTheme(darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.oshxona.ground)
                .padding(if (padded) Spacing.md else 0.dp)
        ) {
            content()
        }
    }
}
