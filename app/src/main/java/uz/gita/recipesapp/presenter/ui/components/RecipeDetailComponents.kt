package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import uz.gita.recipesapp.domain.module.IngredientUiData
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

@Composable
fun IngredientGroupHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.oshxona
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = colors.primaryInk,
        modifier = modifier.padding(top = Spacing.sm, bottom = Spacing.xxs)
    )
}

@Composable
fun IngredientRow(
    item: IngredientUiData.Item,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    strikeWhenChecked: Boolean = true,
) {
    val colors = MaterialTheme.oshxona
    val struck = item.isChecked && strikeWhenChecked
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = Sizes.buttonHeight)
            .scaleClickable(role = null) { onCheckedChange(!item.isChecked) }
            .padding(vertical = Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange,
            colors = oshxonaCheckboxColors()
        )
        Spacer(Modifier.size(Spacing.xxs))
        Text(
            text = item.amount,
            style = MaterialTheme.typography.labelMedium,
            color = if (struck) colors.inkDisabled else colors.ink,
            textDecoration = if (struck) TextDecoration.LineThrough else null
        )
        Spacer(Modifier.size(Spacing.xs))
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (struck) colors.inkDisabled else colors.inkMuted,
            textDecoration = if (struck) TextDecoration.LineThrough else null
        )
    }
}

@Composable
fun oshxonaCheckboxColors(): CheckboxColors {
    val colors = MaterialTheme.oshxona
    return CheckboxDefaults.colors(
        checkedColor = colors.primary,
        uncheckedColor = if (colors.isDark) colors.inkFaint else colors.hairline,
        checkmarkColor = colors.onPrimary
    )
}
