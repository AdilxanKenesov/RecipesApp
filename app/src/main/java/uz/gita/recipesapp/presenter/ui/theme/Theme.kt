package uz.gita.recipesapp.presenter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class OshxonaColors(
    val ground: Color,
    val surface: Color,
    val surfaceAlt: Color,
    val hairline: Color,
    val hairlineSoft: Color,
    val ink: Color,
    val inkMuted: Color,
    val inkFaint: Color,
    val inkDisabled: Color,
    val primary: Color,
    val onPrimary: Color,
    val primaryInk: Color,
    val primaryTint: Color,
    val accent: Color,
    val accentTint: Color,
    val accentInk: Color,
    val error: Color,
    val errorTint: Color,
    val skeleton: Color,
    val skeletonSoft: Color,
    val placeholderTones: List<Color>,
    val scrim: Color,
    val isDark: Boolean,
)

private val LightColors = OshxonaColors(
    ground = Ground,
    surface = SurfaceLight,
    surfaceAlt = SurfaceAlt,
    hairline = Hairline,
    hairlineSoft = HairlineSoft,
    ink = Ink,
    inkMuted = InkMuted,
    inkFaint = InkFaint,
    inkDisabled = InkDisabled,
    primary = Primary,
    onPrimary = OnPrimary,
    primaryInk = PrimaryInk,
    primaryTint = PrimaryTint,
    accent = Accent,
    accentTint = AccentTint,
    accentInk = AccentInk,
    error = ErrorRed,
    errorTint = ErrorTint,
    skeleton = Skeleton,
    skeletonSoft = SkeletonSoft,
    placeholderTones = PlaceholderTones,
    scrim = Scrim,
    isDark = false,
)

private val DarkColors = OshxonaColors(
    ground = GroundDark,
    surface = SurfaceDark,
    surfaceAlt = SurfaceAltDark,
    hairline = HairlineDark,
    hairlineSoft = HairlineSoftDark,
    ink = InkDark,
    inkMuted = InkMutedDark,
    inkFaint = InkFaintDark,
    inkDisabled = InkDisabledDark,
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryInk = PrimaryInkDark,
    primaryTint = PrimaryTintDark,
    accent = AccentDark,
    accentTint = AccentTintDark,
    accentInk = AccentInkDark,
    error = ErrorDark,
    errorTint = ErrorTintDark,
    skeleton = SkeletonDark,
    skeletonSoft = SkeletonSoftDark,
    placeholderTones = PlaceholderTonesDark,
    scrim = Scrim,
    isDark = true,
)

val LocalOshxonaColors = staticCompositionLocalOf { LightColors }

val MaterialTheme.oshxona: OshxonaColors
    @Composable get() = LocalOshxonaColors.current

private val M3Light = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryTint,
    onPrimaryContainer = PrimaryInk,
    secondary = AccentInk,
    onSecondary = OnPrimary,
    secondaryContainer = AccentTint,
    onSecondaryContainer = AccentInk,
    background = Ground,
    onBackground = Ink,
    surface = SurfaceLight,
    onSurface = Ink,
    surfaceVariant = SurfaceAlt,
    onSurfaceVariant = InkMuted,
    outline = Hairline,
    outlineVariant = HairlineSoft,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorTint,
    onErrorContainer = ErrorRed,
)

private val M3Dark = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryTintDark,
    onPrimaryContainer = PrimaryInkDark,
    secondary = AccentInkDark,
    onSecondary = OnPrimaryDark,
    secondaryContainer = AccentTintDark,
    onSecondaryContainer = AccentInkDark,
    background = GroundDark,
    onBackground = InkDark,
    surface = SurfaceDark,
    onSurface = InkDark,
    surfaceVariant = SurfaceAltDark,
    onSurfaceVariant = InkMutedDark,
    outline = HairlineDark,
    outlineVariant = HairlineSoftDark,
    error = ErrorDark,
    onError = OnPrimaryDark,
    errorContainer = ErrorTintDark,
    onErrorContainer = ErrorDark,
)

@Composable
fun OshxonaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val tokens = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(
        LocalOshxonaColors provides tokens
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) M3Dark else M3Light,
            typography = OshxonaTypography,
            content = content
        )
    }
}
