package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.primaryShadow
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    shape: Shape = Shapes.button,
    fillWidth: Boolean = true,
    height: Dp = Sizes.buttonHeightLarge,
    elevated: Boolean = true,
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .then(if (fillWidth) Modifier.fillMaxWidth() else Modifier)
            .height(height)
            .then(if (enabled && elevated) Modifier.primaryShadow() else Modifier)
            .clip(shape)
            .background(if (enabled) colors.primary else colors.surfaceAlt)
            .scaleClickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = if (fillWidth) Spacing.md else Spacing.lg),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = if (enabled) colors.onPrimary else colors.inkDisabled,
                modifier = Modifier.size(Sizes.iconSm)
            )
            Spacer(Modifier.size(Spacing.xs))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) colors.onPrimary else colors.inkDisabled,
            textAlign = TextAlign.Center
        )
        if (trailingIcon != null) {
            Spacer(Modifier.size(Spacing.xs))
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = if (enabled) colors.onPrimary else colors.inkDisabled,
                modifier = Modifier.size(Sizes.iconSm)
            )
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    val colors = MaterialTheme.oshxona
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.buttonHeight)
            .clip(Shapes.button)
            .background(colors.ground)
            .border(
                border = BorderStroke(1.5.dp, if (enabled) colors.primary else colors.hairline),
                shape = Shapes.button
            )
            .scaleClickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = Spacing.md),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = if (enabled) colors.primaryInk else colors.inkDisabled,
                modifier = Modifier.size(Sizes.iconSm)
            )
            Spacer(Modifier.size(Spacing.xs))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) colors.primaryInk else colors.inkDisabled,
            textAlign = TextAlign.Center
        )
        if (trailingIcon != null) {
            Spacer(Modifier.size(Spacing.xs))
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = if (enabled) colors.primaryInk else colors.inkDisabled,
                modifier = Modifier.size(Sizes.iconSm)
            )
        }
    }
}

@Composable
fun OshxonaIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    background: Color? = null,
) {
    val colors = MaterialTheme.oshxona
    Box(
        modifier = modifier
            .size(Sizes.iconButton)
            .clip(CircleShape)
            .background(background ?: Color.Transparent)
            .scaleClickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint ?: colors.ink,
            modifier = Modifier.size(Sizes.icon)
        )
    }
}
