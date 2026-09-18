package uz.gita.recipesapp.presenter.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Spacing {
    val xxs: Dp = 4.dp
    val xs: Dp = 8.dp
    val sm: Dp = 12.dp
    val md: Dp = 16.dp
    val lg: Dp = 24.dp
    val xl: Dp = 32.dp
    val xxl: Dp = 40.dp

    val topBarTop: Dp = 56.dp

    val bottomNavBottom: Dp = 24.dp
}

object Radius {
    val badge: Dp = 4.dp
    val input: Dp = 16.dp
    val image: Dp = 16.dp
    val card: Dp = 20.dp
    val hero: Dp = 24.dp
    val button: Dp = 16.dp
    val pill: Dp = 999.dp
}

object Shapes {
    val badge = RoundedCornerShape(Radius.badge)
    val input = RoundedCornerShape(Radius.input)
    val image = RoundedCornerShape(Radius.image)
    val card = RoundedCornerShape(Radius.card)
    val hero = RoundedCornerShape(Radius.hero)
    val button = RoundedCornerShape(Radius.button)
    val pill = RoundedCornerShape(Radius.pill)
}

object Sizes {
    val touchTarget: Dp = 44.dp

    val iconButton: Dp = 44.dp
    val icon: Dp = 24.dp
    val iconSm: Dp = 20.dp

    val listThumb: Dp = 88.dp
    val listThumbSm: Dp = 64.dp
    val gridImageHeight: Dp = 96.dp
    val heroHeight: Dp = 202.dp
    val videoHeight: Dp = 219.dp
    val buttonHeight: Dp = 48.dp
    val buttonHeightLarge: Dp = 56.dp
    val chipHeight: Dp = 40.dp
    val categoryIconBox: Dp = 48.dp
    val checkbox: Dp = 24.dp
    val bottomBar: Dp = 64.dp
    val stepBadge: Dp = 32.dp
    val emptyIcon: Dp = 72.dp
}

fun Modifier.cardShadow(
    radius: Dp = Radius.card,
    elevation: Dp = 10.dp,
): Modifier = this.shadow(
    elevation = elevation,
    shape = RoundedCornerShape(radius),
    ambientColor = Color(0x141B1A17),
    spotColor = Color(0x121B1A17),
    clip = false
)

fun Modifier.heroShadow(radius: Dp = Radius.hero): Modifier = this.shadow(
    elevation = 18.dp,
    shape = RoundedCornerShape(radius),
    ambientColor = Color(0x1C1B1A17),
    spotColor = Color(0x1C1B1A17),
    clip = false
)

fun Modifier.primaryShadow(radius: Dp = Radius.button): Modifier = this.shadow(
    elevation = 12.dp,
    shape = RoundedCornerShape(radius),
    ambientColor = Color(0x331B1A17),
    spotColor = Color(0x331B1A17),
    clip = false
)
