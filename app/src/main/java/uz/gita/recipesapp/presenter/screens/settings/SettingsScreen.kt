package uz.gita.recipesapp.presenter.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.module.AppLanguage
import uz.gita.recipesapp.domain.module.ThemeMode
import uz.gita.recipesapp.presenter.ui.components.OshxonaScaffold
import uz.gita.recipesapp.presenter.ui.components.ScreenTopBar
import uz.gita.recipesapp.presenter.ui.preview.ThemePreview
import uz.gita.recipesapp.presenter.ui.theme.OshxonaTheme
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.cardShadow
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: SettingsContract.SettingsViewModel = getViewModel<SettingsViewModel>()
        val state by viewModel.collectAsState()

        SettingsContent(state, viewModel::onEventDispatcher)
    }

    @Composable
    private fun SettingsContent(
        state: SettingsContract.SettingsUiState,
        onEventDispatcher: (SettingsContract.SettingsEvent) -> Unit
    ) {
        val colors = MaterialTheme.oshxona

        OshxonaScaffold(
            topBar = {
                ScreenTopBar(
                    title = stringResource(R.string.settings_title),
                    onBack = { onEventDispatcher(SettingsContract.SettingsEvent.Back) }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.md)
            ) {
                SettingsGroup(title = stringResource(R.string.settings_language)) {
                    SettingsOption(
                        label = stringResource(R.string.language_uz),
                        leading = { FlagIcon(FLAG_UZ) },
                        selected = state.language == AppLanguage.UZ,
                        onClick = {
                            onEventDispatcher(
                                SettingsContract.SettingsEvent.LanguageChanged(AppLanguage.UZ)
                            )
                        }
                    )
                    HorizontalDivider(color = colors.hairlineSoft)
                    SettingsOption(
                        label = stringResource(R.string.language_ru),
                        leading = { FlagIcon(FLAG_RU) },
                        selected = state.language == AppLanguage.RU,
                        onClick = {
                            onEventDispatcher(
                                SettingsContract.SettingsEvent.LanguageChanged(AppLanguage.RU)
                            )
                        }
                    )
                }

                Spacer(Modifier.size(Spacing.xl))

                SettingsGroup(title = stringResource(R.string.settings_appearance)) {
                    ThemeMode.entries.forEachIndexed { index, mode ->
                        if (index > 0) HorizontalDivider(color = colors.hairlineSoft)
                        SettingsOption(
                            label = stringResource(
                                when (mode) {
                                    ThemeMode.LIGHT -> R.string.settings_appearance_light
                                    ThemeMode.DARK -> R.string.settings_appearance_dark
                                }
                            ),
                            leading = {
                                ThemeIcon(
                                    when (mode) {
                                        ThemeMode.LIGHT -> Icons.Rounded.LightMode
                                        ThemeMode.DARK -> Icons.Rounded.DarkMode
                                    }
                                )
                            },
                            selected = state.themeMode == mode,
                            onClick = {
                                onEventDispatcher(SettingsContract.SettingsEvent.ThemeChanged(mode))
                            }
                        )
                    }
                }

                Spacer(Modifier.size(Spacing.xxl))
            }
        }
    }

    @Composable
    private fun SettingsGroup(
        title: String,
        content: @Composable () -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = colors.ink,
            modifier = Modifier.padding(bottom = Spacing.sm)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .cardShadow()
                .clip(Shapes.card)
                .background(colors.surface)
        ) {
            content()
        }
    }

    @Composable
    private fun SettingsOption(
        label: String,
        leading: @Composable () -> Unit,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .scaleClickable(onClick = onClick)
                .padding(horizontal = Spacing.md, vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                leading()
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selected) colors.primaryInk else colors.ink
                )
            }
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

    @Composable
    private fun OptionIconBox(content: @Composable () -> Unit) {
        Box(
            modifier = Modifier
                .size(OPTION_ICON_SIZE)
                .clip(CircleShape)
                .background(MaterialTheme.oshxona.primaryTint),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }

    @Composable
    private fun FlagIcon(flag: String) {
        OptionIconBox {
            Text(text = flag, style = MaterialTheme.typography.titleMedium)
        }
    }

    @Composable
    private fun ThemeIcon(icon: ImageVector) {
        OptionIconBox {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.oshxona.primaryInk,
                modifier = Modifier.size(Sizes.iconSm)
            )
        }
    }

    @ThemePreview
    @Composable
    private fun SettingsPreview() {
        OshxonaTheme {
            SettingsContent(
                state = SettingsContract.SettingsUiState(),
                onEventDispatcher = { }
            )
        }
    }

    private companion object {
        const val FLAG_UZ = "\uD83C\uDDFA\uD83C\uDDFF"
        const val FLAG_RU = "\uD83C\uDDF7\uD83C\uDDFA"
        val OPTION_ICON_SIZE = 36.dp
    }
}
