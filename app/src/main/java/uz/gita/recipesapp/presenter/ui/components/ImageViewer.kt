package uz.gita.recipesapp.presenter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState
import me.saket.telephoto.zoomable.rememberZoomableState
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.theme.Overlay
import uz.gita.recipesapp.presenter.ui.theme.Shapes
import uz.gita.recipesapp.presenter.ui.theme.Spacing

private const val MAX_ZOOM_FACTOR = 4f

@Composable
fun ZoomableImageViewer(
    images: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit,
) {
    if (images.isEmpty()) return
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val pagerState = rememberPagerState(
            initialPage = initialIndex.coerceIn(0, images.lastIndex)
        ) { images.size }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ZoomableAsyncImage(
                    model = images[page],
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    state = rememberZoomableImageState(
                        rememberZoomableState(zoomSpec = ZoomSpec(maxZoomFactor = MAX_ZOOM_FACTOR))
                    )
                )
            }

            OshxonaIconButton(
                icon = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.cd_close),
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(Spacing.sm),
                tint = Color.White,
                background = Overlay.copy(alpha = 0.5f)
            )

            if (images.size > 1) {
                Text(
                    text = "${pagerState.currentPage + 1}/${images.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(Spacing.sm)
                        .clip(Shapes.pill)
                        .background(Overlay.copy(alpha = 0.5f))
                        .padding(horizontal = Spacing.sm, vertical = Spacing.xs)
                )
            }
        }
    }
}
