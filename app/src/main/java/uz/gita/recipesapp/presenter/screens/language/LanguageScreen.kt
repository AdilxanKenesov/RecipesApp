package uz.gita.recipesapp.presenter.screens.language

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.components.PrimaryButton
import uz.gita.recipesapp.presenter.ui.preview.ThemePreview
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.cardShadow
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

class LanguageScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: LanguageContract.LanguageViewModel = getViewModel<LanguageViewModel>()
        val state by viewModel.collectAsState()

        LanguageContent(state, viewModel::onEventDispatcher)
    }

    @Composable
    private fun LanguageContent(
        state: LanguageContract.LanguageUiState,
        onEventDispatcher: (LanguageContract.LanguageEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.ground)
                .statusBarsPadding()
                .padding(horizontal = Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.size(Spacing.xxl))

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(Shapes.hero)
                    .background(colors.primaryTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Restaurant,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(Modifier.size(Spacing.md))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall,
                color = colors.ink
            )
            Spacer(Modifier.size(Spacing.xxs))
            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.inkMuted,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.size(Spacing.xl))
            Text(
                text = stringResource(R.string.language_title),
                style = MaterialTheme.typography.titleLarge,
                color = colors.ink,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(Spacing.sm))

            LanguageOption(
                label = stringResource(R.string.language_uz),
                selected = state.selected == AppLanguage.UZ,
                onClick = { onEventDispatcher(LanguageContract.LanguageEvent.Select(AppLanguage.UZ)) }
            )
            Spacer(Modifier.size(Spacing.sm))
            LanguageOption(
                label = stringResource(R.string.language_ru),
                selected = state.selected == AppLanguage.RU,
                onClick = { onEventDispatcher(LanguageContract.LanguageEvent.Select(AppLanguage.RU)) }
            )

            Spacer(Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.language_start),
                onClick = { onEventDispatcher(LanguageContract.LanguageEvent.Confirm) },
                shape = Shapes.pill
            )
            Spacer(Modifier.size(Spacing.sm))
            Text(
                text = stringResource(R.string.language_hint),
                style = MaterialTheme.typography.labelSmall,
                color = colors.inkFaint,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(Spacing.lg))
            Spacer(Modifier.navigationBarsPadding())
        }
    }

    @Composable
    private fun LanguageOption(
        label: String,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .cardShadow()
                .clip(Shapes.card)
                .background(if (selected) colors.primaryTint else colors.surface)
                .border(
                    border = BorderStroke(if (selected) 1.5.dp else 1.dp, if (selected) colors.primary else colors.hairline),
                    shape = Shapes.card
                )
                .scaleClickable(onClick = onClick)
                .padding(Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = if (selected) colors.primaryInk else colors.ink
            )
            if (selected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(Sizes.iconSm)
                )
            }
        }
    }

    @ThemePreview
    @Composable
    private fun LanguagePreview() {
        OshxonaTheme {
            LanguageContent(LanguageContract.LanguageUiState()) { }
        }
    }
}
