package uz.gita.recipesapp.presenter.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import kotlinx.coroutines.launch
import uz.gita.recipesapp.presenter.ui.theme.Overlay
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import kotlin.math.roundToInt

val SheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

@Composable
fun glassStyle(): HazeStyle {
    val colors = MaterialTheme.oshxona
    return HazeStyle(
        backgroundColor = colors.ground,
        tint = HazeTint(colors.ground.copy(alpha = if (colors.isDark) 0.66f else 0.72f)),
        blurRadius = 30.dp,
        noiseFactor = 0.06f,
        fallbackTint = HazeTint(colors.ground.copy(alpha = 0.94f)),
    )
}

fun glassOnImageStyle(): HazeStyle = HazeStyle(
    backgroundColor = Overlay,
    tint = HazeTint(Overlay.copy(alpha = 0.32f)),
    blurRadius = 20.dp,
    noiseFactor = 0.04f,
    fallbackTint = HazeTint(Overlay.copy(alpha = 0.55f)),
)

@Composable
fun SheetHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(top = Spacing.sm, bottom = Spacing.xs)
            .size(width = 36.dp, height = 4.dp)
            .clip(Shapes.pill)
            .background(MaterialTheme.oshxona.ink.copy(alpha = 0.22f))
    )
}

fun Modifier.glassTopEdge(color: Color): Modifier = drawWithContent {
    drawContent()
    val stroke = 1.dp.toPx()
    drawLine(
        brush = Brush.horizontalGradient(listOf(Color.Transparent, color, Color.Transparent)),
        start = Offset(0f, stroke / 2f),
        end = Offset(size.width, stroke / 2f),
        strokeWidth = stroke
    )
}

@Composable
fun GlassBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MaterialTheme.oshxona
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(0f) }
    val dismissThreshold = with(LocalDensity.current) { 120.dp.toPx() }

    LaunchedEffect(visible) {
        if (visible) dragOffset.snapTo(0f)
    }

    BackHandler(enabled = visible, onBack = onDismiss)

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Overlay.copy(alpha = 0.45f))
                    .pointerInput(Unit) { detectTapGestures { onDismiss() } }
            )
        }

        AnimatedVisibility(
            visible = visible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(tween(280)) { it } + fadeIn(tween(200)),
            exit = slideOutVertically(tween(220)) { it } + fadeOut(tween(180))
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, dragOffset.value.roundToInt()) }
                    .clip(SheetShape)
                    .hazeEffect(state = hazeState, style = glassStyle())
                    .glassTopEdge(colors.surface.copy(alpha = if (colors.isDark) 0.18f else 0.9f))
                    .pointerInput(Unit) { detectTapGestures { } }
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            scope.launch {
                                dragOffset.snapTo((dragOffset.value + delta).coerceAtLeast(0f))
                            }
                        },
                        onDragStopped = { velocity ->
                            if (dragOffset.value > dismissThreshold || velocity > 1800f) {
                                onDismiss()
                            } else {
                                dragOffset.animateTo(0f, spring())
                            }
                        }
                    )
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = Spacing.md)
                    .padding(bottom = Spacing.md),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SheetHandle()
                content()
            }
        }
    }
}

@Composable
fun CountdownRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 220.dp,
    strokeWidth: Dp = 10.dp,
    content: @Composable () -> Unit,
) {
    val colors = MaterialTheme.oshxona
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = strokeWidth.toPx()
            val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
            val topLeft = Offset(stroke / 2f, stroke / 2f)
            drawArc(
                color = colors.hairline,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke)
            )
            drawArc(
                color = colors.primary,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        content()
    }
}

private class LastValue<T : Any> {
    var value: T? = null
}

@Composable
fun <T : Any> rememberLastNonNull(value: T?): T? {
    val holder = remember { LastValue<T>() }
    if (value != null) holder.value = value
    return holder.value
}
